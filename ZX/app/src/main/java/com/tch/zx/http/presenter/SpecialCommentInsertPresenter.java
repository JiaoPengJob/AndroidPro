package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.SpecialCommentInsertModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.SpecialCommentInsertView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加专栏评论
 */

public class SpecialCommentInsertPresenter implements Presenter {

    private Context context;

    private SpecialCommentInsertModel specialCommentInsertModel;

    private SpecialCommentInsertView specialCommentInsertView;

    public SpecialCommentInsertPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.specialCommentInsertModel = new SpecialCommentInsertModel(context);
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
        specialCommentInsertView = (SpecialCommentInsertView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialCommentInsert(String data) {
        Flowable flowable = specialCommentInsertModel.specialCommentInsert(data);
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
                            specialCommentInsertView.onSuccess(retResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        specialCommentInsertView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
