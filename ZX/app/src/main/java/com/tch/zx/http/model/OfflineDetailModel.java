package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.OfflineDetailResultBean;

import io.reactivex.Flowable;

public class OfflineDetailModel {

    private HttpBaseService offlineDetailService;

    public OfflineDetailModel(Context context) {
        this.offlineDetailService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<OfflineDetailResultBean> offlineDetail(String data) {
        return offlineDetailService.offlineDetail(data);
    }
}
