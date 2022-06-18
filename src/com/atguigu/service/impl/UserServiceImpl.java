package com.atguigu.service.impl;

import com.atguigu.bean.User;
import com.atguigu.dao.UserDao;
import com.atguigu.dao.impl.UserDaoImpl;
import com.atguigu.service.UserService;
import com.atguigu.utils.MD5Util;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2021-05-12  11:26
 */
public class UserServiceImpl implements UserService{
    private UserDao userDao = new UserDaoImpl();
    @Override
    public void doRegister(User user) throws Exception {
        //1. 注册之前:校验用户名是否已存在
        //调用持久层的方法，根据username查找用户，查找到了就表示已存在
        User existUser = userDao.findByUsername(user.getUsername());
        if (existUser != null) {
            //表示当前用户已存在，注册失败
            throw new RuntimeException("用户名已存在");
        }

        //2. 需求: 注册的时候，要对用户的密码进行加密
        String oldUserPwd = user.getUserPwd();
        //将明文进行加密得到加密后的密码
        String encodedPwd = MD5Util.encode(oldUserPwd);
        //将加密后的密码设置到user中
        user.setUserPwd(encodedPwd);

        //3. 处理注册，其实就是调用持久层的方法添加用户
        userDao.addUser(user);
    }

    @Override
    public User doLogin(User parameterUser) throws Exception {
        //1. 调用dao层的方法根据用户名查询用户信息
        User loginUser = userDao.findByUsername(parameterUser.getUsername());

        //2. 判断loginUser是否为空
        if (loginUser != null) {
            //说明用户名正确，那么接下来校验密码
            //parameterUser中的密码是用户输入的密码，我们对其进行MD5加密，完之后跟loginUser中的密码(数据库中的密码)进行比对
            String encodePwd = MD5Util.encode(parameterUser.getUserPwd());//加密之后的用户输入的密码
            String dbPwd = loginUser.getUserPwd();//数据库中的密码
            if (dbPwd.equals(encodePwd)) {
                //说明密码正确,登录成功，返回loginUser对象
                return loginUser;
            }else {
                //密码错误
                throw new RuntimeException("密码错误");
            }
        }
        throw new RuntimeException("用户名错误");
    }

    @Override
    public void findByUserName(String username) throws Exception {
        //调用持久层的方法根据username查询user
        User user = userDao.findByUsername(username);
        if (user != null) {
            throw new RuntimeException("用户名已存在");
        }

    }
}
