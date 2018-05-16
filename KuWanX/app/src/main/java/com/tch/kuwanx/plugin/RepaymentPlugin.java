package com.tch.kuwanx.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.chat.ProposedExActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by jiaop on 2017/11/22.
 * 发布还款置换plugin
 */

public class RepaymentPlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.repayment);
    }

    @Override
    public String obtainTitle(Context context) {
        return "发布还款置换";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent = new Intent(fragment.getActivity(), ProposedExActivity.class);
        fragment.getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
