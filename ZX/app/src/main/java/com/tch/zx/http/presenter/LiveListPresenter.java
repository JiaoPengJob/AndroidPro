package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.LiveListResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.LiveListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.LiveListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 全部直播
 */

public class LiveListPresenter implements Presenter {

    private Context context;

    private LiveListModel liveListModel;

    private LiveListView liveListView;

    public LiveListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.liveListModel = new LiveListModel(context);
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
        liveListView = (LiveListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void liveList(String data) {
        Flowable flowable = liveListModel.liveList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<LiveListResultBean, LiveListResultBean>() {
                    @Override
                    public LiveListResultBean apply(LiveListResultBean liveListResultBean) throws Exception {
                        return liveListResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<LiveListResultBean>() {
                    @Override
                    public void onSuccess(LiveListResultBean liveListResultBean) {
                        if (liveListResultBean != null) {
                            liveListView.onSuccess(liveListResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        liveListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
