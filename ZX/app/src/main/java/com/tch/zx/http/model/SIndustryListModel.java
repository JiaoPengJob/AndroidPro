package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SIndustryListResultBean;

import io.reactivex.Flowable;

public class SIndustryListModel {

    private HttpBaseService sIndustryListService;

    public SIndustryListModel(Context context) {
        this.sIndustryListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SIndustryListResultBean> sIndustryList(String data) {
        return sIndustryListService.sIndustryList(data);
    }
}
