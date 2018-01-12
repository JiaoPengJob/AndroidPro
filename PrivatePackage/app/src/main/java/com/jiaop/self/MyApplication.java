package com.jiaop.self;

import com.jiaop.self.Entitis.LoggerParameter;
import com.jiaop.self.base.BaseApp;

/**
 * Created by JiaoP on 2018/1/9.
 */

public class MyApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected LoggerParameter loggerParameter() {
        return new LoggerParameter(true
                , 0
                , 3
                , "Test");
    }

}
