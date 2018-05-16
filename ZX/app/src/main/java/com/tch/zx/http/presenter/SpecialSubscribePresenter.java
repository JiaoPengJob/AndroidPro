package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;
import com.tch.zx.http.model.SpecialSubscribeModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.SpecialSubscribeView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 专栏订阅
 */
public class SpecialSubscribePresenter implements Presenter {

    private Context context;

    private SpecialSubscribeModel specialSubscribeModel;

    private SpecialSubscribeView specialSubscribeView;

    public SpecialSubscribePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.specialSubscribeModel = new SpecialSubscribeModel(context);
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
        specialSubscribeView = (SpecialSubscribeView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialSubscribe(String data) {
        Flowable flowable = specialSubscribeModel.specialSubscribe(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpecialSubscribeResultBean, SpecialSubscribeResultBean>() {
                    @Override
                    public SpecialSubscribeResultBean apply(SpecialSubscribeResultBean specialSubscribeResultBean) throws Exception {
                        return specialSubscribeResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SpecialSubscribeResultBean>() {
                    @Override
                    public void onSuccess(SpecialSubscribeResultBean specialSubscribeResultBean) {
                        if (specialSubscribeResultBean != null) {
                            specialSubscribeView.onSuccess(specialSubscribeResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        specialSubscribeView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
