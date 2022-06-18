package com.atguigu.filter;

import com.atguigu.utils.JDBCUtil;

import javax.servlet.*;
import java.io.IOException;

public class CloseConnectionFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            //每个功能都会拦截到这里，然后放心过去，执行完了后没出错就去到finally释放连接
            chain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtil.releaseConnection();
        }
    }
}
