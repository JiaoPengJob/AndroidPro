package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 登录
 */

public class LoginModel {

    private HttpBaseService loginService;

    public LoginModel(Context context) {
        this.loginService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<LoginResultBean> login(String data) {
        return loginService.login(data);
    }
}
