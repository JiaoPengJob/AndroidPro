package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SpecialDetailResultBean;

import io.reactivex.Flowable;

public class SpecialDetailModel {

    private HttpBaseService specialDetailService;

    public SpecialDetailModel(Context context) {
        this.specialDetailService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SpecialDetailResultBean> specialDetail(String data) {
        return specialDetailService.specialDetail(data);
    }
}
