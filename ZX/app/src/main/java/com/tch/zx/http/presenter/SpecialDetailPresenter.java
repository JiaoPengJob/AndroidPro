package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SpecialDetailResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.SpecialDetailModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.SpecialDetailView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 专栏详情
 */

public class SpecialDetailPresenter implements Presenter {

    private Context context;

    private SpecialDetailModel specialDetailModel;

    private SpecialDetailView specialDetailView;

    public SpecialDetailPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.specialDetailModel = new SpecialDetailModel(context);
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
        specialDetailView = (SpecialDetailView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialDetail(String data) {
        Flowable flowable = specialDetailModel.specialDetail(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpecialDetailResultBean, SpecialDetailResultBean>() {
                    @Override
                    public SpecialDetailResultBean apply(SpecialDetailResultBean specialDetailResultBean) throws Exception {
                        return specialDetailResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SpecialDetailResultBean>() {
                    @Override
                    public void onSuccess(SpecialDetailResultBean specialDetailResultBean) {
                        if (specialDetailResultBean != null) {
                            specialDetailView.onSuccess(specialDetailResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        specialDetailView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
