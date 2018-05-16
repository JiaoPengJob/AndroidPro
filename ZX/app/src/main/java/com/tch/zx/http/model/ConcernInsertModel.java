package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class ConcernInsertModel {

    private HttpBaseService concernInsertService;

    public ConcernInsertModel(Context context) {
        this.concernInsertService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> concernInsert(String data) {
        return concernInsertService.concernInsert(data);
    }
}
