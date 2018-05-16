package com.tch.zx.http.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SearchInfoResultBean;
import com.tch.zx.http.model.CollectCancelModel;
import com.tch.zx.http.model.SearchInfoModel;
import com.tch.zx.http.subscriber.BaseSubscriberCallBack;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.SearchInfoView;
import com.tch.zx.http.view.View;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索
 */

public class SearchInfoPresenter implements Presenter {

    private Context context;

    private SearchInfoModel searchInfoModel;

    private SearchInfoView searchInfoView;

    public SearchInfoPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.searchInfoModel = new SearchInfoModel(context);
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
        searchInfoView = (SearchInfoView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void searchInfo(String data) {
        Flowable flowable = searchInfoModel.searchInfo(data);
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<SearchInfoResultBean, SearchInfoResultBean>() {
                    @Override
                    public SearchInfoResultBean apply(SearchInfoResultBean searchInfoResultBean) throws Exception {
                        return searchInfoResultBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberCallBack<SearchInfoResultBean>() {
                    @Override
                    public void onSuccess(SearchInfoResultBean searchInfoResultBean) {
                        if (searchInfoResultBean != null) {
                            searchInfoView.onSuccess(searchInfoResultBean);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        searchInfoView.onError(t.toString().substring(t.toString().indexOf(":") + 1));
                    }
                });
    }
}
