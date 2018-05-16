package com.tch.zx.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑简介
 */
public class IntroductionEditingActivity extends BaseActivity {

    @BindView(R.id.etIntroductionEditing)
    EditText etIntroductionEditing;

    private int intentType;
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_introduction_editing);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        intentType = getIntent().getIntExtra("intentType", 0);
        if (getIntent().getStringExtra("content") != null) {
            content = getIntent().getStringExtra("content");
        }
        if (intentType == 13) {
            etIntroductionEditing.setHint("输入公司简介");
            if (!content.equals("")) {
                etIntroductionEditing.setText(content);
            }
        }
    }

    /**
     * 完成
     */
    @OnClick(R.id.tv_finish_done)
    public void finishIntroEditing() {
        if (!TextUtils.isEmpty(etIntroductionEditing.getText().toString())) {
            Intent intent = new Intent();
            if (intentType == 13) {
                intent.putExtra("content", etIntroductionEditing.getText().toString());
                setResult(13, intent);
            }
            IntroductionEditingActivity.this.finish();
        } else {
            Toast.makeText(IntroductionEditingActivity.this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_introduction_editing)
    public void returnIntroEditing() {
        Intent intent = new Intent();
        if (intentType == 13) {
            intent.putExtra("content", content );
            setResult(13, intent);
        }
        IntroductionEditingActivity.this.finish();
    }

}
