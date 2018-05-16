package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class SpecialCommentInsertModel {

    private HttpBaseService specialCommentInsertService;

    public SpecialCommentInsertModel(Context context) {
        this.specialCommentInsertService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> specialCommentInsert(String data) {
        return specialCommentInsertService.specialCommentInsert(data);
    }
}
