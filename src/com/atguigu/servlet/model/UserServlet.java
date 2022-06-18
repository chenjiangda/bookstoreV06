package com.atguigu.servlet.model;

import com.atguigu.bean.CommonResult;
import com.atguigu.bean.User;
import com.atguigu.constant.BookStoreConstants;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import com.atguigu.servlet.base.ModelBaseServlet;
import com.atguigu.utils.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Leevi
 * 日期2021-05-14  09:07
 */
public class UserServlet extends ModelBaseServlet {
    private UserService userService = new UserServiceImpl();
    /**
     * 跳转到登录页面
     * @param request
     * @param response
     */
    public void toLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processTemplate("user/login",request,response);
    }

    /**
     * 处理登录校验
     * @param request
     * @param response
     * @throws IOException
     */
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //还是做原来的登录校验
        //1. 获取客户端传入的请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();

        //2. 将username和password封装到User对象
        User user = new User();

        //3. 调用业务层的方法处理登录
        try {
            BeanUtils.populate(user,parameterMap);
            //获取登录的用户信息
            User loginUser = userService.doLogin(user);

            //将loginUser对象存储到会话域对象中
            request.getSession().setAttribute(BookStoreConstants.USERSESSIONKEY,loginUser);

            //没有出现异常，说明登录成功，那么跳转到登录成功页面
            processTemplate("user/login_success",request,response);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常表示登录失败，则往域对象中存储登录失败的信息
            request.setAttribute("errorMessage","登录失败,"+e.getMessage());
            //跳转到登录页面，显示登录失败的信息
            processTemplate("user/login",request,response);
        }
    }

    /**
     * 跳转到注册页面
     * @param request
     * @param response
     * @throws IOException
     */
    public void toRegisterPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processTemplate("user/regist",request,response);
    }

    /**
     * 处理注册请求
     * @param request
     * @param response
     * @throws IOException
     */
    public void doRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            //1. 获取请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();

            //获取用户输入的验证码
            //code是我输入的验证码的input的name属性的值
            //parameterMap获取得到的是String数组，这边只有一个验证码所以直接取第一个数据
            String code = parameterMap.get("code")[0];
            //从session总获取服务器生成的验证码
            String checkCode = (String) request.getSession().getAttribute("KAPTCHA_SESSION_KEY");
            //校验验证码:忽略大小写
            if (checkCode.equalsIgnoreCase(code)) {
                //验证码正确,才进行注册
                //2. 使用BeanUtils将parameterMap中的数据封装到User对象
                User user = new User();
                BeanUtils.populate(user,parameterMap);
                //3. 调用业务层的方法处理注册业务
                userService.doRegister(user);

                //没有异常，就是注册成功
                //跳转到注册成功页面
                processTemplate("user/regist_success",request,response);
            }else {
                //验证码错误
                throw new RuntimeException("验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //有异常就注册失败,往域对象中存入失败信息
            request.setAttribute("errorMessage","注册失败,"+e.getMessage());
            //跳转回到注册页面
            processTemplate("user/regist",request,response);
        }

    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @throws IOException
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.立即让本次会话失效
        request.getSession().invalidate();
        //2.跳转到ProtalServlet让ProtalServlet(这个servlet的别名是index.html)查出首页的所有书籍然后转发到首页
        response.sendRedirect(request.getContextPath()+"/index.html");


    }


    /**
     * 校验用户是否已存在
     * @param request
     * @param response
     */
    public void checkUsername(HttpServletRequest request, HttpServletResponse response){
        CommonResult commonResult = null;
        try {
            //1.获取Username
            String username = request.getParameter("username");
            //2.调用业务层的方法，根据username查询user
            userService.findByUserName(username);

            //表示用户名不存在，可以使用(只要返回给前端一个True或者False就行了，因为前端只需要知道这个用户存不存在)
            commonResult = CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            //表示用户名已存在，不能使用
            commonResult = CommonResult.error().setMessage("用户名已存在，请重新输入用户名");
        }

        //将commonResult对象转成json响应给客户端
        JsonUtils.writeResult(response,commonResult);

    }


}
