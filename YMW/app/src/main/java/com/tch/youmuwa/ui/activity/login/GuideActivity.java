package com.tch.youmuwa.ui.activity.login;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.adapter.GuidePagerAdapter;
import com.tch.youmuwa.bean.result.CheckVersionResult;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivtiy {

    @BindView(R.id.vpGuide)
    ViewPager vpGuide;
    @BindView(R.id.tvSkip)
    TextView tvSkip;

    private GuidePagerAdapter guidePagerAdapter;
    private List<View> views;
    private View viewThree;
    private Button btEnterApp;
    private Intent intent;
    private CheckVersionResult cvr;
    private Dialog dialog;
    private TextView tvVersionCancel, tvUpdateVersion;
    private RecyclerView rlVersion;
    private LinearLayoutManager layoutManager;
    private CommonAdapter commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        views = new ArrayList<View>();
        views.add(getLayoutInflater().from(GuideActivity.this).inflate(R.layout.guide_first, null));
        views.add(getLayoutInflater().from(GuideActivity.this).inflate(R.layout.guide_second, null));
        viewThree = getLayoutInflater().from(GuideActivity.this).inflate(R.layout.guide_three, null);
        views.add(viewThree);

        guidePagerAdapter = new GuidePagerAdapter(views);
        vpGuide.setAdapter(guidePagerAdapter);
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tvSkip.setVisibility(View.GONE);
                } else {
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent().getSerializableExtra("cvr") != null) {
            cvr = (CheckVersionResult) getIntent().getSerializableExtra("cvr");
//            versionShow();
        }

        btEnterApp = (Button) viewThree.findViewById(R.id.btEnterApp);
        btEnterApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行判断,是否已经登录,登录进入主页,否则进入登录页面
                intent = new Intent(GuideActivity.this, LoginActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("cvr", cvr);
                intent.putExtras(b);
                startActivity(intent);
                GuideActivity.this.finish();
            }
        });
    }

    /**
     * 显示版本
     */
    private void versionShow() {
        dialog = new Dialog(GuideActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_check_version, null);
        tvVersionCancel = (TextView) layout.findViewById(R.id.tvVersionCancel);
        tvUpdateVersion = (TextView) layout.findViewById(R.id.tvUpdateVersion);
        rlVersion = (RecyclerView) layout.findViewById(R.id.rlVersion);
        initListData();
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(GuideActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvVersionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //确认
        tvUpdateVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(cvr.getDownload_url());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载列表数据
     */
    private void initListData() {
        layoutManager = new LinearLayoutManager(GuideActivity.this);
        rlVersion.setLayoutManager(layoutManager);
        commonAdapter = new CommonAdapter<String>(GuideActivity.this, R.layout.dialog_version, cvr.getFeatures()) {
            @Override
            protected void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.tvVersionInfo, item);
            }
        };
        if (commonAdapter != null) {
            rlVersion.setAdapter(commonAdapter);
        }
    }

    @OnClick(R.id.tvSkip)
    public void skip() {
        //进行判断,是否已经登录,登录进入主页,否则进入登录页面
        intent = new Intent(GuideActivity.this, LoginActivity.class);
//        Bundle b = new Bundle();
//        b.putSerializable("cvr", cvr);
//        intent.putExtras(b);
        startActivity(intent);
        GuideActivity.this.finish();
    }
}
