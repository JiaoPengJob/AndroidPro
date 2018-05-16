package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.ConcernCancelModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.ConcernCancelView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消关注
 */

public class ConcernCancelPresenter implements Presenter {

    private Context context;

    private ConcernCancelModel concernCancelModel;

    private ConcernCancelView concernCancelView;

    public ConcernCancelPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.concernCancelModel = new ConcernCancelModel(context);
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
        concernCancelView = (ConcernCancelView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void concernCancel(String data) {
        Flowable flowable = concernCancelModel.concernCancel(data);
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
                            concernCancelView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        concernCancelView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
