package com.tch.kuwanx.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.wtf("微信支付回调--onCreate--");
        WXPayEntryActivity.this.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.wtf("微信支付回调--onCreate--");
    }

    @Override
    public void onReq(BaseReq req) {
        Logger.wtf("微信支付回调--onReq--");
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.wtf("微信支付回调--onResp--" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Logger.wtf("微信支付回调--resp.errCode == 0--");
            }
        }
    }
}