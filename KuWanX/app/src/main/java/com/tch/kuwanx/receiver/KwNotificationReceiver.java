package com.tch.kuwanx.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import io.rong.imlib.model.Conversation;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by jiaop on 2017/12/1.
 * 融云推送
 */

public class KwNotificationReceiver extends PushMessageReceiver {

    public KwNotificationReceiver() {
        super();
    }

    /**
     * 用来接收服务器发来的通知栏消息(消息到达客户端时触发)
     *
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    /**
     * 是在用户点击通知栏消息时触发 (注意:如果自定义了通知栏的展现，则不会触发)
     *
     * @param context
     * @param msg
     * @return
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage msg) {
        Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(msg.getConversationType().getName().toLowerCase())
                .appendQueryParameter("targetId", msg.getTargetId())
                .appendQueryParameter("title", msg.getTargetUserName())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("name", "小明");
        intent.putExtras(bundle);
        context.startActivity(intent);
        return true;
    }
}
