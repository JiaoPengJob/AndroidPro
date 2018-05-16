package com.tch.youmuwa.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tch.youmuwa.myinterface.LocationInterface;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定位监听
 */

public class BaiduLocationListener implements BDLocationListener {

    private BaiduMap map;
    private boolean isFirst = true;
    private Context context;
    private LocationInterface li;

    public BaiduLocationListener(Context context, BaiduMap map, LocationInterface li) {
        this.map = map;
        this.context = context;
        this.li = li;
    }

    @Override
    public void onReceiveLocation(final BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        map.setMyLocationData(locData);

        if (isFirst) {
            isFirst = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        String city = location.getCity().substring(0, location.getCity().length() - 1);
        li.getResult(String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude()), city);
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
