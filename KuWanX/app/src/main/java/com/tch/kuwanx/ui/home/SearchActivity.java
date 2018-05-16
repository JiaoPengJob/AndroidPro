package com.tch.kuwanx.ui.home;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.SearchAreaCity;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.CountyResult;
import com.tch.kuwanx.result.ProvinceResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.tvSearchModeShow)
    TextView tvSearchModeShow;
    @BindView(R.id.llSearchSelMenu)
    LinearLayout llSearchSelMenu;
    @BindView(R.id.tvSearchSelSortingShow)
    TextView tvSearchSelSortingShow;
    @BindView(R.id.tvSearchSelGameShow)
    TextView tvSearchSelGameShow;
    @BindView(R.id.tvSearchAreaShow)
    TextView tvSearchAreaShow;
    @BindView(R.id.etSearchInput)
    EditText etSearchInput;
    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;
    @BindView(R.id.llBlank)
    LinearLayout llBlank;
    @BindView(R.id.llSearchParent)
    LinearLayout llSearchParent;
    @BindView(R.id.viewSearchDark)
    View viewSearchDark;

    private CommonAdapter searchAdapter;
    private int switchType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        switchType = getIntent().getIntExtra("switchType", 0);
        switch (switchType) {
            case 0:
            case 1:
                tvSearchSelGameShow.setText("PS4");
                break;
            case 2:
                tvSearchSelGameShow.setText("X-Box");
                break;
            case 3:
                tvSearchSelGameShow.setText("Switch");
                break;
        }
        initSearchClick();
        initSearchData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 设置搜索事件
     */
    private void initSearchClick() {
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    Logger.wtf("开始搜索");
                }
                return false;
            }
        });
    }

    private int changeZan = 2;
    private Intent intent;

    private void initSearchData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add("");
        }

        if (list != null && list.size() == 0) {
            rvSearch.setVisibility(View.GONE);
            llBlank.setVisibility(View.VISIBLE);
        } else {
            llBlank.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);
        }

        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(searchAdapter = new CommonAdapter<String>(this, R.layout.item_home_change, list) {
            @Override
            protected void convert(final ViewHolder holder, String item, int position) {
                //头像
                Glide.with(SearchActivity.this)
                        .load("http://imgtu.5011.net/uploads/content/20170323/2876161490257692.jpg")
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivChangeShow));
                holder.setText(R.id.tvChangeName, "姓名");
                holder.setText(R.id.tvChangeDate, "时间");
                holder.setText(R.id.tvChangeLocation, "地点");
                RecyclerView rvHomeChangeImgs = (RecyclerView) holder.getView(R.id.rvHomeChangeImgs);
                initHomeChangeImgs(rvHomeChangeImgs);
                holder.setText(R.id.tvHomeChangeContent, "内容");
                holder.setText(R.id.tvChangeNewPrice, "66.66");
                holder.setText(R.id.tvChangeNeed, "需求|...");
                //赞
                holder.setOnClickListener(R.id.ivChangeZan, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (changeZan == 1) {
                            holder.setImageResource(R.id.ivChangeZan, R.mipmap.item_zan_unsel);
                            holder.setTextColor(R.id.tvChangeZan, Color.parseColor("#7D7B7B"));
                            changeZan = 2;
                        } else {
                            holder.setImageResource(R.id.ivChangeZan, R.mipmap.item_zan_sel);
                            holder.setTextColor(R.id.tvChangeZan, Color.parseColor("#FFDA44"));
                            changeZan = 1;
                        }
                    }
                });
                //评论
                holder.setOnClickListener(R.id.ivChangeComment, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(SearchActivity.this, ReplacementDetailsActivity.class);
                        intent.putExtra("input", true);
                        startActivity(intent);
                    }
                });
                //分享
                holder.setOnClickListener(R.id.ivChangeShare, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showShare();
                    }
                });
            }
        });
        searchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(SearchActivity.this, ReplacementDetailsActivity.class);
                intent.putExtra("input", false);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private LinearLayout llFriends, llWeChat, llQQ, llQzone;
    private Button btMemberShare;
    private CustomPopWindow sharePop;

    /**
     * 显示分享菜单栏
     */
    private void showShare() {
        View view = LayoutInflater.from(SearchActivity.this).
                inflate(R.layout.pop_share, null);
        llFriends = (LinearLayout) view.findViewById(R.id.llFriends);
        llWeChat = (LinearLayout) view.findViewById(R.id.llWeChat);
        llQQ = (LinearLayout) view.findViewById(R.id.llQQ);
        llQzone = (LinearLayout) view.findViewById(R.id.llQzone);
        btMemberShare = (Button) view.findViewById(R.id.btMemberShare);
        btMemberShare.setOnClickListener(new ShareClick());
        llFriends.setOnClickListener(new ShareClick());
        llWeChat.setOnClickListener(new ShareClick());
        llQQ.setOnClickListener(new ShareClick());
        llQzone.setOnClickListener(new ShareClick());

        sharePop = new CustomPopWindow.PopupWindowBuilder(SearchActivity.this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewSearchDark.setVisibility(View.GONE);
                    }
                })
                .setAnimationStyle(R.style.pop_anim)
                .create()
                .showAtLocation(llSearchParent, Gravity.BOTTOM, 0, 0);
        viewSearchDark.setVisibility(View.VISIBLE);
    }

    private class ShareClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (sharePop != null) {
                sharePop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.llFriends:
                    new ShareAction(SearchActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llWeChat:
                    new ShareAction(SearchActivity.this)
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQQ:
                    new ShareAction(SearchActivity.this)
                            .setPlatform(SHARE_MEDIA.QQ)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
                case R.id.llQzone:
                    new ShareAction(SearchActivity.this)
                            .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                            .withText("hello")//分享内容
                            .setCallback(shareListener)//回调监听器
                            .share();
                    break;
            }
        }
    }

    private UMShareListener shareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toasty.warning(SearchActivity.this, "分享成功！", Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toasty.warning(SearchActivity.this, "分享失败：" + t.getMessage(), Toast.LENGTH_SHORT, false).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toasty.warning(SearchActivity.this, "取消分享！", Toast.LENGTH_SHORT, false).show();
        }
    };

    /**
     * 加载显示的图片
     *
     * @param view
     */
    private void initHomeChangeImgs(RecyclerView view) {
        final ArrayList<String> itemImgs = new ArrayList<String>();
        CommonAdapter homeChangeImgsAdapter;
        for (int i = 0; i < 3; i++) {
            itemImgs.add("http://img2.imgtn.bdimg.com/it/u=4215897016,3222246668&fm=27&gp=0.jpg");
        }
        view.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
        view.setAdapter(homeChangeImgsAdapter = new CommonAdapter<String>(SearchActivity.this, R.layout.img, itemImgs) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        Utils.getScreenWidth(SearchActivity.this) / 3,
                        Utils.getScreenWidth(SearchActivity.this) / 3
                );
                lp.setMargins(5, 5, 5, 5);
                ImageView ivPhoto = (ImageView) holder.getView(R.id.ivPhoto);
                ivPhoto.setLayoutParams(lp);
                Glide.with(SearchActivity.this)
                        .load(item)
                        .into(ivPhoto);
            }
        });
        homeChangeImgsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGallery(itemImgs, position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 展示大图画廊
     */
    private void showGallery(ArrayList<String> list, int position) {
        Album.gallery(this)
                .requestCode(100) // 请求码，会在listener中返回。
                .checkedList(list) // 要浏览的图片列表：ArrayList<String>。
                .currentPosition(position)
                .navigationAlpha(250) // Android5.0+的虚拟导航栏的透明度。
                .checkable(false) // 是否有浏览时的选择功能。
                .widget(
                        Widget.newDarkBuilder(SearchActivity.this)
                                .title("预览")
                                .build()
                )
                .start();
    }

    private TextView tvAreaLocationInfo;
    private ImageView ivAreaLocationRefresh;
    private RecyclerView rvAreaCity;
    private RecyclerView rvAreaCounty;
    private CustomPopWindow mAreaPop;
    private RotateAnimation animation;

    /**
     * 区域
     */
    @OnClick(R.id.llSearchSelArea)
    public void searchSelArea() {
        initLocation();
        View view = LayoutInflater.from(this).inflate(R.layout.pop_area, null);
        tvAreaLocationInfo = (TextView) view.findViewById(R.id.tvAreaLocationInfo);
        ivAreaLocationRefresh = (ImageView) view.findViewById(R.id.ivAreaLocationRefresh);
        rvAreaCity = (RecyclerView) view.findViewById(R.id.rvAreaCity);
        rvAreaCounty = (RecyclerView) view.findViewById(R.id.rvAreaCounty);
        tvAreaLocationInfo.setText(cityLocation);
        refreshAnimation();
        loadAreaCityData();
        provinceListHttp();
        ivAreaLocationRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animation != null) {
                    ivAreaLocationRefresh.startAnimation(animation);
                    initLocation();
                }
            }
        });
        mAreaPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewSearchDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(llSearchSelMenu, 0, 5);
        viewSearchDark.setVisibility(View.VISIBLE);
    }

    private CommonAdapter areaCityAdapter;
    private List<SearchAreaCity> list = new ArrayList<>();

    private void loadAreaCityData() {
        rvAreaCity.setLayoutManager(new LinearLayoutManager(this));
        rvAreaCity.setAdapter(areaCityAdapter = new CommonAdapter<SearchAreaCity>(this, R.layout.item_search_area_city, list) {
            @Override
            protected void convert(ViewHolder holder, SearchAreaCity item, int position) {
                if (item.isSel()) {
                    holder.setTextColor(R.id.tvAreaCityInfo, Color.parseColor("#F8C804"));
                    holder.getConvertView().setBackgroundColor(Color.parseColor("#F2F2F2"));
                } else {
                    holder.setTextColor(R.id.tvAreaCityInfo, Color.parseColor("#919191"));
                    holder.getConvertView().setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                holder.setText(R.id.tvAreaCityInfo, ((ProvinceResult.ResultBean) item.getItem()).getDname());
            }
        });
        areaCityAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (SearchAreaCity sac : list) {
                    sac.setSel(false);
                }
                list.get(position).setSel(true);
                areaCityAdapter.notifyDataSetChanged();
                loadCountyData();
                cityListHttp(((ProvinceResult.ResultBean) list.get(position).getItem()).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String cityLocation = "未定位";

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
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
                if (ivAreaLocationRefresh != null) {
                    ivAreaLocationRefresh.clearAnimation();
                }
                if (tvAreaLocationInfo != null) {
                    tvAreaLocationInfo.setText(aMapLocation.getCity());
                    locationClient.stopLocation();
                }
            }
        }
    };

    private CommonAdapter areaCountyAdapter;

    private void loadCountyData() {
        rvAreaCounty.setLayoutManager(new LinearLayoutManager(this));
        rvAreaCounty.setAdapter(areaCountyAdapter = new CommonAdapter<String>(this,
                R.layout.item_search_area_county, new ArrayList<String>()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.tvAreaCountyInfo, item);
            }
        });
        areaCountyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mAreaPop != null) {
                    mAreaPop.dissmiss();
                }
                tvSearchAreaShow.setText(areaCountyAdapter.getDatas().get(position).toString());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void refreshAnimation() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(-1);
        animation.setFillAfter(false);
        animation.setStartOffset(1);
    }

    /**
     * 模式
     */
    @OnClick(R.id.llSearchSelMode)
    public void searchSelMode() {
        showModePopWindow();
    }

    private TextView tvModelPopExIn;
    private TextView tvModelPopExOut;
    private CustomPopWindow mModePop;

    private void showModePopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_model, null);
        tvModelPopExIn = (TextView) view.findViewById(R.id.tvModelPopExIn);
        tvModelPopExOut = (TextView) view.findViewById(R.id.tvModelPopExOut);
        if (tvSearchModeShow.getText().toString().equals("模式") || tvSearchModeShow.getText().toString().equals("换回")) {
            tvModelPopExIn.setTextColor(Color.parseColor("#F8C804"));
            tvModelPopExOut.setTextColor(Color.parseColor("#919191"));
        } else if (tvSearchModeShow.getText().toString().equals("不换回")) {
            tvModelPopExIn.setTextColor(Color.parseColor("#919191"));
            tvModelPopExOut.setTextColor(Color.parseColor("#F8C804"));
        }
        tvModelPopExIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvModelPopExIn.setTextColor(Color.parseColor("#F8C804"));
                tvModelPopExOut.setTextColor(Color.parseColor("#919191"));
                tvSearchModeShow.setText("换回");
                if (mModePop != null) {
                    mModePop.dissmiss();
                }
            }
        });
        tvModelPopExOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvModelPopExIn.setTextColor(Color.parseColor("#919191"));
                tvModelPopExOut.setTextColor(Color.parseColor("#F8C804"));
                tvSearchModeShow.setText("不换回");
                if (mModePop != null) {
                    mModePop.dissmiss();
                }
            }
        });
        mModePop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewSearchDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(llSearchSelMenu, 0, 5);
        viewSearchDark.setVisibility(View.VISIBLE);
    }

    /**
     * 游戏选择
     */
    @OnClick(R.id.llSearchSelGame)
    public void searchSelGame() {
        showGamePopWindow();
    }

    private TextView tvGamePs;
    private TextView tvGameXBox;
    private TextView tvGameSwitch;
    private CustomPopWindow mGamePop;

    private void showGamePopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_game, null);
        tvGamePs = (TextView) view.findViewById(R.id.tvGamePs);
        tvGameXBox = (TextView) view.findViewById(R.id.tvGameXBox);
        tvGameSwitch = (TextView) view.findViewById(R.id.tvGameSwitch);
        if (tvSearchSelGameShow.getText().toString().equals("PS4")) {
            gamePsColor();
        } else if (tvSearchSelGameShow.getText().toString().equals("X-Box")) {
            gameXBoxColor();
        } else if (tvSearchSelGameShow.getText().toString().equals("Switch")) {
            gameSwitchColor();
        }
        tvGamePs.setOnClickListener(new GameClick());
        tvGameXBox.setOnClickListener(new GameClick());
        tvGameSwitch.setOnClickListener(new GameClick());
        mGamePop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewSearchDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(llSearchSelMenu, 0, 5);
        viewSearchDark.setVisibility(View.VISIBLE);
    }

    private class GameClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mGamePop != null) {
                mGamePop.dissmiss();
            }
            tvSearchSelGameShow.setText(((TextView) view).getText().toString());
            switch (view.getId()) {
                case R.id.tvGamePs:
                    gamePsColor();
                    break;
                case R.id.tvGameXBox:
                    gameXBoxColor();
                    break;
                case R.id.tvGameSwitch:
                    gameSwitchColor();
                    break;
            }
        }
    }

    private void gamePsColor() {
        tvGamePs.setTextColor(Color.parseColor("#F8C804"));
        tvGameXBox.setTextColor(Color.parseColor("#919191"));
        tvGameSwitch.setTextColor(Color.parseColor("#919191"));
    }

    private void gameXBoxColor() {
        tvGamePs.setTextColor(Color.parseColor("#919191"));
        tvGameXBox.setTextColor(Color.parseColor("#F8C804"));
        tvGameSwitch.setTextColor(Color.parseColor("#919191"));
    }

    private void gameSwitchColor() {
        tvGamePs.setTextColor(Color.parseColor("#919191"));
        tvGameXBox.setTextColor(Color.parseColor("#919191"));
        tvGameSwitch.setTextColor(Color.parseColor("#F8C804"));
    }

    /**
     * 排序（默认）
     */
    @OnClick(R.id.llSearchSelSorting)
    public void searchSelSorting() {
        showSortingPopWindow();
    }

    private TextView tvSortingPopDefault;
    private TextView tvSortingPopLatestRelease;
    private TextView tvSortingPopHighestDeposit;
    private TextView tvSortingPopMinimumDeposit;
    private TextView tvSortingPopFromRecently;
    private CustomPopWindow mSortingPop;

    private void showSortingPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_sorting, null);
        tvSortingPopDefault = (TextView) view.findViewById(R.id.tvSortingPopDefault);
        tvSortingPopLatestRelease = (TextView) view.findViewById(R.id.tvSortingPopLatestRelease);
        tvSortingPopHighestDeposit = (TextView) view.findViewById(R.id.tvSortingPopHighestDeposit);
        tvSortingPopMinimumDeposit = (TextView) view.findViewById(R.id.tvSortingPopMinimumDeposit);
        tvSortingPopFromRecently = (TextView) view.findViewById(R.id.tvSortingPopFromRecently);
        if (tvSearchSelSortingShow.getText().toString().equals("默认")) {
            sortingPopDefaultColor();
        } else if (tvSearchSelSortingShow.getText().toString().equals("最新")) {
            sortingPopLatestReleaseColor();
        } else if (tvSearchSelSortingShow.getText().toString().equals("最高")) {
            sortingPopHighestDepositColor();
        } else if (tvSearchSelSortingShow.getText().toString().equals("最低")) {
            sortingPopMinimumDeposit();
        } else if (tvSearchSelSortingShow.getText().toString().equals("最近")) {
            sortingPopFromRecently();
        }
        tvSortingPopDefault.setOnClickListener(new SortingClick());
        tvSortingPopLatestRelease.setOnClickListener(new SortingClick());
        tvSortingPopHighestDeposit.setOnClickListener(new SortingClick());
        tvSortingPopMinimumDeposit.setOnClickListener(new SortingClick());
        tvSortingPopFromRecently.setOnClickListener(new SortingClick());
        mSortingPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        viewSearchDark.setVisibility(View.GONE);
                    }
                })
                .create()
                .showAsDropDown(llSearchSelMenu, 0, 5);
        viewSearchDark.setVisibility(View.VISIBLE);
    }

    private class SortingClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mSortingPop != null) {
                mSortingPop.dissmiss();
            }
            switch (view.getId()) {
                case R.id.tvSortingPopDefault:
                    tvSearchSelSortingShow.setText("默认");
                    sortingPopDefaultColor();
                    break;
                case R.id.tvSortingPopLatestRelease:
                    tvSearchSelSortingShow.setText("最新");
                    sortingPopLatestReleaseColor();
                    break;
                case R.id.tvSortingPopHighestDeposit:
                    tvSearchSelSortingShow.setText("最高");
                    sortingPopHighestDepositColor();
                    break;
                case R.id.tvSortingPopMinimumDeposit:
                    tvSearchSelSortingShow.setText("最低");
                    sortingPopMinimumDeposit();
                    break;
                case R.id.tvSortingPopFromRecently:
                    tvSearchSelSortingShow.setText("最近");
                    sortingPopFromRecently();
                    break;
            }
        }
    }

    private void sortingPopDefaultColor() {
        tvSortingPopDefault.setTextColor(Color.parseColor("#F8C804"));
        tvSortingPopLatestRelease.setTextColor(Color.parseColor("#919191"));
        tvSortingPopHighestDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopMinimumDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopFromRecently.setTextColor(Color.parseColor("#919191"));
    }

    private void sortingPopLatestReleaseColor() {
        tvSortingPopDefault.setTextColor(Color.parseColor("#919191"));
        tvSortingPopLatestRelease.setTextColor(Color.parseColor("#F8C804"));
        tvSortingPopHighestDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopMinimumDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopFromRecently.setTextColor(Color.parseColor("#919191"));
    }

    private void sortingPopHighestDepositColor() {
        tvSortingPopDefault.setTextColor(Color.parseColor("#919191"));
        tvSortingPopLatestRelease.setTextColor(Color.parseColor("#919191"));
        tvSortingPopHighestDeposit.setTextColor(Color.parseColor("#F8C804"));
        tvSortingPopMinimumDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopFromRecently.setTextColor(Color.parseColor("#919191"));
    }

    private void sortingPopMinimumDeposit() {
        tvSortingPopDefault.setTextColor(Color.parseColor("#919191"));
        tvSortingPopLatestRelease.setTextColor(Color.parseColor("#919191"));
        tvSortingPopHighestDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopMinimumDeposit.setTextColor(Color.parseColor("#F8C804"));
        tvSortingPopFromRecently.setTextColor(Color.parseColor("#919191"));
    }

    private void sortingPopFromRecently() {
        tvSortingPopDefault.setTextColor(Color.parseColor("#919191"));
        tvSortingPopLatestRelease.setTextColor(Color.parseColor("#919191"));
        tvSortingPopHighestDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopMinimumDeposit.setTextColor(Color.parseColor("#919191"));
        tvSortingPopFromRecently.setTextColor(Color.parseColor("#F8C804"));
    }

    private TextView tvFilterCity, tvFilterFaceCross, tvFilterFlashing,
            tvFilterOne, tvFilterHundred, tvFilterTwoHundred, tvFilterFiveHundred,
            tvFilterThousand, tvFilterTwoThousand, tvFilterFiveThousand, tvFilterMillion;
    private EditText etFilterLowestPrice, etFilterHighestPrice;
    private Button btFilterReset, btFilterConfirm;
    private View viewFilterLowestPrice, viewFilterHighestPrice;
    private CustomPopWindow mFilterPop;
    private int filterType = 0, least = 0, lowestPrice = 0, highestPrice = 0;

    /**
     * 筛选
     */
    @OnClick(R.id.llSearchSelFilter)
    public void searchSelFilter() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_filter, null);
        tvFilterCity = (TextView) view.findViewById(R.id.tvFilterCity);
        tvFilterFaceCross = (TextView) view.findViewById(R.id.tvFilterFaceCross);
        tvFilterFlashing = (TextView) view.findViewById(R.id.tvFilterFlashing);
        tvFilterOne = (TextView) view.findViewById(R.id.tvFilterOne);
        tvFilterHundred = (TextView) view.findViewById(R.id.tvFilterHundred);
        tvFilterTwoHundred = (TextView) view.findViewById(R.id.tvFilterTwoHundred);
        tvFilterFiveHundred = (TextView) view.findViewById(R.id.tvFilterFiveHundred);
        tvFilterThousand = (TextView) view.findViewById(R.id.tvFilterThousand);
        tvFilterTwoThousand = (TextView) view.findViewById(R.id.tvFilterTwoThousand);
        tvFilterFiveThousand = (TextView) view.findViewById(R.id.tvFilterFiveThousand);
        tvFilterMillion = (TextView) view.findViewById(R.id.tvFilterMillion);
        etFilterLowestPrice = (EditText) view.findViewById(R.id.etFilterLowestPrice);
        etFilterHighestPrice = (EditText) view.findViewById(R.id.etFilterHighestPrice);
        btFilterReset = (Button) view.findViewById(R.id.btFilterReset);
        btFilterConfirm = (Button) view.findViewById(R.id.btFilterConfirm);
        viewFilterLowestPrice = view.findViewById(R.id.viewFilterLowestPrice);
        viewFilterHighestPrice = view.findViewById(R.id.viewFilterHighestPrice);

        tvFilterCity.setOnClickListener(new FilterClick());
        tvFilterFaceCross.setOnClickListener(new FilterClick());
        tvFilterFlashing.setOnClickListener(new FilterClick());
        tvFilterOne.setOnClickListener(new FilterClick());
        tvFilterHundred.setOnClickListener(new FilterClick());
        tvFilterTwoHundred.setOnClickListener(new FilterClick());
        tvFilterFiveHundred.setOnClickListener(new FilterClick());
        tvFilterThousand.setOnClickListener(new FilterClick());
        tvFilterTwoThousand.setOnClickListener(new FilterClick());
        tvFilterFiveThousand.setOnClickListener(new FilterClick());
        tvFilterMillion.setOnClickListener(new FilterClick());
        btFilterReset.setOnClickListener(new FilterClick());
        btFilterConfirm.setOnClickListener(new FilterClick());
        viewFilterLowestPrice.setOnClickListener(new FilterClick());
        viewFilterHighestPrice.setOnClickListener(new FilterClick());

        if (filterType == 0) {
            filterCitySel();
        } else if (filterType == 1) {
            filterFaceCrossSel();
        } else if (filterType == 2) {
            filterFlashingSel();
        }

        if (lowestPrice != 0) {
            etFilterLowestPrice.setText(String.valueOf(lowestPrice));
        } else if (highestPrice != 0) {
            etFilterHighestPrice.setText(String.valueOf(highestPrice));
        } else {
            if (least == 1) {
                filterOneSel();
            } else if (least == 100) {
                filterHundredSel();
            } else if (least == 200) {
                filterTwoHundredSel();
            } else if (least == 500) {
                filterFiveHundredSel();
            } else if (least == 1000) {
                filterThousandSel();
            } else if (least == 2000) {
                filterTwoThousandSel();
            } else if (least == 5000) {
                filterFiveThousandSel();
            } else if (least == 10000) {
                filterMillionSel();
            }
        }

        mFilterPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableOutsideTouchableDissmiss(true)
                .setFocusable(true)
                .create()
                .showAsDropDown(llSearchSelMenu, 0, 5);
    }

    /**
     * 重置
     */
    public void filterReset() {
        least = 0;
        filterType = 0;
        lowestPrice = 0;
        highestPrice = 0;
        clearStyle();
    }

    private void clearStyle() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));

        clearBtnStyle();

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void clearBtnStyle() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));
    }

    /**
     * 确认
     */
    public void filterConfirm() {
        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            lowestPrice = Integer.parseInt(etFilterLowestPrice.getText().toString());
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            highestPrice = Integer.parseInt(etFilterHighestPrice.getText().toString());
        }
        mFilterPop.dissmiss();
    }

    private class FilterClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (Utils.isSHowKeyboard(SearchActivity.this, mFilterPop.getPopupWindow().getContentView())) {
                Utils.hindKeyBoard(SearchActivity.this, mFilterPop.getPopupWindow().getContentView());
            }
            switch (view.getId()) {
                case R.id.tvFilterCity:
                    filterType = 0;
                    filterCitySel();
                    break;
                case R.id.tvFilterFaceCross:
                    filterType = 1;
                    filterFaceCrossSel();
                    break;
                case R.id.tvFilterFlashing:
                    filterType = 2;
                    filterFlashingSel();
                    break;
                case R.id.tvFilterOne:
                    least = 1;
                    filterOneSel();
                    break;
                case R.id.tvFilterHundred:
                    least = 100;
                    filterHundredSel();
                    break;
                case R.id.tvFilterTwoHundred:
                    least = 200;
                    filterTwoHundredSel();
                    break;
                case R.id.tvFilterFiveHundred:
                    least = 500;
                    filterFiveHundredSel();
                    break;
                case R.id.tvFilterThousand:
                    least = 1000;
                    filterThousandSel();
                    break;
                case R.id.tvFilterTwoThousand:
                    least = 2000;
                    filterTwoThousandSel();
                    break;
                case R.id.tvFilterFiveThousand:
                    least = 5000;
                    filterFiveThousandSel();
                    break;
                case R.id.tvFilterMillion:
                    least = 10000;
                    filterMillionSel();
                    break;
                case R.id.btFilterReset:
                    filterReset();
                    break;
                case R.id.btFilterConfirm:
                    filterConfirm();
                    break;
                case R.id.viewFilterLowestPrice:
                    least = 0;
                    etFilterLowestPrice.setFocusable(true);
                    etFilterLowestPrice.setFocusableInTouchMode(true);
                    etFilterLowestPrice.requestFocus();
                    clearBtnStyle();
                    popupInputMethodWindow();
                    break;
                case R.id.viewFilterHighestPrice:
                    least = 0;
                    etFilterHighestPrice.setFocusable(true);
                    etFilterHighestPrice.setFocusableInTouchMode(true);
                    etFilterHighestPrice.requestFocus();
                    clearBtnStyle();
                    popupInputMethodWindow();
                    break;
            }
        }
    }

    private Handler handler = new Handler();
    private InputMethodManager imm;

    private void popupInputMethodWindow() {
        mFilterPop.getPopupWindow().setOutsideTouchable(false);
        //软键盘不会挡着popupwindow
        mFilterPop.getPopupWindow().setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mFilterPop.getPopupWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    private void filterCitySel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#333333"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));
    }

    private void filterFaceCrossSel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#333333"));
        tvFilterFlashing.setTextColor(Color.parseColor("#919191"));
    }

    private void filterFlashingSel() {
        tvFilterCity.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFaceCross.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFlashing.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterCity.setTextColor(Color.parseColor("#919191"));
        tvFilterFaceCross.setTextColor(Color.parseColor("#919191"));
        tvFilterFlashing.setTextColor(Color.parseColor("#333333"));
    }

    private void filterOneSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#333333"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }

    }

    private void filterHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterTwoHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterFiveHundredSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#333333"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterTwoThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterFiveThousandSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#333333"));
        tvFilterMillion.setTextColor(Color.parseColor("#919191"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    private void filterMillionSel() {
        tvFilterOne.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveHundred.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterTwoThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterFiveThousand.setBackground(getResources().getDrawable(R.drawable.oval_filter_item));
        tvFilterMillion.setBackground(getResources().getDrawable(R.drawable.oval_filter_item_sel));

        tvFilterOne.setTextColor(Color.parseColor("#919191"));
        tvFilterHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveHundred.setTextColor(Color.parseColor("#919191"));
        tvFilterThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterTwoThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterFiveThousand.setTextColor(Color.parseColor("#919191"));
        tvFilterMillion.setTextColor(Color.parseColor("#333333"));

        lowestPrice = 0;
        highestPrice = 0;

        if (!TextUtils.isEmpty(etFilterLowestPrice.getText().toString())) {
            etFilterLowestPrice.setText("");
        }
        if (!TextUtils.isEmpty(etFilterHighestPrice.getText().toString())) {
            etFilterHighestPrice.setText("");
        }
    }

    @OnClick(R.id.etSearchInput)
    public void searchInput() {
        etSearchInput.setFocusable(true);
        etSearchInput.setFocusableInTouchMode(true);
        etSearchInput.requestFocus();
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibSearchTitleBack)
    public void searchTitleBack() {
        SearchActivity.this.finish();
    }

    /**
     * 获取省列表
     */
    private void provinceListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(SearchActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/provinceList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_provinceList")
                .cacheTime(0)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("省列表获取失败！");
//                        Toasty.warning(SearchActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        ProvinceResult provinceResult =
                                (ProvinceResult) GsonUtil.json2Object(response, ProvinceResult.class);
                        if (provinceResult != null
                                && provinceResult.getRet().equals("1")) {
                            list.clear();
                            for (ProvinceResult.ResultBean city : provinceResult.getResult()) {
                                list.add(new SearchAreaCity(false, city));
                            }
                            areaCityAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 获取区县列表
     */
    private void cityListHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentid", id);
        String params = EncryptionUtil.getParameter(SearchActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "index/cityList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_cityList")
                .cacheTime(0)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Logger.e("获取区县列表失败！");
//                        Toasty.warning(SearchActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        CountyResult countyResult =
                                (CountyResult) GsonUtil.json2Object(response, CountyResult.class);
                        if (countyResult != null
                                && countyResult.getRet().equals("1")) {
                            areaCountyAdapter.getDatas().clear();
                            for (CountyResult.ResultBean item : countyResult.getResult()) {
                                areaCountyAdapter.getDatas().add(item.getDname());
                            }
                            areaCountyAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
