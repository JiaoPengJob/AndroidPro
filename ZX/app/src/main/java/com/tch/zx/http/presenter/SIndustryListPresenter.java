package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SIndustryListResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.SIndustryListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.SIndustryListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 行业二级分类
 */

public class SIndustryListPresenter implements Presenter {

    private Context context;

    private SIndustryListModel sIndustryListModel;

    private SIndustryListView sIndustryListView;

    public SIndustryListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.sIndustryListModel = new SIndustryListModel(context);
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
        sIndustryListView = (SIndustryListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void sIndustryList(String data) {
        Flowable flowable = sIndustryListModel.sIndustryList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SIndustryListResultBean, SIndustryListResultBean>() {
                    @Override
                    public SIndustryListResultBean apply(SIndustryListResultBean sIndustryListResultBean) throws Exception {
                        return sIndustryListResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SIndustryListResultBean>() {
                    @Override
                    public void onSuccess(SIndustryListResultBean sIndustryListResultBean) {
                        if (sIndustryListResultBean != null) {
                            sIndustryListView.onSuccess(sIndustryListResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        sIndustryListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
