package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SpecialCommentResultBean;

import io.reactivex.Flowable;

public class SpecialCommentModel {

    private HttpBaseService specialCommentService;

    public SpecialCommentModel(Context context) {
        this.specialCommentService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SpecialCommentResultBean> specialComment(String data) {
        return specialCommentService.specialComment(data);
    }
}
