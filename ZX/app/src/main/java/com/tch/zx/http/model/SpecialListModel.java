package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.SpecialListResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 全部专栏
 */

public class SpecialListModel {

    private HttpBaseService specialListService;

    public SpecialListModel(Context context) {
        this.specialListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SpecialListResultBean> specialList(String data) {
        return specialListService.specialList(data);
    }
}
