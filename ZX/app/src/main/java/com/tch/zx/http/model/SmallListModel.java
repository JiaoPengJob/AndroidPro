package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.HttpBaseService;

import io.reactivex.Flowable;

/**
 * 全部小课
 */

public class SmallListModel {

    private HttpBaseService smallListService;

    public SmallListModel(Context context) {
        this.smallListService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SmallListBean> smallList(String data) {
        return smallListService.smallList(data);
    }
}
