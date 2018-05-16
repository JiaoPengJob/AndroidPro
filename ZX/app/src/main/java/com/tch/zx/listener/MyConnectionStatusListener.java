package com.tch.zx.listener;

import android.util.Log;

import io.rong.imlib.RongIMClient;

/**
 * Created by peng on 2017/10/13.
 */

public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

        switch (connectionStatus) {

            case CONNECTED://连接成功。
                Log.e("TAG", "连接成功");
                break;
            case DISCONNECTED://断开连接。
                Log.e("TAG", "断开连接");
                break;
            case CONNECTING://连接中。
                Log.e("TAG", "连接中");
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                Log.e("TAG", "网络不可用");
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                Log.e("TAG", "用户账户在其他设备登录，本机会被踢掉线");
                break;
        }
    }
}
