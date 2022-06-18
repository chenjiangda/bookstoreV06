package com.atguigu.servlet.model;

import com.atguigu.servlet.base.ModelBaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Leevi
 * 日期2021-05-14  10:00
 */
public class AdminServlet extends ModelBaseServlet {
    /**
     * 跳转到后台管理界面
     * @param request
     * @param response
     */
    public void toManagerPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processTemplate("manager/manager",request,response);
    }
}
