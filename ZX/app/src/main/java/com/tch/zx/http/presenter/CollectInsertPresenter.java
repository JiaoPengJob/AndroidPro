package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectInsertModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectInsertView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消关注
 */

public class CollectInsertPresenter implements Presenter {

    private Context context;

    private CollectInsertModel collectInsertModel;

    private CollectInsertView collectInsertView;

    public CollectInsertPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.collectInsertModel = new CollectInsertModel(context);
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
        collectInsertView = (CollectInsertView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void collectInsert(String data) {
        Flowable flowable = collectInsertModel.collectInsert(data);
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
                            collectInsertView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        collectInsertView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
