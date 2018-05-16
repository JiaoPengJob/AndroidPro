package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class GiveFabulousModel {

    private HttpBaseService giveFabulousService;

    public GiveFabulousModel(Context context) {
        this.giveFabulousService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> giveFabulous(String data) {
        return giveFabulousService.giveFabulous(data);
    }
}
