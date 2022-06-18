package com.atguigu.service;

import com.atguigu.bean.Cart;
import com.atguigu.bean.User;

import java.sql.SQLException;

public interface OrderService {
    /**
     * 结算业务
     * @param user
     * @param cart
     * @return
     */
    String checkout(User user, Cart cart) throws Exception;
}
