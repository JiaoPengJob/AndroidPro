package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.model.SmallListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.SmallListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 全部小课
 */

public class SmallListPresenter implements Presenter {

    private Context context;

    private SmallListView smallListView;

    private SmallListModel smallListModel;

    public SmallListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.smallListModel = new SmallListModel(context);
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
        smallListView = (SmallListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void smallList(String data) {
        Flowable flowable = smallListModel.smallList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SmallListBean, SmallListBean>() {
                    @Override
                    public SmallListBean apply(SmallListBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SmallListBean>() {
                    @Override
                    public void onSuccess(SmallListBean baseResultBean) {
                        if (baseResultBean != null) {
                            smallListView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        smallListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
