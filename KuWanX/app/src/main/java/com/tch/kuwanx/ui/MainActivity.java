package com.tch.kuwanx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.login.LoginActivity;
import com.tch.kuwanx.utils.SharedPrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ivMain)
    ImageView ivMain;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                boolean first = SharedPrefsUtil.getValue(MainActivity.this, "isAppFirst", true);
                if (first) {
                    SharedPrefsUtil.putValue(MainActivity.this, "isAppFirst", false);
                    intent = new Intent(MainActivity.this, GuideActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                } else {
                    if (SharedPrefsUtil.getValue(MainActivity.this, "hasLogin", true)) {
                        intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    MainActivity.this.finish();
                }
            }
        }, 2000);
    }
}
