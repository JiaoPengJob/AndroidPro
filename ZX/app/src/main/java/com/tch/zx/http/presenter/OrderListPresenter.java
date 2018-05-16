package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.OrderListResultBean;
import com.tch.zx.http.model.HomeModel;
import com.tch.zx.http.model.OrderListModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.HomeView;
import com.tch.zx.http.view.OrderListView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 已购
 */

public class OrderListPresenter implements Presenter {

    private Context context;

    private OrderListView orderListView;

    private OrderListModel orderListModel;

    public OrderListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.orderListModel = new OrderListModel(context);
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
        orderListView = (OrderListView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void orderList(String data) {
        Flowable flowable = orderListModel.orderList(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<OrderListResultBean, OrderListResultBean>() {
                    @Override
                    public OrderListResultBean apply(OrderListResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<OrderListResultBean>() {
                    @Override
                    public void onSuccess(OrderListResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            orderListView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        orderListView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
