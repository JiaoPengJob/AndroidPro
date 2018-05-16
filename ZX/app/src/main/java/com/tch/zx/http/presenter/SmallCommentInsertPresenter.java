package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallCommentInsertResultBean;
import com.tch.zx.http.model.HomeModel;
import com.tch.zx.http.model.SmallCommentInsertModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.http.view.SmallCommentInsertView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 添加小课评论
 */

public class SmallCommentInsertPresenter implements Presenter {

    private Context context;

    private SmallCommentInsertView smallCommentInsertView;

    private SmallCommentInsertModel smallCommentInsertModel;

    public SmallCommentInsertPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.smallCommentInsertModel = new SmallCommentInsertModel(context);
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
        smallCommentInsertView = (SmallCommentInsertView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void smallCommentInsert(String data) {
        Flowable flowable = smallCommentInsertModel.smallCommentInsert(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SmallCommentInsertResultBean, SmallCommentInsertResultBean>() {
                    @Override
                    public SmallCommentInsertResultBean apply(SmallCommentInsertResultBean smallCommentInsertResultBean) throws Exception {
                        return smallCommentInsertResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SmallCommentInsertResultBean>() {
                    @Override
                    public void onSuccess(SmallCommentInsertResultBean smallCommentInsertResultBean) {
                        if (smallCommentInsertResultBean != null) {
                            smallCommentInsertView.onSuccess(smallCommentInsertResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        smallCommentInsertView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
