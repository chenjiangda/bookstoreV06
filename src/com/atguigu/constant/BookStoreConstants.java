package com.atguigu.constant;

public class BookStoreConstants {
    /**
     * 将user存储到session时候的key
     */
    public static final String USERSESSIONKEY = "loginUser";

    /**
     * 将cart存储到session时候的key
     */
    public static final String CARTSESSIONKEY = "cart";


    /**
     * 未发货订单 0
     * 已发货订单 1
     * 已收货订单 2
     *
     */
    public static final Integer UNFILLEDORDER = 0;
    public static final Integer FILLEDORDER = 1;
    public static final Integer RECEIVEDORDER = 2;


}
