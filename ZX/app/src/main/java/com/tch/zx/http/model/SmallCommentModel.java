package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallCommentResultBean;

import io.reactivex.Flowable;

public class SmallCommentModel {

    private HttpBaseService smallCommentService;

    public SmallCommentModel(Context context) {
        this.smallCommentService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SmallCommentResultBean> smallComment(String data) {
        return smallCommentService.smallComment(data);
    }
}
