package com.tch.zx.http.presenter;

import android.content.Intent;

import com.tch.zx.http.view.View;

/**
 * 基类
 */

public interface Presenter {

    void onCreate();

    void onStart();

    void onStop();

    void pause();

    void attachView(View view);

    void attachIncomingIntent(Intent intent);
}
