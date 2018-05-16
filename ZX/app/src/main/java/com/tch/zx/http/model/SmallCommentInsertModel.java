package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.SmallCommentInsertResultBean;

import io.reactivex.Flowable;

public class SmallCommentInsertModel {

    private HttpBaseService smallCommentInsertService;

    public SmallCommentInsertModel(Context context) {
        this.smallCommentInsertService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<SmallCommentInsertResultBean> smallCommentInsert(String data) {
        return smallCommentInsertService.smallCommentInsert(data);
    }
}
