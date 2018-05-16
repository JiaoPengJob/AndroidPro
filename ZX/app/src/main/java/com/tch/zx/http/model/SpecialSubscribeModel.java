package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;

import io.reactivex.Flowable;

public class SpecialSubscribeModel {

    private HttpBaseService specialSubscribeService;

    public SpecialSubscribeModel(Context context) {
        this.specialSubscribeService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SpecialSubscribeResultBean> specialSubscribe(String data) {
        return specialSubscribeService.specialSubscribe(data);
    }
}
