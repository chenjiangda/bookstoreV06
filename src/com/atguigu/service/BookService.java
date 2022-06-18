package com.atguigu.service;

import com.atguigu.bean.Book;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2021-05-14  10:26
 */
public interface BookService {
    /**
     * 查询所有图书列表
     * @return
     */
    List<Book> getBookList() throws Exception;

    /**
     * 根据图书id删除图书
     * @param bookId
     * @throws Exception
     */
    void removeBook(Integer bookId) throws Exception;

    /**
     * 保存图书信息
     * @param book
     * @throws Exception
     */
    void saveBook(Book book) throws Exception;

    /**
     * 根据id查询图书信息
     * @param bookId
     * @return
     */
    Book getBookById(Integer bookId) throws Exception;

    /**
     * 编辑图书信息
     * @param book
     * @throws Exception
     */
    void editBook(Book book) throws Exception;
}
