package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SpecialCommentResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.SpecialCommentModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.SpecialCommentView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 专栏评论列表
 */

public class SpecialCommentPresenter implements Presenter {

    private Context context;

    private SpecialCommentModel specialCommentModel;

    private SpecialCommentView specialCommentView;

    public SpecialCommentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.specialCommentModel = new SpecialCommentModel(context);
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
        specialCommentView = (SpecialCommentView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialComment(String data) {
        Flowable flowable = specialCommentModel.specialComment(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpecialCommentResultBean, SpecialCommentResultBean>() {
                    @Override
                    public SpecialCommentResultBean apply(SpecialCommentResultBean specialCommentResultBean) throws Exception {
                        return specialCommentResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SpecialCommentResultBean>() {
                    @Override
                    public void onSuccess(SpecialCommentResultBean specialCommentResultBean) {
                        if (specialCommentResultBean != null) {
                            specialCommentView.onSuccess(specialCommentResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        specialCommentView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
