package com.tch.zx.activity.line;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.fragment.purchased.AllPurchasedFragment;
import com.tch.zx.fragment.purchased.ClassPurchasedFragment;
import com.tch.zx.fragment.purchased.ColumnPurchasedFragment;
import com.tch.zx.fragment.purchased.OnlinePurchasedFragment;
import com.tch.zx.fragment.purchased.UnlinePurchasedFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 已购页面
 */
public class PurchasedActivity extends BaseActivity {

    /**
     * 整体选项卡布局
     */
    @BindView(R.id.stl_purchased)
    SmartTabLayout stl_purchased;
    /**
     * 整体选项卡子布局ViewPager
     */
    @BindView(R.id.vp_purchased)
    ViewPager vp_purchased;
    /**
     * 标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_purchased);
        //集成使用Butterknife
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tv_title_top_all.setText("已购");
        setViewPagersData();
    }

    /**
     * 加载已购tabView信息
     */
    private void setViewPagersData() {
        //向选项卡布局中添加子布局Fragment
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("全部", AllPurchasedFragment.class)
                .add("直播", OnlinePurchasedFragment.class)
                .add("小课", ClassPurchasedFragment.class)
                .add("专栏", ColumnPurchasedFragment.class)
//                .add("线下", UnlinePurchasedFragment.class)
                .create());
        vp_purchased.setAdapter(adapter);
        stl_purchased.setViewPager(vp_purchased);
    }

    /**
     * 头标题返回点击事件
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void backReturnOnClick() {
        PurchasedActivity.this.finish();
    }
}
