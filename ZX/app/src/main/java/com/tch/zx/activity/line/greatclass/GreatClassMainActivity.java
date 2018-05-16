package com.tch.zx.activity.line.greatclass;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.fragment.line.ClassFragment;
import com.tch.zx.fragment.line.HotOnlinePlayerRemdFragment;
import com.tch.zx.http.presenter.TypePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 精品小课主页面
 */
public class GreatClassMainActivity extends BaseActivity {

    /**
     * tab标签栏
     */
    @BindView(R.id.stl_general)
    SmartTabLayout stl_general;
    /**
     * 标签栏viewpager
     */
    @BindView(R.id.vp_general)
    ViewPager vp_general;
    /**
     * 头标题内容
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
        setContentView(R.layout.activity_on_line_player_main);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化页面信息
     */
    private void initView() {
        tv_title_top_all.setText("精品小课");
        setViewPagersData();
    }


    /**
     * 加载tabView信息
     */
    private void setViewPagersData() {
        //向选项卡布局中添加子布局Fragment，
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("全部", ClassFragment.class)
                .create());
        vp_general.setAdapter(adapter);
        stl_general.setViewPager(vp_general);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnBackOnClick() {
        GreatClassMainActivity.this.finish();
    }

}
