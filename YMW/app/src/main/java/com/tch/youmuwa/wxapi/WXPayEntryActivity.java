package com.tch.youmuwa.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.employer.DismissReasonActivity;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "微信支付回调--onCreate--");
        WXPayEntryActivity.this.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("TAG", "微信支付回调--onNewIntent--");
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("TAG", "微信支付回调--onReq--");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("TAG", "微信支付回调--onResp--" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e("TAG", "微信支付回调--1--" + resp.errCode);
            if (resp.errCode == 0) {
                Log.e("TAG", "微信支付回调--2--" + resp.errCode);

            }
        }
    }
}