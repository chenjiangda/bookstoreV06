package com.atguigu.servlet.model;

import com.atguigu.bean.Book;
import com.atguigu.bean.Cart;
import com.atguigu.bean.CartItem;
import com.atguigu.bean.CommonResult;
import com.atguigu.constant.BookStoreConstants;
import com.atguigu.service.BookService;
import com.atguigu.service.impl.BookServiceImpl;
import com.atguigu.servlet.base.ModelBaseServlet;
import com.atguigu.utils.JsonUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "CartServlet", value = "/protected/cart")
public class CartServlet extends ModelBaseServlet {

    private BookService bookService = new BookServiceImpl();

    /**
     * 添加商品进购物车
     * @param request
     * @param response
     */
    public void addCartItem(HttpServletRequest request,HttpServletResponse response) {
        CommonResult commonResult = null;
        try {
            //1.获取请求参数id的值
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2.根据id调用bookService的方法根据id查询book信息
            Book book = bookService.getBookById(id);
            //3.尝试从会话域中获取购物车
            HttpSession session = request.getSession();
            //下面这句话不是把cart从session中拿出来，而是用一个变量指向session中的cart
            Cart cart = (Cart) session.getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //4.判断之前是否已经添加过购物车
            if (cart == null) {
                //说明这是第一次添加购物车
                //那么就要先创建一个购物车对象
                cart = new Cart();
                //然后将当前book加入到该购物车
                cart.addBookToCart(book);
                //然后将购物车cart存入session，以后第二次加入购物车的时候就能拿到这个cart了
                session.setAttribute(BookStoreConstants.CARTSESSIONKEY, cart);
            } else {
                //说明这不是第一次添加购物车
                //那么就直接用之前的购物车添加当前book
                cart.addBookToCart(book);
                //其实下面这句话不用写了，因为这个cart是指向session中的cart的，他变了session中的cart也会变
                //session.setAttribute("cart",cart);
            }

            System.out.println(cart);

            //添加购物车成功
            //获取购物车中的商品数量
            Integer totalCount = cart.getTotalCount();
            commonResult = CommonResult.ok().setResultData(totalCount);
        } catch (Exception e) {
            e.printStackTrace();
            //添加购物车失败
            commonResult = CommonResult.error().setMessage("添加购物车失败");

        }

        JsonUtils.writeResult(response, commonResult);

    }

    /**
     * 跳转到显示购物车列表的页面
     * @param request
     * @param response
     */
    public void toCartPage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //本来应该是将购物车信息查询出来将它存储到域对象在跳转的，但是这边购物车信息不是存在db中而是存在了session，直接就在域对象里面了
        processTemplate("cart/cart",request,response);

    }

    /**
     * 清空购物车
     * @param request
     * @param response
     */
    public void cleanCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //就是移除掉session中的cart
        request.getSession().removeAttribute(BookStoreConstants.CARTSESSIONKEY);
        //清除完后跳转到购物车展示页面
        processTemplate("cart/cart",request,response);
    }

    /**
     * 购物车中某个购物项的数量-1
     * @param request
     * @param response
     */
    public void countDecrease(HttpServletRequest request,HttpServletResponse response) throws IOException {
        CommonResult commonResult = null;
        try {
            //1. 获取到要-1的书的id
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2. 从session中获取购物车信息
            Cart cart = (Cart) request.getSession().getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //3. 调用购物车的-1方法
            cart.itemCountDecrease(id);

            //将服务器端最新的购物车totalCount和totalAmount响应给客户端
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalCount",cart.getTotalCount());
            responseMap.put("totalAmount",cart.getTotalAmount());

            //直接讲当前购物项的总金额也封装到map中
            responseMap.put("amount",cart.getCartItemMap().get(id).getAmount());

            commonResult = CommonResult.ok().setResultData(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = CommonResult.error();
        }
        JsonUtils.writeResult(response, commonResult);

    }

    /**
     * 购物车中某个购物项的数量+1
     * @param request
     * @param response
     */
    public void countIncrease(HttpServletRequest request,HttpServletResponse response) throws IOException {
        CommonResult commonResult = null;
        try {
            //1. 获取到要-+1的书的id
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2. 从session中获取购物车信息
            Cart cart = (Cart) request.getSession().getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //3. 调用购物车的+1方法
            cart.itemCountIncrease(id);

            //将服务器端最新的购物车totalCount和totalAmount响应给客户端
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalCount",cart.getTotalCount());
            responseMap.put("totalAmount",cart.getTotalAmount());

            //直接讲当前购物项的总金额也封装到map中
            responseMap.put("amount",cart.getCartItemMap().get(id).getAmount());

            commonResult = CommonResult.ok().setResultData(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = CommonResult.error();
        }

        JsonUtils.writeResult(response,commonResult);

    }

    /**
     * 删除购物项
     * @param request
     * @param response
     */
    public void removeCartItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
        CommonResult commonResult = null;
        try {
            //1. 获取要删除的购物项的书的id
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2.从session中获取购物车
            Cart cart = (Cart) request.getSession().getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //3. 调用cart的删除购物项的方法
            cart.removeCartItem(id);

            //将服务器端最新的购物车totalCount和totalAmount响应给客户端
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalCount",cart.getTotalCount());
            responseMap.put("totalAmount",cart.getTotalAmount());
            commonResult = CommonResult.ok().setResultData(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = CommonResult.error();
        }
        JsonUtils.writeResult(response,commonResult);
    }


    /**
     * 修改购物项的数量
     * @param request
     * @param response
     * @throws IOException
     */
    public void updateCartItemCount(HttpServletRequest request,HttpServletResponse response) throws IOException {
        CommonResult commonResult = null;
        try {
            //1. 获取请求参数:id,newCount
            Integer id = Integer.valueOf(request.getParameter("id"));
            Integer newCount = Integer.valueOf(request.getParameter("newCount"));
            //2. 从session中获取购物车信息
            Cart cart = (Cart) request.getSession().getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //3. 调用cart中跟新数量的方法
            cart.updateItemCount(id,newCount);

            //将服务器端最新的购物车totalCount和totalAmount响应给客户端
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalCount",cart.getTotalCount());
            responseMap.put("totalAmount",cart.getTotalAmount());
            commonResult = CommonResult.ok().setResultData(responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            commonResult = CommonResult.error();
        }

        JsonUtils.writeResult(response,commonResult);
    }


    /**
     * 获取购物车的json数据
     * @param request
     * @param response
     */
    public void getCartJSON(HttpServletRequest request,HttpServletResponse response) {
        CommonResult commonResult = null;
        try {
            //1.获取购物车信息
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute(BookStoreConstants.CARTSESSIONKEY);

            //2.我们要响应给客户端的是json:{"totalCount":总条数,"totalAmount":总金额,"cartItemList":购物项的集合}
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalCount", cart.getTotalCount());
            responseMap.put("totalAmount", cart.getTotalAmount());

            //获取购物项列表
            Collection<CartItem> cartItemCollection = cart.getCartItemMap().values();
            List<CartItem> cartItemList = new ArrayList<>(cartItemCollection);
            responseMap.put("cartItemList", cartItemList);

            //查询成功
            commonResult = CommonResult.ok().setResultData(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = CommonResult.error().setMessage("查询购物车信息失败");
        }

        //3.将commonResult转成json并且响应给客户端
        JsonUtils.writeResult(response, commonResult);
    }

}
