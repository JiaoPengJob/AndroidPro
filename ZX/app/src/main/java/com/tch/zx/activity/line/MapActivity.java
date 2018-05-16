package com.tch.zx.activity.line;

import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.map.MapView;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地图页面
 */
public class MapActivity extends BaseActivity {

    /**
     * 地图展示
     */
    @BindView(R.id.mapView)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_map);
        //集成使用Butterknife
        ButterKnife.bind(this);

    }
}
