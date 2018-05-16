package com.tch.zx.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.tch.zx.util.NetBroadcastReceiver;
import com.tch.zx.util.NetStateUtil;

/**
 * Activity基类
 */
public class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {

    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        evevt = this;
        inspectNet();
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        this.netMobile = NetStateUtil.getConnectedType(BaseActivity.this);

        if (netMobile == 1) {
            Log.d("ConnectedType", "inspectNet：连接wifi");
        } else if (netMobile == 0) {
            Log.d("ConnectedType", "inspectNet：连接wifi连接移动数据");
        } else if (netMobile == -1) {
            Log.d("ConnectedType", "inspectNet：当前没有网络");
        }

        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }
}
