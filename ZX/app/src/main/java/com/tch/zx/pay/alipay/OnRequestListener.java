package com.tch.zx.pay.alipay;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface OnRequestListener {
    void onSuccess(String s);

    void onError(String s);
}
