package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.UploadPicResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.UploadPicModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.UploadPicView;
import com.tch.zx.http.view.View;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 图片上传公共接口
 */

public class UploadPicPresenter implements Presenter {

    private Context context;

    private UploadPicModel uploadPicModel;

    private UploadPicView uploadPicView;

    public UploadPicPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.uploadPicModel = new UploadPicModel(context);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        uploadPicView = (UploadPicView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void uploadPic(String data, Map<String, RequestBody> partList) {
        Flowable flowable = uploadPicModel.uploadPic(data, partList);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<UploadPicResultBean, UploadPicResultBean>() {
                    @Override
                    public UploadPicResultBean apply(UploadPicResultBean uploadPicResultBean) throws Exception {
                        return uploadPicResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<UploadPicResultBean>() {
                    @Override
                    public void onSuccess(UploadPicResultBean uploadPicResultBean) {
                        if (uploadPicResultBean != null) {
                            uploadPicView.onSuccess(uploadPicResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        uploadPicView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
