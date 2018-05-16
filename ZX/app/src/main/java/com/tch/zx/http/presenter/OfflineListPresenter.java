package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.OfflineListResultBean;
import com.tch.zx.http.model.OfflineListModel;
import com.tch.zx.http.model.SmallListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.OfflineListView;
import com.tch.zx.http.view.SmallListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 全部线下
 */

public class OfflineListPresenter implements Presenter {

    private Context context;

    private OfflineListView offlineListView;

    private OfflineListModel offlineListModel;

    public OfflineListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.offlineListModel = new OfflineListModel(context);
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
        offlineListView = (OfflineListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void offlineList(String data) {
        Flowable flowable = offlineListModel.offlineList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<OfflineListResultBean, OfflineListResultBean>() {
                    @Override
                    public OfflineListResultBean apply(OfflineListResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<OfflineListResultBean>() {
                    @Override
                    public void onSuccess(OfflineListResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            offlineListView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        offlineListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
