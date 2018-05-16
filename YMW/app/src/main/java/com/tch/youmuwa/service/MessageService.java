package com.tch.youmuwa.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.MessageInterface;
import com.tch.youmuwa.util.GsonUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于接收消息列表
 */

public class MessageService extends Service {

    private Thread thread;
    private Timer timer;
    private TimerTask task;
    private PresenterImpl<Object> presenter;
    private static MessageInterface mi;

    public MessageService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread() {
            @Override
            public void run() {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                };
                timer.schedule(task, 0, 1000 * 60);
            }
        };
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                getMsgs();
            }
        }
    };

    /**
     * 获取消息列表
     */
    private void getMsgs() {
        presenter = new PresenterImpl<Object>(this);
        presenter.onCreate();
        presenter.getmessagelist();
        presenter.attachView(msgView);
    }

    private ClientBaseView<Object> msgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                if (mi != null) {
                    mi.getMsg(null);
                }
            } else {
                MsgResult msg = (MsgResult) GsonUtil.parseJson(baseBean.getData(), MsgResult.class);
                if (mi != null) {
                    mi.getMsg(msg);
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "msgView--" + result);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public static void setMi(MessageInterface mi) {
        MessageService.mi = mi;
    }
}
