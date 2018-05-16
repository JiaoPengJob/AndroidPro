package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 主页信息
 */

public class HomeModel {

    private HttpBaseService homeService;

    public HomeModel(Context context) {
        this.homeService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<HomeResultBean> home(String data) {
        return homeService.home(data);
    }
}
