package com.atguigu.filter;

import com.atguigu.bean.CommonResult;
import com.atguigu.bean.User;
import com.atguigu.constant.BookStoreConstants;
import com.atguigu.utils.JsonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //1.判断当前是否已登录
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(BookStoreConstants.USERSESSIONKEY);
        if (loginUser == null) {
            /*//ajax发的请求拦截到这里不能用下面方法跳转了，得换方法
            //往request域对象中存储errorMessage
            request.setAttribute("errorMessage","请先登录");

            //2.如果未登录则跳转到登录页面
            request.getRequestDispatcher("/user?method=toLoginPage").forward(request,response);
            //不return的话不结束就会继续往下走走到chain.doFilter(req, resp);就会放行原本应该拦截的请求了*/

            //响应数据给客户端，告诉客户端未登录
            CommonResult commonResult = CommonResult.error().setMessage("unlogin");
            JsonUtils.writeResult(response,commonResult);


            return;
        }
        //3.如果已经登录，则放行
        chain.doFilter(req, resp);
    }
}

