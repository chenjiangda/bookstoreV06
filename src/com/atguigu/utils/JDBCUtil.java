package com.atguigu.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 包名:com.atguigu.jdbc.utils
 *
 * @author Leevi
 * 日期2021-04-29  15:15
 * 这个工具类中会提供仨方法:
 * 1. 获取连接池对象
 * 2. 从连接池中获取连接
 * 3. 将链接归还到连接池
 */
public class JDBCUtil {
    private static DataSource dataSource;
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    static {
        try {
            //1. 使用类加载器读取配置文件，转成字节输入流
            InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            //2. 使用Properties对象加载字节输入流
            Properties properties = new Properties();
            properties.load(is);
            //3. 使用DruidDataSourceFactory创建连接池对象
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接池对象
     * @return
     */
    public static DataSource getDataSource(){

        return dataSource;
    }

    /**
     * 获取连接，让他在同一个线程中调用的时候拿到的是同一个连接
     * @return
     */
    public static Connection getConnection() {
        try {
            //注意：get不是拿出来，只是指向那个对象
            Connection conn = connectionThreadLocal.get();
            if (conn == null) {
                //说明ThreadLocal中还没有数据
                //那么就从连接池中获取一个连接
                conn = dataSource.getConnection();
                //将这个连接存储到ThreadLocal
                connectionThreadLocal.set(conn);
            }

            //第一次调用会从连接池中拿一个连接并且把这个连接放入ThreadLocal，第二次直接从ThreadLocal中拿到conn(第一次存的那个conn)，同一个线程中从ThreadLocal中获取的连接是一样的，方便做事务管理
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void releaseConnection(){
        try {
            //将连接归还会连接池
            getConnection().close();
            //移除ThreadLocal中的连接
            connectionThreadLocal.remove();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
