package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallCommentResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;
import com.tch.zx.http.model.HomeModel;
import com.tch.zx.http.model.SmallCommentModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.http.view.SmallCommentView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SmallCommentPresenter implements Presenter {

    private Context context;

    private SmallCommentView smallCommentView;

    private SmallCommentModel smallCommentModel;

    public SmallCommentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.smallCommentModel = new SmallCommentModel(context);
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
        smallCommentView = (SmallCommentView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void smallComment(String data) {
        Flowable flowable = smallCommentModel.smallComment(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SmallCommentResultBean, SmallCommentResultBean>() {
                    @Override
                    public SmallCommentResultBean apply(SmallCommentResultBean smallCommentResultBean) throws Exception {
                        return smallCommentResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SmallCommentResultBean>() {
                    @Override
                    public void onSuccess(SmallCommentResultBean smallCommentResultBean) {
                        if (smallCommentResultBean != null) {
                            smallCommentView.onSuccess(smallCommentResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        smallCommentView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
