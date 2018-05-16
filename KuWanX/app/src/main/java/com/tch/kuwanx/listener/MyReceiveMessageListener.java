package com.tch.kuwanx.listener;

import com.orhanobut.logger.Logger;
import com.tch.kuwanx.interfaces.GroupInterface;
import com.tch.kuwanx.interfaces.PrivateInterface;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by jiaop on 2017/11/29.
 * 接收消息监听器
 */

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    private PrivateInterface mPi;
    private GroupInterface mGi;

    public MyReceiveMessageListener(){

    }

    public MyReceiveMessageListener(PrivateInterface pi, GroupInterface gi) {
        mPi = pi;
        mGi = gi;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        Logger.wtf("监听器返回的==" + message.getConversationType().getName());
        //私聊
        if (message.getConversationType().getName().equals("private")) {
            mPi.ifRefreshPrivate(true);
        } else if (message.getConversationType().getName().equals("group")) {
            mGi.ifRefreshGroup(true);
        }
        return true;
    }

}
