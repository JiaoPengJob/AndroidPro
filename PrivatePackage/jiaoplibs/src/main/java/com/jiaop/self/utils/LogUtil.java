package com.jiaop.self.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by JiaoP
 * Log日志输出帮助类
 */

public class LogUtil {

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void clearLogAdapters() {
        Logger.clearLogAdapters();
    }

}
