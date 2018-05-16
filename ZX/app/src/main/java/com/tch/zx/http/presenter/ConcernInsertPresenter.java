package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.ConcernInsertModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.ConcernInsertView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 关注
 */

public class ConcernInsertPresenter implements Presenter {

    private Context context;

    private ConcernInsertModel concernInsertModel;

    private ConcernInsertView concernInsertView;

    public ConcernInsertPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.concernInsertModel = new ConcernInsertModel(context);
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
        concernInsertView = (ConcernInsertView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void concernInsert(String data) {
        Flowable flowable = concernInsertModel.concernInsert(data);
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
                            concernInsertView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        concernInsertView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
