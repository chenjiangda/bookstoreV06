package com.atguigu.dao;

public interface OrderItemDao {
    /**
     * 批量添加订单项
     * @param insertOrderItemParamArr
     */
    void insertOrderItemArr(Object[][] insertOrderItemParamArr) throws Exception;
}
