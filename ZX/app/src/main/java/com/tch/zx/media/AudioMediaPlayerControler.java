package com.tch.zx.media;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.MediaSharePopupWindow;
import com.tch.zx.view.RewardPopupWindow;

import java.io.IOException;

import butterknife.OnClick;

/**
 * 音频控制器
 */

public class AudioMediaPlayerControler implements View.OnClickListener {

    private Context context;
    private String url;
    private View view;
    private View parentView;

    //控制器打赏
    private ImageView iv_audio_controller_reward;
    //分享
    private ImageView iv_audio_controller_share;
    //收藏
    private ImageView iv_audio_controller_collection;
    //进度条
    private SeekBar sb_audio_controller;
    //播放进度
    private TextView tv_audio_controller_timer;
    //音频总进度
    private TextView tv_audio_controller_time;
    //上一首
    private ImageView iv_audio_controller_last;
    //播放/暂停
    private ImageView iv_audio_controller_play;
    //下一首
    private ImageView iv_audio_controller_next;

    /*-----------------------------------------------------------*/
    //设置popupwindow的布局参数
    private WindowManager.LayoutParams params;
    //音频控制器
    private MediaPlayer mediaPlayer;
    //是否暂停状态
    private boolean isPaused = true;
    //音频播放当前进度
    private long audioDurationPosition = 0;
    //子线程
    private Thread sbThread;

    /**
     * 构造函数
     *
     * @param context    文本对象
     * @param url        播放地址
     * @param view       控制器布局
     * @param parentView 展示小窗口的父布局
     */
    public AudioMediaPlayerControler(Context context, String url, View view, View parentView) {
        this.context = context;
        this.url = url;
        this.view = view;
        this.parentView = parentView;
    }

    /**
     * 初始化音频播放
     */
    public void initAudioPlayer() {
        initView();
        startMediaPlayer();
    }

    /**
     * 加载布局
     */
    private void initView() {
        iv_audio_controller_reward = (ImageView) view.findViewById(R.id.iv_audio_controller_reward);
        iv_audio_controller_share = (ImageView) view.findViewById(R.id.iv_audio_controller_share);
        iv_audio_controller_collection = (ImageView) view.findViewById(R.id.iv_audio_controller_collection);
        sb_audio_controller = (SeekBar) view.findViewById(R.id.sb_audio_controller);
        tv_audio_controller_timer = (TextView) view.findViewById(R.id.tv_audio_controller_timer);
        tv_audio_controller_time = (TextView) view.findViewById(R.id.tv_audio_controller_time);
        iv_audio_controller_last = (ImageView) view.findViewById(R.id.iv_audio_controller_last);
        iv_audio_controller_play = (ImageView) view.findViewById(R.id.iv_audio_controller_play);
        iv_audio_controller_next = (ImageView) view.findViewById(R.id.iv_audio_controller_next);

        iv_audio_controller_reward.setOnClickListener(this);
        iv_audio_controller_share.setOnClickListener(this);
        iv_audio_controller_collection.setOnClickListener(this);
        iv_audio_controller_last.setOnClickListener(this);
        iv_audio_controller_play.setOnClickListener(this);
        iv_audio_controller_next.setOnClickListener(this);

        /**
         * 进度条监听事件
         */
        sb_audio_controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioDurationPosition = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进度条手势停止后,音频跳转播放进度
                mediaPlayer.seekTo((int) audioDurationPosition);
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
            //打赏
            case R.id.iv_audio_controller_reward:
                rewardInfo();
                break;
            //分享
            case R.id.iv_audio_controller_share:
                shareInfo();
                break;
            //收藏
            case R.id.iv_audio_controller_collection:

                break;
            //上一首
            case R.id.iv_audio_controller_last:
                clearAudioMediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(url));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    sbThread.start();
                    iv_audio_controller_play.setImageResource(R.mipmap.audio_pause);
                    isPaused = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            //播放/暂停
            case R.id.iv_audio_controller_play:
                //如果是暂停状态
                if (isPaused) {
                    iv_audio_controller_play.setImageResource(R.mipmap.audio_pause);
                    mediaPlayer.start();
                    isPaused = false;
                } else {
                    //播放状态
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        iv_audio_controller_play.setImageResource(R.mipmap.audio_start);
                        isPaused = true;
                    }
                }
                break;
            //下一首
            case R.id.iv_audio_controller_next:
                clearAudioMediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(url));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    sbThread.start();
                    iv_audio_controller_play.setImageResource(R.mipmap.audio_pause);
                    isPaused = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 初始化播放音频
     */
    private void startMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.prepare();
            sb_audio_controller.setMax(mediaPlayer.getDuration());
            //展示音频总时间
            tv_audio_controller_time.setText(HelperUtil.timeFormat(mediaPlayer.getDuration(), "HH:mm:ss"));
            //开启子线程,实时展示播放时间进度
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            if (handler != null) {
                                handler.sendEmptyMessage(22);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            //音频控制器播放完毕监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //设置图标变为开始
                    iv_audio_controller_play.setImageResource(R.mipmap.audio_start);
                    isPaused = true;
                    sb_audio_controller.setProgress(0);
                    mediaPlayer.seekTo(0);
                    tv_audio_controller_timer.setText("00:00:00");
                    audioDurationPosition = 0;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 22:
                    tv_audio_controller_timer.setText(HelperUtil.timeFormat(mediaPlayer.getCurrentPosition(), "HH:mm:ss"));
                    sb_audio_controller.setProgress(mediaPlayer.getCurrentPosition());
                    break;
            }
        }
    };

    /**
     * 清空控制器
     */
    private void clearAudioMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer.reset();
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
}
