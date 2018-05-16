package com.tch.zx.activity.mine;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.LiveActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我要成为主页
 */
public class ToBeMineActivity extends BaseActivity {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    /**
     * 跳转
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_to_be_mine);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("成为");
    }

    /**
     * 成为导师
     */
    @OnClick(R.id.rl_to_be_teacher)
    public void toBeTeacher() {
        intent = new Intent(this, ToBeTeacherActivity.class);
        startActivity(intent);
    }


    /**
     * 成为大咖
     */
    @OnClick(R.id.rl_to_be_big_cast)
    public void toBeBigCast() {
        intent = new Intent(this, ToBeBigCastActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnToBeMain() {
        this.finish();
    }

    /**
     * 我要直播
     */
    @OnClick(R.id.tv_want_live)
    public void wantLive() {
        intent = new Intent(this, LiveActivity.class);
        startActivity(intent);
    }
}
