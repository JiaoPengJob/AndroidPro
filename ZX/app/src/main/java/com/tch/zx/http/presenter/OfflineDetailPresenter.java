package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.OfflineDetailResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.OfflineDetailModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.OfflineDetailView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 线下详情
 */

public class OfflineDetailPresenter implements Presenter {

    private Context context;

    private OfflineDetailModel offlineDetailModel;

    private OfflineDetailView offlineDetailView;

    public OfflineDetailPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.offlineDetailModel = new OfflineDetailModel(context);
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
        offlineDetailView = (OfflineDetailView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void offlineDetail(String data) {
        Flowable flowable = offlineDetailModel.offlineDetail(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<OfflineDetailResultBean, OfflineDetailResultBean>() {
                    @Override
                    public OfflineDetailResultBean apply(OfflineDetailResultBean offlineDetailResultBean) throws Exception {
                        return offlineDetailResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<OfflineDetailResultBean>() {
                    @Override
                    public void onSuccess(OfflineDetailResultBean offlineDetailResultBean) {
                        if (offlineDetailResultBean != null) {
                            offlineDetailView.onSuccess(offlineDetailResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        offlineDetailView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
