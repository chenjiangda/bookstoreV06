package com.atguigu;

import com.atguigu.utils.JDBCUtil;

import java.sql.Connection;

public class TestThreadLocalDemo {
    public static void main(String[] args) {
        //ThreadLocal是在同一个线程中共享数据的容器

        //他底层维护了一个map，map的key就是当前线程Thread，它的value就是该线程存储的数据

        /*ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("张三");
        threadLocal.set("李四");
        threadLocal.set("王五");


        new Thread(()->{
            threadLocal.set("赵六");
            System.out.println("在新线程中获取："+threadLocal.get());
        }).start();

        System.out.println("在主线程中获取："+threadLocal.get());*/


        //此时工具类中刚修改的getConnection方法。
        Connection conn1 = JDBCUtil.getConnection();
        Connection conn2 = JDBCUtil.getConnection();
        Connection conn3 = JDBCUtil.getConnection();

        System.out.println(conn1);
        System.out.println(conn2);
        System.out.println(conn3);


    }
}
