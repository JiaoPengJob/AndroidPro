package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;

import io.reactivex.Flowable;

/**
 * 小课详情
 */

public class SmallDetailsModel {

    private HttpBaseService smallDetailsService;

    public SmallDetailsModel(Context context) {
        this.smallDetailsService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SmallDetailsResultBean> smallDetails(String data) {
        return smallDetailsService.smallDetails(data);
    }
}
