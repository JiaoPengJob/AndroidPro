package com.tch.youmuwa.listener;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tch.youmuwa.bean.parameters.PoiParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.util.SharedPrefsUtil;

/**
 * 定位监听
 */

public class WorkerLocationListener implements BDLocationListener {

    private PresenterImpl<Object> presenter;
    private Context context;

    public WorkerLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (!SharedPrefsUtil.getValue(context, "isEmployer", true)) {
            updateLocation(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
        }
    }

    /**
     * 更新位置信息
     */
    private void updateLocation(String longitude, String latitude) {
        PoiParam poiParam = new PoiParam(
                longitude,
                latitude
        );
        presenter = new PresenterImpl<Object>(context);
        presenter.onCreate();
        presenter.updatepoi(poiParam);
        presenter.attachView(poiView);
    }

    private ClientBaseView<Object> poiView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            Log.e("TAG", "poiView==" + baseBean.getMsg().toString());
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "poiView==" + result);
        }
    };

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
