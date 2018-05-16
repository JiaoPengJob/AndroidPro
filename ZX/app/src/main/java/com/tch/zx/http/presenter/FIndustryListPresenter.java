package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.FIndustryListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.FIndustryListResultBean;
import com.tch.zx.http.view.FIndustryListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 行业一级分类
 */

public class FIndustryListPresenter implements Presenter {

    private Context context;

    private FIndustryListModel fIndustryListModel;

    private FIndustryListView fIndustryListView;

    public FIndustryListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.fIndustryListModel = new FIndustryListModel(context);
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
        fIndustryListView = (FIndustryListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void fIndustryList(String data) {
        Flowable flowable = fIndustryListModel.fIndustryList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<FIndustryListResultBean, FIndustryListResultBean>() {
                    @Override
                    public FIndustryListResultBean apply(FIndustryListResultBean fIndustryListResultBean) throws Exception {
                        return fIndustryListResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<FIndustryListResultBean>() {
                    @Override
                    public void onSuccess(FIndustryListResultBean fIndustryListResultBean) {
                        if (fIndustryListResultBean != null) {
                            fIndustryListView.onSuccess(fIndustryListResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        fIndustryListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
