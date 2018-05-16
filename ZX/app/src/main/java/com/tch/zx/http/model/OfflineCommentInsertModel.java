package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class OfflineCommentInsertModel {

    private HttpBaseService offlineCommentInsertService;

    public OfflineCommentInsertModel(Context context) {
        this.offlineCommentInsertService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<RetResultBean> offlineCommentInsert(String data) {
        return offlineCommentInsertService.offlineCommentInsert(data);
    }
}
