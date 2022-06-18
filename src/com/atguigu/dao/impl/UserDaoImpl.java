package com.atguigu.dao.impl;

import com.atguigu.bean.User;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.UserDao;

import java.sql.SQLException;

/**
 * 包名:com.atguigu.dao
 * @author Leevi
 * 日期2021-05-12  10:47
 * 用户模块的持久层类
 *
 * 接口的作用是可以进行模块之间的解耦
 */
public class UserDaoImpl extends BaseDao<User> implements UserDao{
    @Override
    public User findByUsername(String username) throws Exception{
        String sql = "select user_id userId,user_name username,user_pwd userPwd,email from t_user where user_name=?";
        return getBean(User.class,sql,username);
    }

    @Override
    public void addUser(User user) throws Exception {
        String sql = "insert into t_user (user_name,user_pwd,email) values (?,?,?)";
        update(sql,user.getUsername(),user.getUserPwd(),user.getEmail());
    }
}
