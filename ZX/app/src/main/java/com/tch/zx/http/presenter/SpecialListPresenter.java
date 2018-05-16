package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.SpecialListResultBean;
import com.tch.zx.http.model.SmallListModel;
import com.tch.zx.http.model.SpecialListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.SmallListView;
import com.tch.zx.http.view.SpecialListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 全部专栏
 */

public class SpecialListPresenter implements Presenter {

    private Context context;

    private SpecialListView specialListView;

    private SpecialListModel specialListModel;

    public SpecialListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.specialListModel = new SpecialListModel(context);
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
        specialListView = (SpecialListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void specialList(String data) {
        Flowable flowable = specialListModel.specialList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SpecialListResultBean, SpecialListResultBean>() {
                    @Override
                    public SpecialListResultBean apply(SpecialListResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SpecialListResultBean>() {
                    @Override
                    public void onSuccess(SpecialListResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            specialListView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        specialListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
