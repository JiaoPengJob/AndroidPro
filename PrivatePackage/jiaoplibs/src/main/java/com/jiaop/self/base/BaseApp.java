package com.jiaop.self.base;

import android.app.Application;

import com.jiaop.self.Entitis.LoggerParameter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import es.dmoral.toasty.Toasty;

/**
 * Created by JiaoP
 * Application基类
 */

public abstract class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initLog();
    }

    /**
     * 设置Log输出日志
     */
    private void initLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(loggerParameter().isShowThreadInfo())       // 是否显示线程信息
                .methodCount(loggerParameter().getMethodCount())            // 要显示多少种方法行
                .methodOffset(loggerParameter().getMethodOffset())          // 跳过堆栈跟踪中的一些方法调用
                .tag(loggerParameter().getTag())                            // 每个日志的自定义标签
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    protected abstract LoggerParameter loggerParameter();

}
