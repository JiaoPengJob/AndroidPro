package com.tch.youmuwa.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.CityPoiResult;
import com.tch.youmuwa.bean.result.MarkResult;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.bean.result.WorkerTypeResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.listener.BaiduLocationListener;
import com.tch.youmuwa.myinterface.LocationInterface;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.service.MessageService;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.employer.SearchActivity;
import com.tch.youmuwa.ui.activity.employer.SwitchCityActivity;
import com.tch.youmuwa.ui.activity.employer.WorkerInfoActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */

public class MapFragment extends Fragment implements OnGetGeoCoderResultListener, CloudListener, LocationInterface, MessageInterface {

    @BindView(R.id.rvWorkerTypesTab)
    RecyclerView rvWorkerTypesTab;
    //    @BindView(R.id.mapView)
    TextureMapView mapView;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvMsgNumber)
    TextView tvMsgNumber;

    private CommonAdapter adapter;
    private int nPosition = 0;
    private BaiduMap baiduMap;
    private LocationClient mLocationClient = null;
    private BaiduLocationListener myListener;
    private Intent intent;
    private GeoCoder mSearch;
    private PresenterImpl<Object> presenter;
    private String type = "0";
    private List<PoiInfo> poiInfos;
    private String cityName = "";
    private List<CloudPoiInfo> poiList;
    private List<MarkResult> marks;
    private boolean isBig = false;
    private ImageView ivMarkPhoto;
    private TextView tvMarkInfo;
    private List<MsgResult.MsgListBean> msgList;
    private String locationInfo = null;
    private Timer timer;
    private TimerTask timerTask;
    private MsgResult msgR;
    private float mapState = 18f;
    private boolean ifFirst = true;
    private View view;
    private CloudManager cloudManager;

    public static MapFragment newInstance(String param) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("size", param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    /**
     * 点击进行定位
     */
    @OnClick(R.id.ivLocation)
    public void locationMap() {
        initLocation();
        handler.sendEmptyMessage(2);
    }

    /**
     * 初始化数据
     */
    private void initView() {
        mapView = (TextureMapView) view.findViewById(R.id.mapView);
        marks = new ArrayList<MarkResult>();
        poiList = new ArrayList<CloudPoiInfo>();
        msgList = new ArrayList<MsgResult.MsgListBean>();
        clientGetworkertype();
        MessageService.setMi(this);
//        CloudManager.getInstance().init(this);
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts(mapView);
        }
        initMap();
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(2);
//            }
//        };
//        timer.schedule(timerTask, 30000);
    }

    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    public void showContacts(View v) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestContactsPermissions(v);
        } else {
            Log.e("TAG", "Contact permissions have already been granted. Displaying contact details.");
        }
    }

    private void requestContactsPermissions(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(view, "permission_contacts_rationale",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_CONTACT, 1000);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_CONTACT, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            Log.e("TAG", "权限设置成功");
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void getMsg(MsgResult msg) {
        if (msg != null) {
            msgR = msg;
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        handler.sendEmptyMessage(2);
        handler.sendEmptyMessage(4);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (baiduMap != null) {
            baiduMap.setMyLocationEnabled(false);
        }

        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
            baiduMap = null;
        }
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (cloudManager != null) {
            cloudManager.destroy();
            cloudManager = null;
        }
        super.onDestroy();
    }

    /**
     * 加载工种
     */
    private void setTypes(final List<WorkerTypeResult.DataBean> types) {
        types.add(0, new WorkerTypeResult.DataBean(0, 0, "全部"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWorkerTypesTab.setLayoutManager(linearLayoutManager);
        if (getContext() != null) {
            adapter = new CommonAdapter<WorkerTypeResult.DataBean>(getContext(), R.layout.item_map_worker_type, types) {
                @Override
                protected void convert(ViewHolder viewHolder, WorkerTypeResult.DataBean s, int position) {
                    viewHolder.setText(R.id.tvTypeTab, s.getName());
                    if (nPosition == position) {
                        viewHolder.setTextColor(R.id.tvTypeTab, Color.parseColor("#FFFFFF"));
                        viewHolder.setBackgroundRes(R.id.tvTypeTab, R.drawable.oval_guide_button);
                    } else {
                        viewHolder.setTextColor(R.id.tvTypeTab, Color.parseColor("#444444"));
                        viewHolder.setBackgroundColor(R.id.tvTypeTab, Color.parseColor("#FFFFFF"));
                    }
                }
            };
        }
        if (adapter != null) {
            rvWorkerTypesTab.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    nPosition = position;
                    type = String.valueOf(types.get(position).getId());
                    baiduMap.clear();
                    handler.sendEmptyMessage(2);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    /**
     * 获取工种
     */
    private void clientGetworkertype() {
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getworkertype();
        presenter.attachView(workerTypeView);
    }

    private ClientBaseView<Object> workerTypeView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                WorkerTypeResult type = (WorkerTypeResult) GsonUtil.parseJson(baseBean, WorkerTypeResult.class);
                if (type != null) {
                    setTypes(type.getData());
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerTypeView==" + result);
        }
    };

    private void initMap() {
        // 隐藏百度的LOGO
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏)
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.clear();
        //普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.getUiSettings().setCompassEnabled(false);

        initLocation();

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                if (mapStatus.zoom <= 15) {
                    isBig = false;
                    addMarks();
                } else {
                    isBig = true;
                    addMarks();
                }
//                if (ifFirst) {
//                    handler.sendEmptyMessage(2);
//                }
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                intent = new Intent(getActivity(), WorkerInfoActivity.class);
                intent.putExtra("workerId", marker.getExtraInfo().getInt("workerId"));
                intent.putExtra("isSpecify", marker.getExtraInfo().getString("worker_type_name"));
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 添加图标
     */
    private void addMarks() {
        baiduMap.clear();
        if (marks != null && marks.size() > 0) {
            for (MarkResult markResult : marks) {
                BitmapDescriptor markerBitmap = null;
                View popMarker = View.inflate(getActivity(), R.layout.map_item_mark, null);
                tvMarkInfo = (TextView) popMarker.findViewById(R.id.tvMarkInfo);
                tvMarkInfo.setText(markResult.getTitle() + " | " + markResult.getWorker_type_name());
                if (isBig) {
                    tvMarkInfo.setVisibility(View.VISIBLE);
                } else {
                    tvMarkInfo.setVisibility(View.GONE);
                }
                markerBitmap = BitmapDescriptorFactory.fromView(popMarker);
                //定义Maker坐标点
                LatLng point = new LatLng(markResult.getLatitude(), markResult.getLongitude());
                Bundle bundle = new Bundle();
                bundle.putInt("workerId", Integer.parseInt(markResult.getWorker_id()));
                bundle.putString("isSpecify", markResult.getWorker_type_name());
                //构建MarkerOption，用于在地图上添加Marker
                MarkerOptions option = new MarkerOptions()
                        .position(point)
                        .icon(markerBitmap)
                        .extraInfo(bundle)
                        .zIndex(19);
                //在地图上添加Marker，并显示
                Marker marker = (Marker) baiduMap.addOverlay(option);
                marker.setToTop();
//                marker.setVisible(true);
                markerBitmap.recycle();
            }
        }
        if (cloudManager != null) {
            cloudManager.destroy();
            cloudManager = null;
        }
    }

    /**
     * 添加定位
     */
    private void initLocation() {
        myListener = new BaiduLocationListener(getContext(), baiduMap, this);
        baiduMap.setMyLocationEnabled(true);
        //加载自定义marker
        View popMarker = View.inflate(getActivity(), R.layout.map_location_mark, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(popMarker);
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor));
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(0);
        option.setCoorType("bd09ll");//必加
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void locationCity(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude,
                longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(15f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        handler.sendEmptyMessage(2);
    }

    /**
     * 选择城市
     */
    @OnClick(R.id.llSwitchCity)
    public void switchCity() {
        intent = new Intent(getActivity(), SwitchCityActivity.class);
        intent.putExtra("city", tvCityName.getText().toString());
        startActivityForResult(intent, 10);
    }

    /**
     * 搜索
     */
    @OnClick(R.id.ivSearch)
    public void search() {
        intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("city", tvCityName.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                cityName = data.getStringExtra("cityName");
                tvCityName.setText(cityName);
                poiCity();
                handler.sendEmptyMessage(2);
                break;
            default:
                break;
        }
    }

    /**
     * 获取并定位到所选城市
     */
    private void poiCity() {
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.citytopoi(cityName);
        presenter.attachView(cityPoiView);
    }

    private ClientBaseView<Object> cityPoiView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                CityPoiResult cp = (CityPoiResult) GsonUtil.parseJson(baseBean.getData(), CityPoiResult.class);
                if (mLocationClient.isStarted()) {
                    mLocationClient.stop();
                }
                locationCity(Double.parseDouble(cp.getLatitude()), Double.parseDouble(cp.getLongitude()));
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "cityPoiView" + result);
        }
    };

    private String cityStr;

    @Override
    public void getResult(final String result, String city) {
        cityStr = city;
        handler.sendEmptyMessage(5);

        if (result != null && !result.equals("")) {
            Message message = new Message();
            message.what = 0;
            message.obj = result;
            handler.sendMessage(message);
        }

//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        timer.schedule(timerTask, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    locationInfo = msg.obj.toString();
                    handler.sendEmptyMessage(2);
//                    timer.cancel();
                    handler.removeMessages(0);
                    break;
                case 1:
                    if (msgR != null) {
                        msgList = new ArrayList<MsgResult.MsgListBean>();
                        for (MsgResult.MsgListBean mb : msgR.getMsg_list()) {
                            if (mb.getIs_read() == 0) {
                                msgList.add(mb);
                            }
                        }
                        if (msgList.size() <= 0) {
                            tvMsgNumber.setVisibility(View.GONE);
                        } else {
                            tvMsgNumber.setVisibility(View.VISIBLE);
                            tvMsgNumber.setText(String.valueOf(msgList.size()));
                        }
                    }
                    break;
                case 2:
                    lbsSearch();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    addMarks();
                    break;
                case 4:
                    getMsgs();
                    break;
                case 5:
                    tvCityName.setText(cityStr);
                    break;
            }

        }
    };

    /**
     * LBS云检索
     */
    private void lbsSearch() {
        if (cloudManager == null) {
            cloudManager = CloudManager.getInstance();
            cloudManager.init(this);
        }

        NearbySearchInfo info = new NearbySearchInfo();
        //access_key（必须），最大长度50
        info.ak = "IbAfhXeHpd46LkBk8RIZ5TUCtWV4kXT9";
        //geo table 表主键（必须）
        info.geoTableId = 174799;
        if (!type.equals("0")) {
            info.filter = "worker_type_name:" + type;
        }
        info.radius = 50000;
        info.pageIndex = 0;
        info.pageSize = 50;
        info.location = locationInfo;
        /**
         * localSearch(LocalSearchInfo info)
         * 区域检索，如果所有参数都合法，返回true，否则返回 fasle，
         * 检索的结果在 CloudListener 中的 onGetSearchResult() 函数中。
         * */
        cloudManager.nearbySearch(info);
    }

    /**
     * void onGetSearchResult(CloudSearchResult result,int error)
     * 当详情检索完成时回调此函数
     */
    @Override
    public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
        if (cloudSearchResult != null) {
            poiList = cloudSearchResult.poiList;
            Log.e("TAG", "检索的数量===" + poiList.size());
            if (poiList != null) {
                handler.removeMessages(2);
                marks = new ArrayList<MarkResult>();
                for (CloudPoiInfo info : poiList) {
                    String type;
                    switch (info.extras.get("worker_type_name").toString()) {
                        case "1":
                            type = "油工";
                            break;
                        case "2":
                            type = "木工";
                            break;
                        case "3":
                            type = "泥瓦工";
                            break;
                        case "4":
                            type = "水电工";
                            break;
                        case "5":
                            type = "工长";
                            break;
                        case "6":
                            type = "保洁工";
                            break;
                        case "7":
                            type = "小工";
                            break;
                        case "8":
                            type = "其他";
                            break;
                        default:
                            type = "";
                            break;
                    }
                    marks.add(new MarkResult(info.title,
                            info.longitude,
                            info.latitude,
                            type,
                            info.extras.get("work_state").toString(),
                            info.extras.get("head_image_path").toString(),
                            info.extras.get("worker_id").toString()
                    ));
                    if (getContext() != null) {
                        handler.sendEmptyMessage(3);
                    }
//                    CloudManager.getInstance().destroy();
                }
            } else {
                marks = new ArrayList<MarkResult>();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            marks = new ArrayList<MarkResult>();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
        Log.e("TAG", "onGetCloudRgcResult=====Error===" + i);
        if (cloudRgcResult != null) {
            Log.e("TAG", "onGetCloudRgcResult===" + cloudRgcResult.status);
        }
    }

    /**
     * DetailSearchResult:详细信息检索结果类
     * 字段：  poiInfo 详细信息结果数据
     */
    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
        Log.e("TAG", "onGetDetailSearchResult=======Error===" + i);
        if (detailSearchResult != null) {
            if (detailSearchResult.poiInfo != null) {
                Log.e("TAG", "onGetCloudRgcResult===" + detailSearchResult.poiInfo.title);
            } else {
                Log.e("TAG", "onGetCloudRgcResult===" + detailSearchResult.status);
            }
        }
    }

    /**
     * 对选中的城市进行检索
     */
    private void geoAddress(String city) {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.geocode(new GeoCodeOption().city(city).address(""));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.color.transparent)));
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.color.transparent)));
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
    }

    /**
     * 消息
     */
    @OnClick({R.id.ivMsgNumber, R.id.tvMsgNumber})
    public void msgShow() {
        intent = new Intent(getActivity(), MessageCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 获取消息列表
     */
    private void getMsgs() {
//        Intent intent = new Intent(getContext(), MessageService.class);
//        getContext().startService(intent);
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getmessagelist();
        presenter.attachView(msgView);
    }

    private ClientBaseView<Object> msgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                MsgResult msg = (MsgResult) GsonUtil.parseJson(baseBean.getData(), MsgResult.class);
                if (msg.getMsg_list().size() <= 0) {
                    tvMsgNumber.setVisibility(View.GONE);
                } else {
                    msgList = new ArrayList<MsgResult.MsgListBean>();
                    for (MsgResult.MsgListBean mb : msg.getMsg_list()) {
                        if (mb.getIs_read() == 0) {
                            msgList.add(mb);
                        }
                    }
                    if (msgList.size() <= 0) {
                        tvMsgNumber.setVisibility(View.GONE);
                    } else {
                        tvMsgNumber.setVisibility(View.VISIBLE);
                        tvMsgNumber.setText(String.valueOf(msgList.size()));
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "msgView--" + result);
        }
    };
}
