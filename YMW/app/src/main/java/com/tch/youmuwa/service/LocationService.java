package com.tch.youmuwa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tch.youmuwa.listener.WorkerLocationListener;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.util.SharedPrefsUtil;


/**
 * Created by peng on 2017/8/29.
 */

public class LocationService extends Service {

    private WorkerLocationListener myListener;
    private LocationClient mLocationClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 添加定位
     */
    private void initLocation() {
        myListener = new WorkerLocationListener(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");//必加
        option.setScanSpan(1000 * 10 * 60);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
