package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;

import io.reactivex.Flowable;

public class SpecialWhetherPayModel {

    private HttpBaseService specialWhetherPayService;

    public SpecialWhetherPayModel(Context context) {
        this.specialWhetherPayService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SpecialWhetherPayResultBean> specialWhetherPay(String data) {
        return specialWhetherPayService.specialWhetherPay(data);
    }
}
