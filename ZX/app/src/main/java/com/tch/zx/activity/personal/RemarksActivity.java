package com.tch.zx.activity.personal;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备注
 */
public class RemarksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_remarks);
        ButterKnife.bind(this);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_remark)
    public void returnRemark(){
        this.finish();
    }

    /**
     * 保存
     */
    @OnClick(R.id.tv_remark_save)
    public void saveRemark() {

    }
}
