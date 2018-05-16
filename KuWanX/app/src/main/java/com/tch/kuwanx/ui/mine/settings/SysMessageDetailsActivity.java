package com.tch.kuwanx.ui.mine.settings;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tch.kuwanx.R;
import com.tch.kuwanx.result.SysMsgListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统消息详情
 */
public class SysMessageDetailsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.tvSysTitle)
    TextView tvSysTitle;
    @BindView(R.id.tvSysContent)
    TextView tvSysContent;
    @BindView(R.id.tvSysDate)
    TextView tvSysDate;
    @BindView(R.id.llSysWeb)
    LinearLayout llSysWeb;
    @BindView(R.id.webShow)
    WebView webShow;
    @BindView(R.id.playSys)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.llSysContent)
    LinearLayout llSysContent;

    private SysMsgListResult.ResultBean sysBean;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_message_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("系统消息");
        if (getIntent().getSerializableExtra("bean") != null) {
            sysBean = (SysMsgListResult.ResultBean) getIntent().getSerializableExtra("bean");
            tvSysTitle.setText(sysBean.getMsg_title());
            tvSysContent.setText(sysBean.getMsg_desc());
            tvSysDate.setText(Utils.times(sysBean.getCreate_time(), "yyyy.MM.dd HH:mm:ss"));
            if (sysBean.getMsg_type().equals("1")) {
                webShow.setVisibility(View.GONE);
                videoPlayer.setVisibility(View.VISIBLE);
                showVideo(sysBean.getVedio_url());
            } else {
                webShow.setVisibility(View.VISIBLE);
                videoPlayer.setVisibility(View.GONE);
                webShow.loadDataWithBaseURL(null, sysBean.getImg_text(), "text/html", "utf-8", null);
            }
        }
    }

    OrientationUtils orientationUtils;
    private boolean mIsFullScreen = false;
    private int width, height;

    private void showVideo(String videoUri) {
        width = Utils.getScreenWidth(SysMessageDetailsActivity.this);
        height = width * 9 / 16;
        videoPlayer.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        videoPlayer.setUp(videoUri, true, sysBean.getVedio_title());

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    //横屏
                    vertical();
                } else {
                    //竖屏
                    horizontal();
                }
            }
        });

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vertical();
            }
        });
    }

    /**
     * 横屏
     */
    private void horizontal() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        rlTitle.setVisibility(View.GONE);
        llSysContent.setVisibility(View.GONE);
        orientationUtils.resolveByClick();
        videoPlayer.setLayoutParams(new LinearLayout.LayoutParams(
                Utils.getScreenWidth(SysMessageDetailsActivity.this),
                Utils.getScreenHeight(SysMessageDetailsActivity.this)));
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置为全屏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        orientationUtils.setScreenType(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 竖屏
     */
    private void vertical() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        rlTitle.setVisibility(View.VISIBLE);
        llSysContent.setVisibility(View.VISIBLE);
        videoPlayer.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            orientationUtils.backToProtVideo();
            orientationUtils.setScreenType(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    void ibTitleBack() {
        SysMessageDetailsActivity.this.finish();
    }
}
