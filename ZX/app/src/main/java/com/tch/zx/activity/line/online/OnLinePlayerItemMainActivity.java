package com.tch.zx.activity.line.online;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.fragment.line.OnLinePlayerItemMainFragment;
import com.tch.zx.fragment.line.OnLinePlayerItemTalkFragment;
import com.tch.zx.http.bean.result.LiveDetailsResultBean;
import com.tch.zx.http.presenter.LiveDetailsPresenter;
import com.tch.zx.http.view.LiveDetailsView;
import com.tch.zx.media.MediaPlayerController;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播/观看的页面
 */
public class OnLinePlayerItemMainActivity extends BaseActivity {

    /**
     * 整体选项卡布局
     */
    @BindView(R.id.stl_general)
    SmartTabLayout stl_general;
    /**
     * 整体选项卡子布局ViewPager
     */
    @BindView(R.id.vp_general)
    ViewPager vp_general;
    /**
     * 视频播放整体布局
     */
    @BindView(R.id.rl_player_play)
    RelativeLayout rl_player_play;
    /**
     * 整体父布局
     */
    @BindView(R.id.ll_activity_player_item_main)
    LinearLayout ll_activity_player_item_main;

    /**
     * 接收传递的参数
     */
    private String appUserId = "";
    private int liveId;
    /**
     * 接口
     */
    private LiveDetailsPresenter liveDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_on_line_player_item_main);
        //集成使用Butterknife
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        appUserId = getIntent().getStringExtra("appUserId");
        liveId = getIntent().getIntExtra("liveId", 0);

        liveDetailsPresenter = new LiveDetailsPresenter(this);
        liveDetailsPresenter.onCreate();
        liveDetailsPresenter.attachView(liveDetailsView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("liveId", String.valueOf(liveId));

        String data = HelperUtil.getParameter(map);
        liveDetailsPresenter.liveDetails(data);
    }

    /**
     * 加载视频播放器
     */
    private void initPlayer(String type, String url, String viewNum, String collectTypeId) {
        MediaPlayerController mediaPlayerController = new MediaPlayerController(OnLinePlayerItemMainActivity.this, appUserId, "1", collectTypeId, type, url, viewNum, rl_player_play, ll_activity_player_item_main);
        mediaPlayerController.init();
    }

    /**
     * 加载首页tabView信息
     */
    private void setViewPagersData(LiveDetailsResultBean.ResultBean.ResponseObjectBean liveDetailsBean) {
        //向选项卡布局中添加子布局Fragment
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("课程", OnLinePlayerItemMainFragment.class, new Bundler().putSerializable("liveBean", liveDetailsBean).get())
                .add("讨论", OnLinePlayerItemTalkFragment.class)
                .create());
        vp_general.setAdapter(adapter);
        stl_general.setViewPager(vp_general);
    }

    /**
     * 接口回调
     */
    private LiveDetailsView liveDetailsView = new LiveDetailsView() {
        @Override
        public void onSuccess(LiveDetailsResultBean liveDetailsResultBean) {
            if (liveDetailsResultBean.getResult().getResponseObject() != null) {
                initPlayer(liveDetailsResultBean.getResult().getResponseObject().getLiveStatus(),
                        liveDetailsResultBean.getResult().getResponseObject().getLiveVideo(),
                        liveDetailsResultBean.getResult().getResponseObject().getLiveViewNum(),
                        String.valueOf(liveDetailsResultBean.getResult().getResponseObject().getLiveId()));
                setViewPagersData(liveDetailsResultBean.getResult().getResponseObject());
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "liveDetailsView:==" + result);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        return super.onKeyDown(keyCode, event);
    }
}
