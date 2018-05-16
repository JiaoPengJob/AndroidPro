package com.tch.youmuwa.http.presenter;

import android.content.Intent;

import com.tch.youmuwa.http.view.ClientBaseView;

/**
 * 主导器
 */

public interface IPresenter<T> {

    void onCreate();

    void onStart();

    void onStop();

    void pause();

    void attachView(ClientBaseView<T> view);

    void attachIncomingIntent(Intent intent);
}
