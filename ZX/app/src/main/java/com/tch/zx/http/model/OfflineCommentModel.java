package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.OfflineCommentResultBean;
import com.tch.zx.http.bean.result.RetResultBean;

import io.reactivex.Flowable;

public class OfflineCommentModel {

    private HttpBaseService offlineCommentService;

    public OfflineCommentModel(Context context) {
        this.offlineCommentService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<OfflineCommentResultBean> offlineComment(String data) {
        return offlineCommentService.offlineComment(data);
    }
}
