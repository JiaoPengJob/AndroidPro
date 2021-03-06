package com.tch.zx.media;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.presenter.CollectCancelPresenter;
import com.tch.zx.http.presenter.CollectInsertPresenter;
import com.tch.zx.http.view.CollectCancelView;
import com.tch.zx.http.view.CollectInsertView;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.MediaSharePopupWindow;
import com.tch.zx.view.RewardPopupWindow;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by peng on 2017/6/15.
 */

public class MediaPlayerController implements View.OnClickListener {

    private String type;
    private String url;
    private String viewNum;
    private String collectType;
    private String appUserId;
    private String collectTypeId;

    /**
     * 控制器布局
     */
    private View view;
    /**
     * 文本对象
     */
    private Context context;
    /**
     * 播放器
     */
    private static IjkVideoView ijk_view_player;
    /**
     * 返回按钮
     */
    private ImageView iv_return_media_player;
    /**
     * 控制器顶部布局
     */
    private RelativeLayout rl_media_controller_top;
    /**
     * 打赏
     */
    private ImageView iv_reward_media_player;
    /**
     * 控制器底部布局
     */
    private RelativeLayout rl_media_controller_bottom;
    /**
     * 视频的点击布局
     */
    private LinearLayout ll_ijk_layout;
    /**
     * 暂停按钮
     */
    private ImageView iv_player_pause;
    /**
     * 当前播放时间进度
     */
    private TextView tv_player_timer;
    /**
     * 视频总时间进度
     */
    private TextView tv_player_time;
    /**
     * 分享
     */
    private ImageView iv_small_player_share;
    /**
     * 收藏
     */
    private ImageView iv_small_player_collection;
    /**
     * 观看人数
     */
    private TextView tv_small_player_audience_num;
    /**
     * 全屏
     */
    private ImageView iv_screen_media_player;
    /**
     * 底部不是直播时需要隐藏的布局
     */
    private RelativeLayout rl_bottom_left;
    /**
     * 锁屏按钮
     */
    private ImageView iv_lock_media_player;

    /**
     * 进度条
     */
    private SeekBar sb_player;
    /**
     * 计时器
     */
    private Timer timer;
    private TimerTask timerTask;
    /**
     * 是否暂停
     */
    private boolean isPaused = false;
    /**
     * 视频播放当前进度
     */
    private long vedioDurationPosition = 0;
    /**
     * 是否为全屏
     */
    private boolean isfullScreen = false;
    /**
     * 是否锁屏中
     */
    private boolean isLock = false;
    /**
     * activity的整体布局
     */
    private View parentView;
    /**
     * 小课隐藏的布局
     */
    private View visibilityView;
    /**
     * 设置popupwindow的布局参数
     */
    private WindowManager.LayoutParams params;
    /**
     * 菊花圈效果
     */
    private SpinKitView spin_kit;

    private CollectInsertPresenter collectInsertPresenter;
    private CollectCancelPresenter cancelPresenter;

    private LinearLayout llBgmBlack;

    /**
     * 有参构造
     */
    public MediaPlayerController(Context context, String appUserId, String collectType, String collectTypeId, String type, String url, String viewNum, View view, View parentView) {
        this.context = context;
        this.type = type;
        this.url = url;
        this.viewNum = viewNum;
        this.view = view;
        this.parentView = parentView;
        this.collectType = collectType;
        this.appUserId = appUserId;
        this.collectTypeId = collectTypeId;
    }

    /**
     * 加载
     */
    public void init() {
        initView();
        setOtherListener();
        startVedio();
    }

