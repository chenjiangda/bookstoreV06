package com.atguigu.servlet.model;

import com.atguigu.bean.Book;
import com.atguigu.service.BookService;
import com.atguigu.service.impl.BookServiceImpl;
import com.atguigu.servlet.base.ModelBaseServlet;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * @author Leevi
 * 日期2021-05-14  10:14
 */
public class BookManagerServlet extends ModelBaseServlet {
    private BookService bookService = new BookServiceImpl();
    /**
     * 跳转到图书管理页面
     * @param request
     * @param response
     */
    public void toBookManagerPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //查询出图书列表
            List<Book> bookList = bookService.getBookList();
            //将图书列表存储到请求域
            request.setAttribute("bookList",bookList);
            processTemplate("manager/book_manager",request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除图书
     * @param request
     * @param response
     * @throws IOException
     */
    public void removeBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 获取要删除的图书的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        //2. 调用业务层的方法根据id删除图书
        try {
            bookService.removeBook(id);
            //3. 删除成功，重新查询所有图书信息
            response.sendRedirect(request.getContextPath()+"/bookManager?method=toBookManagerPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到添加图书页面
     * @param request
     * @param response
     * @throws IOException
     */
    public void toAddPage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        processTemplate("manager/book_edit",request,response);
    }

    /**
     * 添加或者图书信息
     * @param request
     * @param response
     * @throws IOException
     */
    public void saveOrUpdateBook(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1. 获取请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //2. 将parameterMap中的数据封装到Book对象
        try {
            Book book = new Book();
            BeanUtils.populate(book,parameterMap);

            //判断到底是修改还是添加
            if (book.getBookId() != null && !"".equals(book.getBookId())) {
                //修改图书信息
                bookService.editBook(book);
            }else {
                //添加图书信息
                //设置一个固定的imgPath
                book.setImgPath("static/uploads/xiaowangzi.jpg");
                //3. 调用业务层的方法保存图书信息
                bookService.saveBook(book);
            }

            //4. 保存成功，则重新查询所有图书
            response.sendRedirect(request.getContextPath()+"/bookManager?method=toBookManagerPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到修改页面
     * @param request
     * @param response
     * @throws IOException
     */
    public void toEditPage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //获取客户端传入的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            //根据id查询图书详情
            Book book = bookService.getBookById(id);
            //将图书信息存储到请求域
            request.setAttribute("book",book);
            processTemplate("manager/book_edit",request,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
