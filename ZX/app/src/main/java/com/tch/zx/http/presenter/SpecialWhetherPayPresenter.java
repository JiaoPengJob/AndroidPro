package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;
import com.tch.zx.http.model.SpecialWhetherPayModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.SpecialWhetherPayView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 专栏是否付费
 */

public class SpecialWhetherPayPresenter implements Presenter {

    private Context context;

    private SpecialWhetherPayModel whetherPayModel;

    private SpecialWhetherPayView whetherPayView;

    public SpecialWhetherPayPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.whetherPayModel = new SpecialWhetherPayModel(context);
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
        whetherPayView = (SpecialWhetherPayView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialWhetherPay(String data) {
        Flowable flowable = whetherPayModel.specialWhetherPay(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpecialWhetherPayResultBean, SpecialWhetherPayResultBean>() {
                    @Override
                    public SpecialWhetherPayResultBean apply(SpecialWhetherPayResultBean whetherPayResultBean) throws Exception {
                        return whetherPayResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SpecialWhetherPayResultBean>() {
                    @Override
                    public void onSuccess(SpecialWhetherPayResultBean whetherPayResultBean) {
                        if (whetherPayResultBean != null) {
                            whetherPayView.onSuccess(whetherPayResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        whetherPayView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
