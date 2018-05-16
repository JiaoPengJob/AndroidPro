package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.GiveFabulousModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.GiveFabulousView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消关注
 */

public class GiveFabulousPresenter implements Presenter {

    private Context context;

    private GiveFabulousModel giveFabulousModel;

    private GiveFabulousView giveFabulousView;

    public GiveFabulousPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.giveFabulousModel = new GiveFabulousModel(context);
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
        giveFabulousView = (GiveFabulousView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void giveFabulous(String data) {
        Flowable flowable = giveFabulousModel.giveFabulous(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<RetResultBean, RetResultBean>() {
                    @Override
                    public RetResultBean apply(RetResultBean retResultBean) throws Exception {
                        return retResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<RetResultBean>() {
                    @Override
                    public void onSuccess(RetResultBean retResultBean) {
                        if (retResultBean != null) {
                            giveFabulousView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        giveFabulousView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
