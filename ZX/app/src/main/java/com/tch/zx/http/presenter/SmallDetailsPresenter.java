package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;
import com.tch.zx.http.model.HomeModel;
import com.tch.zx.http.model.SmallDetailsModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.http.view.SmallDetailsView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 小课详情
 */

public class SmallDetailsPresenter implements Presenter {

    private Context context;

    private SmallDetailsView smallDetailsView;

    private SmallDetailsModel smallDetailsModel;

    public SmallDetailsPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.smallDetailsModel = new SmallDetailsModel(context);
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
        smallDetailsView = (SmallDetailsView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void smallDetails(String data) {
        Flowable flowable = smallDetailsModel.smallDetails(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SmallDetailsResultBean, SmallDetailsResultBean>() {
                    @Override
                    public SmallDetailsResultBean apply(SmallDetailsResultBean smallDetailsResultBean) throws Exception {
                        return smallDetailsResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SmallDetailsResultBean>() {
                    @Override
                    public void onSuccess(SmallDetailsResultBean smallDetailsResultBean) {
                        if (smallDetailsResultBean != null) {
                            smallDetailsView.onSuccess(smallDetailsResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        smallDetailsView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
