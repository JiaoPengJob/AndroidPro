package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SearchInfoResultBean;

import io.reactivex.Flowable;

public class SearchInfoModel {

    private HttpBaseService searchInfoService;

    public SearchInfoModel(Context context) {
        this.searchInfoService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SearchInfoResultBean> searchInfo(String data) {
        return searchInfoService.searchInfo(data);
    }
}
