package com.tch.zx.activity.mine;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.view.Window;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.login_register.GuideActivity;
import com.tch.zx.activity.mine.settings.AboutAppInfoActivity;
import com.tch.zx.activity.mine.settings.AccountSettingActivity;
import com.tch.zx.activity.mine.settings.AddFriendSettingActivity;
import com.tch.zx.activity.mine.settings.FeedBackActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.LiveBeanDao;
import com.tch.zx.dao.green.SmallBeanDao;
import com.tch.zx.dao.green.SpecialBeanDao;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.util.SharedPrefsUtil;
import com.tch.zx.util.SoundPoolUtils;
import com.tch.zx.view.SwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity {
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    /**
     * 推送消息
     */
    @BindView(R.id.sv_push_message)
    SwitchView sv_push_message;
    /**
     * 消息铃声
     */
    @BindView(R.id.sv_message_ring)
    SwitchView sv_message_ring;
    /**
     * 消息震动
     */
    @BindView(R.id.sv_message_shock)
    SwitchView sv_message_shock;

    /**
     * 页面跳转
     */
    private Intent intent;
    private boolean isSound, isShock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initView();
    }

    private AudioManager audioManager;
    private int ringerMode;

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("设置");

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ringerMode = audioManager.getRingerMode();

        sv_push_message.setState(SharedPrefsUtil.getValue(SettingsActivity.this, "isPush", true));
        //推送消息
        sv_push_message.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                sv_push_message.setState(true);
                RongIM.getInstance().setNotificationQuietHours("00:00:00", 1380, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        SharedPrefsUtil.putValue(SettingsActivity.this, "isPush", true);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }

            @Override
            public void toggleToOff() {
                sv_push_message.setState(false);
                RongIM.getInstance().removeNotificationQuietHours(new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        SharedPrefsUtil.putValue(SettingsActivity.this, "isPush", false);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        });


        sv_message_ring.setState(SharedPrefsUtil.getValue(SettingsActivity.this, "isSound", false));
        //消息铃声
        sv_message_ring.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                sv_message_ring.setState(true);
                isSound = true;
                SharedPrefsUtil.putValue(SettingsActivity.this, "isSound", true);
                if (isShock) {
                    needSV();
                } else {
                    onlySound();
                }

            }

            @Override
            public void toggleToOff() {
                sv_message_ring.setState(false);
                isSound = false;
                SharedPrefsUtil.putValue(SettingsActivity.this, "isSound", false);
                if (isShock) {
                    onlyVibrate();
                } else {
                    noSV();
                }
            }
        });

        sv_message_shock.setState(SharedPrefsUtil.getValue(SettingsActivity.this, "isShock", false));
        //消息震动
        sv_message_shock.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                sv_message_shock.setState(true);
                isShock = true;
                SharedPrefsUtil.putValue(SettingsActivity.this, "isShock", true);
                if (isSound) {
                    needSV();
                } else {
                    onlyVibrate();
                }
            }

            @Override
            public void toggleToOff() {
                sv_message_shock.setState(false);
                isShock = false;
                SharedPrefsUtil.putValue(SettingsActivity.this, "isShock", false);
                if (isSound) {
                    onlySound();
                } else {
                    noSV();
                }
            }
        });


    }

    // 获取系统默认铃声的Uri
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
    }

    private MediaPlayer mMediaPlayer;

    /**
     * 铃声
     */
    private void onlySound() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(SettingsActivity.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
        JPushInterface.setDefaultPushNotificationBuilder(builder);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SettingsActivity.this);
        mBuilder.build().defaults |= Notification.DEFAULT_SOUND;
        mBuilder.build().flags |= Notification.FLAG_SHOW_LIGHTS;

//        //有的手机会创建失败，从而导致mMediaPlayer为空。
//        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
//        if (mMediaPlayer == null) {//有的手机铃声会创建失败，如果创建失败，播放我们自己的铃声
//            SoundPoolUtils.playCallWaitingAudio();//自己定义的铃音播放工具类。具体实现见下方
//        } else {
//            mMediaPlayer.setLooping(true);// 设置循环
//            try {
//                mMediaPlayer.prepare();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mMediaPlayer.start();
//        }
//
//        if (vibrator != null) {
//            vibrator.cancel();
//        } else {
//            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            vibrator.vibrate(0);
//        }
    }

    private Vibrator vibrator;

    /**
     * 振动
     */
    private void onlyVibrate() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(SettingsActivity.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        JPushInterface.setDefaultPushNotificationBuilder(builder);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SettingsActivity.this);
        mBuilder.build().defaults |= Notification.DEFAULT_VIBRATE;
        mBuilder.build().flags |= Notification.FLAG_SHOW_LIGHTS;

//        if (vibrator == null) {
//            //获取震动服务
//            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        }
//        //震动模式隔1秒震动1.4秒
//        long[] pattern = {1000, 1400};
//        //震动重复，从数组的0开始（-1表示不重复）
//        vibrator.vibrate(pattern, 0);
//
//        if (mMediaPlayer != null) {
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//                mMediaPlayer = null;
//            }
//        }
//        SoundPoolUtils.stopCallWaitingAudio();
    }

    /**
     * 都不要
     */
    private void noSV() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(SettingsActivity.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
        JPushInterface.setDefaultPushNotificationBuilder(builder);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SettingsActivity.this);
        mBuilder.build().sound = null;//取消铃声
        mBuilder.build().vibrate = null;//取消震动
    }

    /**
     * 都要
     */
    private void needSV() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(SettingsActivity.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;
        JPushInterface.setDefaultPushNotificationBuilder(builder);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SettingsActivity.this);
        mBuilder.build().defaults |= Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        mBuilder.build().flags |= Notification.FLAG_SHOW_LIGHTS;
    }

    /**
     * 加好友设置
     */
    @OnClick(R.id.rl_add_friends_settings)
    public void intentAddFriendsSettings() {
        intent = new Intent(this, AddFriendSettingActivity.class);
        startActivity(intent);
    }


    /**
     * 账号设置
     */
    @OnClick(R.id.rl_account_settings)
    public void intentAccountSettings() {
        intent = new Intent(this, AccountSettingActivity.class);
        startActivity(intent);
    }

    /**
     * 关于直线
     */
    @OnClick(R.id.rl_about_app_info)
    public void intentAboutAppInfo() {
        intent = new Intent(this, AboutAppInfoActivity.class);
        startActivity(intent);
    }


    /**
     * 意见反馈
     */
    @OnClick(R.id.rl_feed_back_settings)
    public void intentFeedBack() {
        intent = new Intent(this, FeedBackActivity.class);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.tvExitLogin)
    public void exitLogin() {
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        LiveBeanDao liveBeanDao = daoSession.getLiveBeanDao();
        SmallBeanDao smallBeanDao = daoSession.getSmallBeanDao();
        SpecialBeanDao specialBeanDao = daoSession.getSpecialBeanDao();
        userBeanDao.deleteAll();
        liveBeanDao.deleteAll();
        smallBeanDao.deleteAll();
        specialBeanDao.deleteAll();
        Intent intent = new Intent(SettingsActivity.this, GuideActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnSetting() {
        this.finish();
    }

}
