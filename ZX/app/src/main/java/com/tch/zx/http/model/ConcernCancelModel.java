package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class ConcernCancelModel {

    private HttpBaseService concernCancelService;

    public ConcernCancelModel(Context context) {
        this.concernCancelService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> concernCancel(String data) {
        return concernCancelService.concernCancel(data);
    }
}
