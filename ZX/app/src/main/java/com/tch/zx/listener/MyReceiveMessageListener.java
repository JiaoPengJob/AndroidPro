package com.tch.zx.listener;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 融云接收信息监听
 */

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    @Override
    public boolean onReceived(Message message, int i) {
        switch (message.getConversationType()) {
            case PRIVATE:           //单聊
                Log.e("MyReceiveMessage", "--单聊" + message.getConversationType() + "//" + message.getSentTime());
                TextMessage rongMsg = (TextMessage) message.getContent();
                Log.d("MyReceiveMessage", "融云接收消息-->"
                        + "userId:" + message.getSenderUserId()
                        + " 内容:" + rongMsg.getContent()
                        + " 发送时间:" + message.getSentTime());
                break;
            case GROUP:             //群组
                Log.e("MyReceiveMessage", "--群组");
                break;
            case DISCUSSION:        //讨论组
                Log.d("MyReceiveMessage", "--讨论组");
                break;
            case CHATROOM:          //聊天室
                Log.d("MyReceiveMessage", "--聊天室");
                break;
            case CUSTOMER_SERVICE:  //客服
                Log.d("MyReceiveMessage", "--客服");
                break;
            case SYSTEM:            //系统会话
                Log.d("MyReceiveMessage", "--系统会话");
                break;
            default:
                break;
        }
        return false;
    }
}
