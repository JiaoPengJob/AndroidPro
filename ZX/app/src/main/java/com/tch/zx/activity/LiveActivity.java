package com.tch.zx.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.tch.zx.R;
import com.tch.zx.adapter.LivingCommentAdapter;
import com.tch.zx.adapter.line.OnLinePlayerItemTalkAdapter;
import com.tch.zx.view.CameraPreviewFrameView;
import com.tch.zx.view.RecyclerViewDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 进行直播页面
 */
public class LiveActivity extends Activity implements StreamingStateChangedListener {
    /**
     * 列表
     */
    @BindView(R.id.rv_live_comment)
    RecyclerView rv_live_comment;
    /**
     * 正式开启
     */
    @BindView(R.id.tv_live_start)
    TextView tv_live_start;
    /**
     * 直播显示布局
     */
    @BindView(R.id.cameraPreview_afl)
    AspectFrameLayout afl;
    @BindView(R.id.cameraPreview_surfaceView)
    CameraPreviewFrameView surfaceView;
    /**
     * 列表适配器
     */
    private LivingCommentAdapter livingCommentAdapter;
    /**
     * 直播所需要的参数
     */
    private JSONObject mJSONObject;
    private MediaStreamingManager mMediaStreamingManager;
    private StreamingProfile mProfile;
    private CameraStreamingSetting setting;
    /**
     * 通过serverUrl,链接服务器获取到streamJson信息
     */
    private String streamJson = "testContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_live);
        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        initData();
        ready();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaStreamingManager != null) {
            mMediaStreamingManager.resume();
        }
    }

    /**
     * 加载列表数据
     */
    private void initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        livingCommentAdapter = new LivingCommentAdapter(this, list);
        rv_live_comment.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        rv_live_comment.addItemDecoration(new RecyclerViewDecoration(this, "#FFFFFF", 0, false));
        rv_live_comment.setAdapter(livingCommentAdapter);
    }

    /**
     * 正式开始
     */
    @OnClick(R.id.tv_live_start)
    public void startLiving() {
        tv_live_start.setVisibility(View.GONE);
        startLive();
    }

    /**
     * 关闭直播
     */
    @OnClick(R.id.iv_close_live)
    public void closeLive() {
        this.finish();
    }

    private void ready() {
        setting = buildCameraStreamingSetting();
        mProfile = new StreamingProfile();
        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH1)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
                .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_480)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY);
        mMediaStreamingManager = new MediaStreamingManager(this, afl, surfaceView, AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);
        mMediaStreamingManager.prepare(setting, mProfile);
    }

    private CameraStreamingSetting buildCameraStreamingSetting() {
        CameraStreamingSetting cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_VIDEO)
                .setContinuousFocusModeEnabled(true)
                .setFrontCameraPreviewMirror(true)
                .setFrontCameraMirror(true).setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f));
        return cameraStreamingSetting;
    }

    /**
     * 开启直播
     */
    private void startLive() {
        try {
            mJSONObject = new JSONObject("I:-5IVlpFNNGJHwv-2qKwVIakC0ME=");
            StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
            mProfile.setStream(stream);
            mMediaStreamingManager.prepare(setting, mProfile);
            mMediaStreamingManager.setStreamingStateListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaStreamingManager.pause();
    }

    /**
     * 直播监听
     *
     * @param streamingState
     * @param o
     */
    @Override
    public void onStateChanged(StreamingState streamingState, Object o) {
        switch (streamingState) {
            case PREPARING:
                Log.e("TAG", "准备");
                break;
            case READY:
                Log.e("TAG", "准备开始");
                // start streaming when READY
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMediaStreamingManager != null) {
                            mMediaStreamingManager.startStreaming();
                        }
                    }
                }).start();
                break;
            case CONNECTING:
                break;
            case STREAMING:
                Log.e("TAG", " had been sent");
                // The av packet had been sent.
                break;
            case SHUTDOWN:
                Log.e("TAG", "直播结束");
                // The streaming had been finished.
                break;
            case IOERROR:
                Log.e("TAG", "链接失败");
                // Network connect error.
                break;
            case OPEN_CAMERA_FAIL:
                Log.e("TAG", "摄像头打开失败");
                // Failed to open camera.
                break;
            case DISCONNECTED:
                Log.e("TAG", "链接关闭");
                // The socket is broken while streaming
                break;
        }
    }
}
