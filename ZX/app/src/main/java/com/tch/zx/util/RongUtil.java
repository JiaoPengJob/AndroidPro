package com.tch.zx.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 融云工具类
 */

public class RongUtil {
    /**
     * 连接服务器，在整个应用程序全局，只需要调用一次
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     *              //     * @param callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    public static void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("TAG", "--onIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.e("TAG", "--onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG", "--onError" + errorCode);
            }
        });
    }

    /**
     * 发送信息
     */
    public static void send(Conversation.ConversationType conversationType, String id, MessageContent content) {
        /**
         * 发送普通消息
         * @param conversationType      会话类型
         * @param targetId              会话ID
         * @param content               消息的内容，一般是MessageContent的子类对象
         * @param pushContent           接收方离线时需要显示的push消息内容
         * @param pushData              接收方离线时需要在push消息中携带的非显示内容
         * @param SendMessageCallback   发送消息的回调
         * @param ResultCallback        消息存库的回调，可用于获取消息实体
         */
        RongIMClient.getInstance().sendMessage(conversationType, id, content, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                String str = "消息发送成功";
                Log.e("MyReceiveMessage", str);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                String str = "消息发送失败";
                Log.e("MyReceiveMessage", str);
            }
        }, null);
    }

    /**
     * 创建群组
     */
    public static void joinHadGroup(Context context, String groupId, String title) {
        RongIM.getInstance().startGroupChat(context, groupId, title);
    }

}
