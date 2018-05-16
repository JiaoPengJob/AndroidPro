package com.tch.kuwanx.https;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.zhouyou.http.subsciber.IProgressDialog;

/**
 * Created by jiaop on 2017/12/1.
 * Http帮助类
 */

public class HttpUtils {

    public static final String URI_CENTER = "shareplay-api/";

    public static IProgressDialog getIProgressDialog(final Context mContext, final String msg) {
        return new IProgressDialog() {

            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage(msg);
                return dialog;
            }
        };
    }

}
