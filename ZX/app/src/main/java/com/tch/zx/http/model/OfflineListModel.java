package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.OfflineListResultBean;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 全部线下
 */

public class OfflineListModel {

    private HttpBaseService offlineListService;

    public OfflineListModel(Context context) {
        this.offlineListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<OfflineListResultBean> offlineList(String data) {
        return offlineListService.offlineList(data);
    }
}
