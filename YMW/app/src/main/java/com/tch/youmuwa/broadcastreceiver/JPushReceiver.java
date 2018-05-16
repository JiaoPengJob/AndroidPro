package com.tch.youmuwa.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.SwitchRolesResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.employer.MessageCenterActivity;
import com.tch.youmuwa.ui.activity.employer.OrderCenterActivity;
import com.tch.youmuwa.ui.activity.employer.OrderDetailsActivity;
import com.tch.youmuwa.ui.activity.mine.PerfectDataActivity;
import com.tch.youmuwa.ui.activity.worker.WalletActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerDemandDetailsActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerOrderInfoActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.tencent.mm.opensdk.constants.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by peng on 2017/9/6.
 */

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "TAG";
    private Context myContext;

    private JpushParam jp;
    private Intent i;
    private Bundle bundle;
    private PresenterImpl<Object> presenter;

    public JPushReceiver() {
        super();
    }

//    public JPushReceiver(Context context) {
//        super();
//        myContext = context;
//    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        myContext = context;
        Bundle bundle = intent.getExtras();
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
            Log.e(TAG, "JP==" + jp.getUser_type());
            if (jp != null) {
                if (jp.getUser_type().equals("1")) {
                    if (!SharedPrefsUtil.getValue(context, "isEmployer", true)) {
                        switchRoles(1);
                    } else {
                        switch (jp.getType()) {
                            case "101":
                            case "102":
                            case "303":
                                i = new Intent(myContext, MessageCenterActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                            case "103":
                                if (jp.getUser_type().equals("1")) {
                                    i = new Intent(myContext, OrderCenterActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    bundle = new Bundle();
                                    bundle.putSerializable("jp", jp);
                                    i.putExtras(bundle);
                                    myContext.startActivity(i);
                                }
                                break;
                            case "105":
                            case "201":
                            case "202":
                            case "203":
                                if (jp.getUser_type().equals("1")) {
                                    i = new Intent(myContext, OrderDetailsActivity.class);
                                }
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                            case "301":
                            case "302":
                                i = new Intent(myContext, WalletActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                        }
                    }
                } else {
                    if (SharedPrefsUtil.getValue(context, "isEmployer", true)) {
                        switchRoles(2);
                    } else {
                        switch (jp.getType()) {
                            case "101":
                            case "102":
                            case "303":
                                i = new Intent(myContext, MessageCenterActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                            case "103":
                                if (jp.getUser_type().equals("2")) {
                                    i = new Intent(myContext, WorkerDemandDetailsActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    bundle = new Bundle();
                                    bundle.putSerializable("jp", jp);
                                    i.putExtras(bundle);
                                    myContext.startActivity(i);
                                }
                                break;
                            case "105":
                            case "201":
                            case "202":
                            case "203":
                                if (jp.getUser_type().equals("2")) {
                                    i = new Intent(myContext, WorkerOrderInfoActivity.class);
                                }
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                            case "301":
                            case "302":
                                i = new Intent(myContext, WalletActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                bundle = new Bundle();
                                bundle.putSerializable("jp", jp);
                                i.putExtras(bundle);
                                myContext.startActivity(i);
                                break;
                        }
                    }
                }
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.e(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    jp = new JpushParam();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                        switch (myKey) {
                            case "message_id":
                                jp.setMessage_id(json.optString(myKey));
                                break;
                            case "unreadcount":
                                jp.setUnreadcount(json.optString(myKey));
                                break;
                            case "type":
                                jp.setType(json.optString(myKey));
                                break;
                            case "user_type":
                                jp.setUser_type(json.optString(myKey));
                                break;
                            case "require_id":
                                jp.setRequire_id(json.optString(myKey));
                                break;
                            case "order_id":
                                jp.setOrder_id(json.optString(myKey));
                                break;
                            case "order_number":
                                jp.setOrder_number(json.optString(myKey));
                                break;
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

    }

    /**
     * 切换角色
     */
    private void switchRoles(int index) {
        presenter = new PresenterImpl<Object>(myContext);
        presenter.onCreate();
        presenter.switchroles(index);
        presenter.attachView(SwitchRolesView);
    }

    private ClientBaseView<Object> SwitchRolesView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1) {
                SwitchRolesResult switchRolesResult = (SwitchRolesResult) GsonUtil.parseJson(baseBean.getData(), SwitchRolesResult.class);
                if (switchRolesResult.getUserInfo().getType().equals("1")) {
                    SharedPrefsUtil.putValue(myContext, "isEmployer", true);
                } else {
                    SharedPrefsUtil.putValue(myContext, "isEmployer", false);
                }
                switch (jp.getType()) {
                    case "101":
                    case "102":
                    case "303":
                        i = new Intent(myContext, MessageCenterActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        bundle = new Bundle();
                        bundle.putSerializable("jp", jp);
                        i.putExtras(bundle);
                        myContext.startActivity(i);
                        break;
                    case "103":
                        if (jp.getUser_type().equals("1")) {
                            i = new Intent(myContext, OrderCenterActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            bundle = new Bundle();
                            bundle.putSerializable("jp", jp);
                            i.putExtras(bundle);
                            myContext.startActivity(i);
                        } else {
                            i = new Intent(myContext, WorkerDemandDetailsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            bundle = new Bundle();
                            bundle.putSerializable("jp", jp);
                            i.putExtras(bundle);
                            myContext.startActivity(i);
                        }
                        break;
                    case "105":
                    case "201":
                    case "202":
                    case "203":
                        if (jp.getUser_type().equals("1")) {
                            i = new Intent(myContext, OrderDetailsActivity.class);
                        } else if (jp.getUser_type().equals("2")) {
                            i = new Intent(myContext, WorkerOrderInfoActivity.class);
                        } else if (jp.getOrder_id() == null || jp.getOrder_number() == null) {
                            i = new Intent(myContext, MessageCenterActivity.class);
                        }
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        bundle = new Bundle();
                        bundle.putSerializable("jp", jp);
                        i.putExtras(bundle);
                        myContext.startActivity(i);
                        break;
                    case "301":
                    case "302":
                        i = new Intent(myContext, WalletActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        bundle = new Bundle();
                        bundle.putSerializable("jp", jp);
                        i.putExtras(bundle);
                        myContext.startActivity(i);
                        break;
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SwitchRolesView==" + result);
        }
    };
}
