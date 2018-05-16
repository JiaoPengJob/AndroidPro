package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class CollectInsertModel {

    private HttpBaseService collectInsertService;

    public CollectInsertModel(Context context) {
        this.collectInsertService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> collectInsert(String data) {
        return collectInsertService.collectInsert(data);
    }
}
