package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.OfflineCommentResultBean;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.OfflineCommentModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.OfflineCommentView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 线下评论列表
 */

public class OfflineCommentPresenter implements Presenter {

    private Context context;

    private OfflineCommentModel offlineCommentModel;

    private OfflineCommentView offlineCommentView;

    public OfflineCommentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.offlineCommentModel = new OfflineCommentModel(context);
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
        offlineCommentView = (OfflineCommentView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void offlineComment(String data) {
        Flowable flowable = offlineCommentModel.offlineComment(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<OfflineCommentResultBean, OfflineCommentResultBean>() {
                    @Override
                    public OfflineCommentResultBean apply(OfflineCommentResultBean offlineCommentResultBean) throws Exception {
                        return offlineCommentResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<OfflineCommentResultBean>() {
                    @Override
                    public void onSuccess(OfflineCommentResultBean offlineCommentResultBean) {
                        if (offlineCommentResultBean != null) {
                            offlineCommentView.onSuccess(offlineCommentResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        offlineCommentView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
