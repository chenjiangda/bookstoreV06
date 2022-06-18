package com.atguigu.service.impl;

import com.atguigu.bean.Cart;
import com.atguigu.bean.CartItem;
import com.atguigu.bean.Order;
import com.atguigu.bean.User;
import com.atguigu.constant.BookStoreConstants;
import com.atguigu.dao.BookDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderItemDao;
import com.atguigu.dao.impl.BookDaoImpl;
import com.atguigu.dao.impl.OrderDaoImpl;
import com.atguigu.dao.impl.OrderItemDaoImpl;
import com.atguigu.service.OrderService;
import com.atguigu.utils.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = new OrderDaoImpl();
    private OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();

    @Override
    public String checkout(User user, Cart cart) throws Exception {

        //1.往订单表中插入一条数据
        Order order = new Order();
        //1.1 生成一个唯一的订单好：方式一使用UUID、方式二使用当前毫秒数拼接用户ID。只要保证他唯一就行了
        //这边使用UUID，UUID是java中生成唯一字符串的工具类
        String orderSequence = UUID.randomUUID().toString();
        order.setOrderSequence(orderSequence);

        //1.2创建当前时间的字符串存入到order中
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = dateFormat.format(new Date());
        order.setCreateTime(createTime);

        //1.3设置订单的totalCount,其实就是cart的totalCount
        order.setTotalCount(cart.getTotalCount());

        //1.4设置订单的totalAmount,其实就是cart的totalAmount
        order.setTotalAmount(cart.getTotalAmount());

        //1.5设置订单状态：0表示已支付未发货、1表示已发货、2表示已收货
        order.setOrderStatus(BookStoreConstants.UNFILLEDORDER);

        //1.6设置订单的用户id
        order.setUserId(user.getUserId());

        //1.7调用持久层的方法保存order
        orderDao.insertOrder(order);

        //2.往订单详情表中插入多条订单详情（用批处理）

        //获取购物车中所有购物项所在的map
        Map<Integer, CartItem> cartItemMap = cart.getCartItemMap();
        //Map的key是购物项的id，所有value就是购物项
        Collection<CartItem> cartItemCollection = cartItemMap.values();
        //将Collection封装成List集合，集合中存储的就是所有购物项


        List<CartItem> cartItemList = new ArrayList<>(cartItemCollection);


        //用二维数组封装批处理需要的数据:第一维表示要执行的数据条数吗，二维表示sql语句的参数个数。
        //要执行的数据条数，就是要添加的订单项的个数，就是购物车中购物项的个数
        //有多少个购物项就有多少个订单项
        Object[][] insertOrderItemParamArr = new Object[cartItemList.size()][6];

        //3.批量修改书的库存和销量
        //封装批量修改书的库存和销量的参数
        Object[][] updateBookParamArr = new Object[cartItemList.size()][3];

        //注意（回忆）：一个订单项对应一个购物项，一个购物项对应一本书，一个购物车对应多个购物项
        for (int i = 0; i < cartItemList.size(); i++) {
            //获取到遍历出来的购物项
            CartItem cartItem = cartItemList.get(i);

            //封装批量添加订单项的参数（订单项里的很多信息其实就是cartItem拿过来的）
            //封装sql语句中的第一个参数book_name的值，就是cartItem的bookName
            insertOrderItemParamArr[i][0] = cartItem.getBookName();
            //就是cartItem的price
            insertOrderItemParamArr[i][1] = cartItem.getPrice();
            //就是cartItem的imgPath
            insertOrderItemParamArr[i][2] = cartItem.getImgPath();
            //就是cartItem的count
            insertOrderItemParamArr[i][3] = cartItem.getCount();
            //就是cartItem的amount
            insertOrderItemParamArr[i][4] = cartItem.getAmount();
            //orderId就是订单的id,就是第一步获取的自增长的主键值
            insertOrderItemParamArr[i][5] = order.getOrderId();

            //封装批量修改t_book的库存和销量的参数
            //要增加的销量，其实就是购物项的count
            updateBookParamArr[i][0] = cartItem.getCount();
            //要减少的库存，其实也是购物项的count
            updateBookParamArr[i][1] = cartItem.getCount();
            //要修改的数的id，其实就是购物项的bookId
            updateBookParamArr[i][2] = cartItem.getBookId();
        }

        //调用持久层的方法，进行批量添加订单项
        orderItemDao.insertOrderItemArr(insertOrderItemParamArr);

        //调用持久层的方法，进行批量修改书的销量和库存
        bookDao.updateBookArr(updateBookParamArr);

        //4.返回订单的序列号
        return orderSequence;

    }
}
