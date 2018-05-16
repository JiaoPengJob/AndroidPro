package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.model.HomeModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页信息
 */

public class HomePresenter implements Presenter {

    private Context context;

    private HomeView homeView;

    private HomeModel homeModel;

    public HomePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.homeModel = new HomeModel(context);
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
        homeView = (HomeView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void home(String data) {
        Flowable flowable = homeModel.home(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<HomeResultBean, HomeResultBean>() {
                    @Override
                    public HomeResultBean apply(HomeResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<HomeResultBean>() {
                    @Override
                    public void onSuccess(HomeResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            homeView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        homeView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
