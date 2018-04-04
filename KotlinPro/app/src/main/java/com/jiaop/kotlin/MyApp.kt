package com.jiaop.kotlin

import com.jiaop.libs.base.JPApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


/**
 * Created by jiaop on 2018/3/9.
 */
class MyApp : JPApplication() {

    override fun onCreate() {
        super.onCreate()

        //设置Log输出日志
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) 是否显示线程信息. Default true
                .methodCount(0)         // (Optional) 要显示多少种方法行. Default 2
                .methodOffset(3)        // (Optional) 跳过堆栈跟踪中的一些方法调用. Default 5
                .tag("Kotlin:")   // (Optional) 每个日志的自定义标签. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))


    }

}