package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.LiveDetailsResultBean;

import io.reactivex.Flowable;

/**
 * 直播详情
 */

public class LiveDetailsModel {

    private HttpBaseService liveDetailsService;

    public LiveDetailsModel(Context context) {
        this.liveDetailsService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<LiveDetailsResultBean> liveDetails(String data) {
        return liveDetailsService.liveDetails(data);
    }
}
