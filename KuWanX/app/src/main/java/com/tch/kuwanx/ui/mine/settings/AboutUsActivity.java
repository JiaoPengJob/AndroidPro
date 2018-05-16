package com.tch.kuwanx.ui.mine.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvOfficialPhone)
    TextView tvOfficialPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("关于我们");
        try {
            tvVersion.setText(Utils.getVersionName(AboutUsActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打官方电话
     */
    @OnClick(R.id.tvOfficialPhone)
    public void officialPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvOfficialPhone.getText().toString()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void aboutUsBack() {
        AboutUsActivity.this.finish();
    }
}
