package com.jiaop.self.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.jiaop.self.utils.ActivityUtil;
import com.jiaop.self.utils.NetWorkUtil;

import butterknife.ButterKnife;

/**
 * Created by JiaoP
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        //隐藏标题栏
        getSupportActionBar().hide();
        //加载View注解
        ButterKnife.bind(this);
        //Activity栈管理
        ActivityUtil.getInstance().addActivity(this);
        //沉浸式状态栏
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .statusBarColor(statusBarColor())
                .fitsSystemWindows(true)
                .init();
        //加载布局样式
        initView();
        //判断是否有网络状态，分别加载网络数据和离线数据
        if (NetWorkUtil.isNetworkConnected(this)) {
            initNetData();
        } else {
            initOfflineData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * 获取layoutId
     *
     * @return
     */
    protected abstract int layoutId();

    /**
     * 加载页面
     */
    protected abstract void initView();

    /**
     * 加载网络数据
     */
    protected abstract void initNetData();

    /**
     * 加载离线数据
     * 用于设置没网状态下的数据
     */
    protected abstract void initOfflineData();

    /**
     * 设置状态栏样式
     *
     * @return
     */
    protected abstract int statusBarColor();
}
