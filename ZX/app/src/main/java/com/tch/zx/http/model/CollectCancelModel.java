package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class CollectCancelModel {

    private HttpBaseService collectCancelService;

    public CollectCancelModel(Context context) {
        this.collectCancelService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> collectCancel(String data) {
        return collectCancelService.collectCancel(data);
    }
}
