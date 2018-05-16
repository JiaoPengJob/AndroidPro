package com.tch.kuwanx.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.tch.kuwanx.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by jiaop on 2017/11/22.
 * 发布不还款置换plugin
 */

public class UnRepaymentPlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.un_repayment);
    }

    @Override
    public String obtainTitle(Context context) {
        return "发布不还款置换";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
