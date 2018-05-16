package com.tch.zx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * Created by JoePeng on 2017/6/8.
 */

public class GoTopScrollView extends ScrollView implements View.OnClickListener {

    //展示置顶的图片按钮
    private ImageView goTopBtn;
    //屏幕高度 //默认高度为500
    private int screenHeight = 500;


    public GoTopScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //1.0
    //设置滑动到多少出现
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    //2.0
    //设置滚动置顶按钮以及其点击监听事件
    public void setImgeViewOnClickGoToFirst(ImageView goTopBtn) {
        this.goTopBtn = goTopBtn;
        this.goTopBtn.setOnClickListener(this);
    }

    //3.0
    //重写滚动改变返回的回调
    // l oldl 分别代表水平位移
    // t oldt 代表当前左上角距离Scrollview顶点的距离
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 滑动距离超过500px,出现置顶按钮,可以做为自定义属性
         * 滑动距离如果用户设置了使用用户的 如果用户没有设置使用默认的
         */
        //3.1
        //当 当前的左上角距离顶点距离 大于某个值的时候就显现置顶按钮出来 如果小雨某个值就隐藏
        if (screenHeight != 0) {
            if (t > screenHeight) {
                goTopBtn.setVisibility(VISIBLE);
            } else {
                goTopBtn.setVisibility(GONE);
            }
        }
    }

    //4.0
    //置顶按钮的点击事件监听
    @Override
    public void onClick(View v) {
        //滑动到ScrollView的顶点
        this.smoothScrollTo(0, 0);
    }

}
