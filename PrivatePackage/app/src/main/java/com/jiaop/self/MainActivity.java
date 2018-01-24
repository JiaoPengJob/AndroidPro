package com.jiaop.self;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.jiaop.self.entitis.TitleBarParameter;
import com.jiaop.self.base.BaseActivity;
import com.jiaop.self.utils.GlideUtil;
import com.jiaop.self.utils.TitleBarUtil;
import com.jiaop.self.utils.ToastUtil;
import com.jiaop.self.views.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @BindView(R.id.ivShow)
    ImageView ivShow;

    @OnClick(R.id.btClick)
    void btClick() {
        GlideUtil.loadCircleImg(this,"http://img0.imgtn.bdimg.com/it/u=2171073523,1991282945&fm=27&gp=0.jpg",ivShow);
    }

    @OnClick(R.id.tvClick)
    void tvClick(){
        ToastUtil.info(this,"点击");
    }

    @BindView(R.id.tbBar)
    TitleBar tbBar;

    @Override
    protected void initView() {
        initTitle();
    }

    /**
     * 标题栏
     */
    private void initTitle() {
        TitleBarParameter parameter = new TitleBarParameter();
        parameter.setTitle("主标题");
        parameter.setTitleColor(Color.WHITE);
        TitleBarUtil.initTitle(tbBar, parameter);
        tbBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.normal(MainActivity.this, "点击");
            }
        });
    }

    @Override
    protected void initNetData() {

    }

    @Override
    protected void initOfflineData() {

    }

    @Override
    protected int statusBarColor() {
        return R.color.colorAccent;
    }
}
