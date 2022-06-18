package com.atguigu.dao;

import com.atguigu.bean.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * 包名:com.atguigu.dao
 *
 * @author Leevi
 * 日期2021-05-14  10:23
 */
public interface BookDao {
    /**
     * 查询所有图书列表
     * @return
     */
    List<Book> selectBookList() throws Exception;

    /**
     * 根据id删除图书
     * @param bookId
     * @throws SQLException
     */
    void deleteBook(Integer bookId) throws Exception;

    /**
     * 保存图书信息
     * @param book
     * @throws SQLException
     */
    void insertBook(Book book) throws Exception;

    /**
     * 根据主键查询图书信息
     * @param bookId
     * @return
     */
    Book selectBookByPrimaryKey(Integer bookId) throws Exception;

    /**
     * 修改图书信息
     * @param book
     * @throws SQLException
     */
    void updateBook(Book book) throws Exception;


    /**
     * 批量修改书的库存和销量
     * @param updateBookParamArr
     */
    void updateBookArr(Object[][] updateBookParamArr) throws Exception;
}