    /**
     * 开始播放
     */
    private void startVedio() {
        //播放器在视频预处理完成后被调用
        ijk_view_player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                //设置进度条总长度
                sb_player.setMax(ijk_view_player.getDuration());
                //展示视频总时间
                tv_player_time.setText(HelperUtil.timeFormat(iMediaPlayer.getDuration(), "HH:mm:ss"));
                //开启子线程,实时展示播放时间进度
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                if (handler != null) {
                                    handler.sendEmptyMessage(12);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                if (ijk_view_player != null) {
                    ijk_view_player.start();
                    //设置图标变为暂停
                    iv_player_pause.setImageResource(R.mipmap.player_pause);
                    isPaused = true;
                }
            }
        });
    }

    /**
     * 设置组件相关事件监听
     */
    private void setOtherListener() {
        iv_return_media_player.setOnClickListener(this);
        iv_small_player_collection.setOnClickListener(this);
        iv_small_player_share.setOnClickListener(this);
        iv_reward_media_player.setOnClickListener(this);
        iv_player_pause.setOnClickListener(this);
        iv_screen_media_player.setOnClickListener(this);
        ll_ijk_layout.setOnClickListener(this);
        iv_lock_media_player.setOnClickListener(this);

        //设置进度条滑动播放进度
        sb_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                vedioDurationPosition = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进度条手势停止后,视频跳转播放进度
                ijk_view_player.seekTo((int) vedioDurationPosition);
            }
        });

        //播放器播放完成
        ijk_view_player.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                clearData();
            }
        });
    }

    /**
     * 点击事件监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_return_media_player:
                if (isfullScreen) {
                    smallScreen();
                } else {
                    clearData();
                    ((Activity) context).finish();
                }
                break;
            //收藏
            case R.id.iv_small_player_collection:
                if (iv_small_player_collection.getDrawable().getCurrent().getConstantState()
                        .equals(context.getResources().getDrawable(R.mipmap.player_collection).getConstantState())) {
                    collectInsert();
                } else {
                    collectCancel();
                }
                break;
            //分享
            case R.id.iv_small_player_share:
                shareInfo();
                break;
            //打赏
            case R.id.iv_reward_media_player:
                rewardInfo();
                break;
            //暂停
            case R.id.iv_player_pause:
                //如果点击的是开始图片
                if (isPaused) {
                    //判断播放器是否正在播放
                    if (ijk_view_player.isPlaying()) {
                        //播放器暂停
                        ijk_view_player.pause();
                        //设置图标变为开始
                        iv_player_pause.setImageResource(R.mipmap.player_start);
                        isPaused = false;
                    }
                } else {
                    //播放器开始
                    ijk_view_player.start();
                    //设置图标变为暂停
                    iv_player_pause.setImageResource(R.mipmap.player_pause);
                    isPaused = true;
                }
                break;
            //全屏
            case R.id.iv_screen_media_player:
                if (isfullScreen) {
                    smallScreen();
                } else {
                    fullScreen();
                }
                break;
            //视频点击
            case R.id.ll_ijk_layout:
                if (isLock) {
                    iv_lock_media_player.setVisibility(View.VISIBLE);
                } else {
                    //直播状态
                    if (type.equals("2")) {
                        if (rl_media_controller_top.getVisibility() == View.GONE && iv_reward_media_player.getVisibility() == View.GONE && rl_media_controller_bottom.getVisibility() == View.GONE) {
                            rl_media_controller_top.setVisibility(View.VISIBLE);
                            iv_reward_media_player.setVisibility(View.VISIBLE);
                            rl_media_controller_bottom.setVisibility(View.VISIBLE);
                            rl_bottom_left.setVisibility(View.GONE);
                        }
                    } else {
                        if (rl_media_controller_top.getVisibility() == View.GONE && iv_reward_media_player.getVisibility() == View.GONE && rl_media_controller_bottom.getVisibility() == View.GONE) {
                            rl_media_controller_top.setVisibility(View.VISIBLE);
                            iv_reward_media_player.setVisibility(View.VISIBLE);
                            rl_media_controller_bottom.setVisibility(View.VISIBLE);
                        }
                    }
                    if (isfullScreen) {
                        iv_lock_media_player.setVisibility(View.VISIBLE);
                    }
                }
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(11);
                    }
                };
                timer.schedule(timerTask, 5000);
                break;
            //锁屏
            case R.id.iv_lock_media_player:
                if (isLock) {
                    isLock = false;
                    iv_lock_media_player.setImageResource(R.mipmap.player_icon_unlock);
                } else {
                    isLock = true;
                    iv_lock_media_player.setImageResource(R.mipmap.player_icon_lock);
                    if (rl_media_controller_top.getVisibility() == View.VISIBLE && iv_reward_media_player.getVisibility() == View.VISIBLE && rl_media_controller_bottom.getVisibility() == View.VISIBLE) {
                        rl_media_controller_top.setVisibility(View.GONE);
                        iv_reward_media_player.setVisibility(View.GONE);
                        rl_media_controller_bottom.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    /**
     * 打赏
     */
    private void rewardInfo() {
        RewardPopupWindow rewardPopupWindow = new RewardPopupWindow(context);
        //设置Popupwindow显示位置（从底部弹出）
        rewardPopupWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = ((Activity) context).getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        ((Activity) context).getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        rewardPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = ((Activity) context).getWindow().getAttributes();
                params.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 分享
     */
    private void shareInfo() {
        MediaSharePopupWindow mediaSharePopupWindow = new MediaSharePopupWindow(context);
        //设置Popupwindow显示位置（从底部弹出）
        mediaSharePopupWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = ((Activity) context).getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        ((Activity) context).getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mediaSharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = ((Activity) context).getWindow().getAttributes();
                params.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(params);
            }
        });

    }

    /**
     * 设置全屏
     */
    private void fullScreen() {
        isfullScreen = true;
        iv_screen_media_player.setImageResource(R.mipmap.player_small_screen);
        iv_small_player_collection.setClickable(false);
        //隐藏状态栏
        WindowManager.LayoutParams wlp = ((Activity) context).getWindow().getAttributes();
        wlp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ((Activity) context).getWindow().setAttributes(wlp);
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        ijk_view_player.setLayoutParams(lp);
    }

    /**
     * 设置小屏
     */
    private void smallScreen() {
        isfullScreen = false;
        iv_screen_media_player.setImageResource(R.mipmap.player_full_screen);
        iv_lock_media_player.setVisibility(View.GONE);
        iv_small_player_collection.setClickable(true);
        //隐藏状态栏
        WindowManager.LayoutParams wlp = ((Activity) context).getWindow().getAttributes();
        wlp.flags |= WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW;
        ((Activity) context).getWindow().setAttributes(wlp);
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 360);
        view.setLayoutParams(lp);
        ijk_view_player.setLayoutParams(lp);
    }

    /**
     * handler线程
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    if (isLock) {
                        iv_lock_media_player.setVisibility(View.GONE);
                    } else {
                        if (rl_media_controller_top.getVisibility() == View.VISIBLE && iv_reward_media_player.getVisibility() == View.VISIBLE && rl_media_controller_bottom.getVisibility() == View.VISIBLE) {
                            rl_media_controller_top.setVisibility(View.GONE);
                            iv_reward_media_player.setVisibility(View.GONE);
                            rl_media_controller_bottom.setVisibility(View.GONE);
                            timer.cancel();
                            timer = null;
                            timerTask.cancel();
                            timerTask = null;
                            handler.removeMessages(11);
                        }
                        if (isfullScreen) {
                            iv_lock_media_player.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 12:
                    llBgmBlack.setVisibility(View.GONE);
                    spin_kit.setVisibility(View.GONE);
                    tv_player_timer.setText(HelperUtil.timeFormat(ijk_view_player.getCurrentPosition(), "HH:mm:ss"));
                    sb_player.setProgress(ijk_view_player.getCurrentPosition());
                    break;
            }
        }
    };

    /**
     * 加载组件
     */
    private void initView() {
        ijk_view_player = (IjkVideoView) view.findViewById(R.id.ijk_view_player);
        ijk_view_player.setHudView(new TableLayout(context));
        ijk_view_player.setVideoURI(Uri.parse(url));

        rl_media_controller_top = (RelativeLayout) view.findViewById(R.id.rl_media_controller_top);
        iv_reward_media_player = (ImageView) view.findViewById(R.id.iv_reward_media_player);
        rl_media_controller_bottom = (RelativeLayout) view.findViewById(R.id.rl_media_controller_bottom);
        ll_ijk_layout = (LinearLayout) view.findViewById(R.id.ll_ijk_layout);
        iv_player_pause = (ImageView) view.findViewById(R.id.iv_player_pause);
        tv_player_timer = (TextView) view.findViewById(R.id.tv_player_timer);
        tv_player_time = (TextView) view.findViewById(R.id.tv_player_time);
        sb_player = (SeekBar) view.findViewById(R.id.sb_player);
        iv_return_media_player = (ImageView) view.findViewById(R.id.iv_return_media_player);
        iv_small_player_share = (ImageView) view.findViewById(R.id.iv_small_player_share);
        iv_small_player_collection = (ImageView) view.findViewById(R.id.iv_small_player_collection);
        tv_small_player_audience_num = (TextView) view.findViewById(R.id.tv_small_player_audience_num);
        iv_screen_media_player = (ImageView) view.findViewById(R.id.iv_screen_media_player);
        rl_bottom_left = (RelativeLayout) view.findViewById(R.id.rl_bottom_left);
        iv_lock_media_player = (ImageView) view.findViewById(R.id.iv_lock_media_player);
        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);
        llBgmBlack = (LinearLayout) view.findViewById(R.id.llBgmBlack);

        tv_small_player_audience_num.setText(viewNum);
    }

    /**
     * 收藏
     */
    private void collectInsert() {
        collectInsertPresenter = new CollectInsertPresenter(context);
        collectInsertPresenter.onCreate();
        collectInsertPresenter.attachView(collectInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("collectType", collectType);
        map.put("collectTypeId", collectTypeId);

        Log.e("TAG", "appUserId==" + appUserId + "collectType==" + collectType + "collectTypeId==" + collectTypeId);

        String data = HelperUtil.getParameter(map);
        collectInsertPresenter.collectInsert(data);

    }

    /**
     * 收藏
     */
    private CollectInsertView collectInsertView = new CollectInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    iv_small_player_collection.setImageResource(R.mipmap.player_collection_audio);
                    Toast.makeText(context, "收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "收藏失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "collectInsertView" + result);
        }
    };

    /**
     * 取消收藏
     */
    private void collectCancel() {
        cancelPresenter = new CollectCancelPresenter(context);
        cancelPresenter.onCreate();
        cancelPresenter.attachView(cancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", appUserId);
        map.put("collectType", collectType);
        map.put("collectTypeId", collectTypeId);

        String data = HelperUtil.getParameter(map);
        cancelPresenter.collectCancel(data);

    }

    private CollectCancelView cancelView = new CollectCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    iv_small_player_collection.setImageResource(R.mipmap.player_collection);
                    Toast.makeText(context, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "取消收藏失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "collectCancelView" + result);
        }
    };

    /**
     * 清空数据
     */
    private void clearData() {
//        //停止播放并释放资源
        ijk_view_player.stopPlayback();
        //设置图标变为开始
        iv_player_pause.setImageResource(R.mipmap.player_start);
        isPaused = false;
        sb_player.setProgress(0);
        ijk_view_player.seekTo(0);
        tv_player_timer.setText("00:00");
        vedioDurationPosition = 0;
    }

    public static void stopIjk() {
        if (ijk_view_player != null) {
            ijk_view_player.stopPlayback();
        }
    }

}
