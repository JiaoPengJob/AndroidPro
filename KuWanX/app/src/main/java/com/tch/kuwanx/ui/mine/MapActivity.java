package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地图页面
 */
public class MapActivity extends BaseActivity implements AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMyLocationChangeListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.map)
    MapView mMapView;

    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;
    private GeocodeSearch geocoderSearch;
    private String formatAddress = "";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("地图");
        btTitleFeatures.setVisibility(View.VISIBLE);
        btTitleFeatures.setText("确认");
        initMap();
        initLocation();
    }

    /**
     * 显示地图
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //对amap添加单击地图事件监听器
        aMap.setOnMapClickListener(this);
        setMapUi();
//        location();
        //逆地理编码,根据定位的坐标来获取该地点的位置详细信息
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 点击定位
     */
    @OnClick(R.id.ibLocationIcon)
    public void myLocation() {
        if (marker != null) {
            marker.remove();
        }
        initLocation();
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        locationStyle();
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(2000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
                setMapLocation(aMapLocation);
            }
        }
    };

    private void setMapLocation(AMapLocation mapLocation) {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude()), 18, 30, 0));
        aMap.animateCamera(mCameraUpdate);
    }

    /**
     * 设置地图显示UI
     */
    private void setMapUi() {
        //隐藏高德地图Logo
        mMapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup child = (ViewGroup) mMapView.getChildAt(0);//地图框架
                child.getChildAt(2).setVisibility(View.GONE);//logo
            }
        });
        mUiSettings = aMap.getUiSettings();
        //隐藏缩放控件
        mUiSettings.setZoomControlsEnabled(false);
    }

    /**
     * 地图定位蓝点展示
     */
    private void locationStyle() {
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.address_location));
        //设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.strokeColor(Color.parseColor("#00000000"));
        //设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.radiusFillColor(Color.parseColor("#00000000"));
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMyLocationChange(Location location) {
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location.getLongitude()), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 地图点击监听回调
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        getPointInfo(latLng);
    }

    private Marker marker = null;

    /**
     * 反地理编码，获取点击的地址描述信息
     */
    private void getPointInfo(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.draggable(false);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.address_location)));
        marker = aMap.addMarker(markerOption);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int i) {
        if (result != null && result.getRegeocodeAddress() != null
                && result.getRegeocodeAddress().getFormatAddress() != null) {
            formatAddress = result.getRegeocodeAddress().getFormatAddress();
            Logger.wtf("点击的地址信息描述：" + result.getRegeocodeAddress().getFormatAddress());
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int i) {

    }

    /**
     * 确定
     */
    @OnClick(R.id.btTitleFeatures)
    public void confirmAddress() {
        if (formatAddress != null && !formatAddress.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("formatAddress", formatAddress);
            MapActivity.this.setResult(0, intent);
            MapActivity.this.finish();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void mapBack() {
        backActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backActivity() {
        Intent intent = new Intent();
        intent.putExtra("formatAddress", formatAddress);
        MapActivity.this.setResult(0, intent);
        MapActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        destroyLocation();
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
