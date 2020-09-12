package com.gbdpcloud.config;

/**
 * @author lihaifeng
 * @version 1.0
 * @Description 数据源上下文
 * @date 2020/6/7 18:37
 */
public class DataSourceContext {
    //用于存放多线程环境下的成员变量
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String value) {
        contextHolder.set(value);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
