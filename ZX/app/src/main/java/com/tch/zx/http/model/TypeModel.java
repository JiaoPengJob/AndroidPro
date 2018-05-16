package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.TypeResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 主页选项卡
 */

public class TypeModel {

    private HttpBaseService typeService;

    public TypeModel(Context context) {
        this.typeService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<TypeResultBean> type(String data) {
        return typeService.type(data);
    }
}
