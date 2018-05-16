package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.model.LoginModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.LoginView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录
 */

public class LoginPresenter implements Presenter {

    private Context context;

    private LoginView loginView;

    private LoginModel loginModel;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.loginModel = new LoginModel(context);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        loginView = (LoginView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void login(String data) {
        Flowable flowable = loginModel.login(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<LoginResultBean, LoginResultBean>() {
                    @Override
                    public LoginResultBean apply(LoginResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<LoginResultBean>() {
                    @Override
                    public void onSuccess(LoginResultBean loginResultBean) {
                        if (loginResultBean != null) {
                            loginView.onSuccess(loginResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        loginView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
