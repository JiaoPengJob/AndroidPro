package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.OrderListResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 已购
 */

public class OrderListModel {

    private HttpBaseService orderListService;

    public OrderListModel(Context context) {
        this.orderListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<OrderListResultBean> orderList(String data) {
        return orderListService.orderList(data);
    }
}
