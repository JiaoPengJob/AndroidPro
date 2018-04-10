package com.jiaop.kotlin;

import android.widget.TextView;

import com.jiaop.libs.base.JPBaseFragment;

import butterknife.BindView;

public class HomeFragment extends JPBaseFragment {

    @BindView(R.id.tvHomeShow)
    TextView tvHomeShow;

    @Override
    protected void initView() {
        tvHomeShow.setText("Home 22");
    }

    @Override
    protected int layoutId() {
        return R.layout.f_1;
    }

    @Override
    protected void initWiFiData() {
        tvHomeShow.setText("Fragment Wifi");
    }

    @Override
    protected void initNetData() {
        tvHomeShow.setText("Fragment Net");
    }

    @Override
    protected void initOfflineData() {
        tvHomeShow.setText("Fragment Off");
    }
}
