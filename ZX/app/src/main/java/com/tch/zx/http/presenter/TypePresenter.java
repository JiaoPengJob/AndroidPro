package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;

import com.tch.zx.http.RetrofitHelper;
import com.tch.zx.http.bean.result.TypeResultBean;
import com.tch.zx.http.model.TypeModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.TypeView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录
 */

public class TypePresenter implements Presenter {

    private Context context;

    private TypeView typeView;

    private TypeModel typeModel;

    public TypePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.typeModel = new TypeModel(context);
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
        typeView = (TypeView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void type(String data) {
        Flowable flowable = typeModel.type(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<TypeResultBean, TypeResultBean>() {
                    @Override
                    public TypeResultBean apply(TypeResultBean baseResultBean) throws Exception {
                        return baseResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<TypeResultBean>() {
                    @Override
                    public void onSuccess(TypeResultBean baseResultBean) {
                        if (baseResultBean != null) {
                            typeView.onSuccess(baseResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        typeView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
