package com.atguigu.servlet.model;

import com.atguigu.bean.Cart;
import com.atguigu.bean.User;
import com.atguigu.constant.BookStoreConstants;
import com.atguigu.service.OrderService;
import com.atguigu.service.impl.OrderServiceImpl;
import com.atguigu.servlet.base.ModelBaseServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "OrderClientServlet", value = "/protected/orderClient")
public class OrderClientServlet extends ModelBaseServlet {

    private OrderService orderService = new OrderServiceImpl();

    /**
     * 结算
     * @param request
     * @param response
     */
    public void checkout(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
            //1. 从session中获取购物车信息
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute(BookStoreConstants.CARTSESSIONKEY);
            //2. 从session中获取用户信息
            User loginUser = (User) session.getAttribute(BookStoreConstants.USERSESSIONKEY);
            //3. 调用业务层的方法进行订单结算，获取订单号
            String orderSequence = orderService.checkout(loginUser, cart);
            //4. 清空购物车
            session.removeAttribute(BookStoreConstants.CARTSESSIONKEY);
            //5. 将订单号存储到请求域中,然后跳转到checkout.html
            request.setAttribute("orderSequence",orderSequence);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            processTemplate("cart/checkout",request,response);
        }

    }

}
