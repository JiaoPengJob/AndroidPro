package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.LiveListResultBean;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class LiveListModel {

    private HttpBaseService liveListService;

    public LiveListModel(Context context) {
        this.liveListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<LiveListResultBean> liveList(String data) {
        return liveListService.liveList(data);
    }
}
