package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.FriendListByAppUserIdResult;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.model.BaseModel;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.FriendListByAppUserIdView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 取消关注
 */

public class FriendListByAppUserIdPresenter implements Presenter {

    private Context context;

    private BaseModel model;

    private FriendListByAppUserIdView view;

    public FriendListByAppUserIdPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.model = new BaseModel(context);
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
        this.view = (FriendListByAppUserIdView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void getFriendListByAppUserId(String data) {
        Flowable flowable = model.getFriendListByAppUserId(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<BaseResultBean<Object>, BaseResultBean<Object>>() {
                    @Override
                    public BaseResultBean<Object> apply(BaseResultBean<Object> result) throws Exception {
                        return result;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<BaseResultBean<Object>>() {
                    @Override
                    public void onSuccess(BaseResultBean<Object> result) {
                        if (result != null) {
                            view.onSuccess(result);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        view.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
