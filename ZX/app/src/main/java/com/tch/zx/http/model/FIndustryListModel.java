package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.view.FIndustryListResultBean;

import io.reactivex.Flowable;

public class FIndustryListModel {

    private HttpBaseService fIndustryListService;

    public FIndustryListModel(Context context) {
        this.fIndustryListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<FIndustryListResultBean> fIndustryList(String data) {
        return fIndustryListService.fIndustryList(data);
    }
}
