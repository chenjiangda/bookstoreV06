package com.atguigu.dao;

import com.atguigu.bean.User;

import java.sql.SQLException;

/**
 * 包名:com.atguigu.dao
 *
 * @author Leevi
 * 日期2021-05-12  11:20
 */
public interface UserDao {

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     * @throws SQLException
     */
    User findByUsername(String username) throws Exception;

    /**
     * 往t_user表中添加用户信息
     * @param user
     */
    void addUser(User user) throws Exception;
}
