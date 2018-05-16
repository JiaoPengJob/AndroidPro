package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tch.zx.http.bean.result.LiveDetailsResultBean;
import com.tch.zx.http.model.LiveDetailsModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.LiveDetailsView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页信息
 */

public class LiveDetailsPresenter implements Presenter {

    private Context context;

    private LiveDetailsView liveDetailsView;

    private LiveDetailsModel liveDetailsModel;

    public LiveDetailsPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.liveDetailsModel = new LiveDetailsModel(context);
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
        liveDetailsView = (LiveDetailsView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void liveDetails(String data) {
        Flowable flowable = liveDetailsModel.liveDetails(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<LiveDetailsResultBean, LiveDetailsResultBean>() {
                    @Override
                    public LiveDetailsResultBean apply(LiveDetailsResultBean liveDetailsResultBean) throws Exception {
                        return liveDetailsResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<LiveDetailsResultBean>() {
                    @Override
                    public void onSuccess(LiveDetailsResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            liveDetailsView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        liveDetailsView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
