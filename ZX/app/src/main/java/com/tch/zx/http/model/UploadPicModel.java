package com.tch.zx.http.model;

import android.content.Context;

import com.tch.zx.http.HttpBaseService;
import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.UploadPicResultBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadPicModel {

    private HttpBaseService uploadPicService;

    public UploadPicModel(Context context) {
        this.uploadPicService = RetrofitHelper.getInstance(context).getServer();
    }

    public Flowable<UploadPicResultBean> uploadPic(String data, Map<String, RequestBody> partList) {
        return uploadPicService.uploadPic(data, partList);
    }
}
