package com.tch.zx.activity.personal;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.activity.mine.CompanyInfoMainActivity;
import com.tch.zx.activity.mine.DynamicMineActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友详细资料
 */
public class DetailedInfoActivity extends BaseActivity {

    //跳转页面
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_detailed_info);
        ButterKnife.bind(this);

    }

    /**
     * 个人信息
     */
    @OnClick(R.id.rl_personal_detailed_info)
    public void intentPersonalDetailedInfo() {
        intent = new Intent(this, PersonalContentActivity.class);
        startActivity(intent);
    }


    /**
     * 公司信息
     */
    @OnClick(R.id.rl_personal_detailed_company)
    public void intentCompany() {
        intent = new Intent(this, CompanyInfoMainActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_return_detailed_info)
    public void returnDetailedInfo() {
        this.finish();
    }

    /**
     * 动态点击跳转
     */
    @OnClick({R.id.ll_dynamic_info, R.id.rl_dynamic_info})
    public void intentDynamicInfo() {
        intent = new Intent(this, DynamicMineActivity.class);
        startActivity(intent);
    }
}
