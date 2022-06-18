package com.atguigu.dao;

import com.atguigu.bean.Order;

import java.sql.SQLException;

public interface OrderDao {
    /**
     * 保存订单信息
     * @param order
     */
    void insertOrder(Order order) throws Exception;
}
