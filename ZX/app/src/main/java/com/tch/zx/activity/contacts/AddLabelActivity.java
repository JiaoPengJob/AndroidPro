package com.tch.zx.activity.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加子标签信息
 */
public class AddLabelActivity extends BaseActivity {

    @BindView(R.id.tvAddLabelTitle)
    TextView tvAddLabelTitle;
    @BindView(R.id.tvAddLabelInfo)
    TextView tvAddLabelInfo;
    @BindView(R.id.tvAddLabelLeft)
    TextView tvAddLabelLeft;
    @BindView(R.id.etAddLAbelInput)
    EditText etAddLAbelInput;

    private int intentType;
    private Intent intent;
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_label);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        intentType = getIntent().getIntExtra("intentType", 0);
        if (getIntent().getStringExtra("content") != null) {
            content = getIntent().getStringExtra("content");
        }
        switch (intentType) {
            case 10:
                tvAddLabelTitle.setText("公司名称");
                tvAddLabelInfo.setText("公司名称");
                tvAddLabelLeft.setText("公司名称");
                etAddLAbelInput.setHint("请输入公司名称");
                if (!content.equals("")) {
                    etAddLAbelInput.setText(content);
                }
                break;
            case 11:
                tvAddLabelTitle.setText("公司地址");
                tvAddLabelInfo.setText("公司地址");
                tvAddLabelLeft.setText("公司地址");
                etAddLAbelInput.setHint("请输入公司地址");
                if (!content.equals("")) {
                    etAddLAbelInput.setText(content);
                }
                break;
        }
    }

    /**
     * 完成
     */
    @OnClick(R.id.tv_finish_add_label)
    public void finishAddLabel() {
        if (!TextUtils.isEmpty(etAddLAbelInput.getText().toString())) {
            intent = new Intent();
            intent.putExtra("content", etAddLAbelInput.getText().toString());
            setResult(intentType, intent);
            AddLabelActivity.this.finish();
        } else {
            Toast.makeText(AddLabelActivity.this, "输入内容不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_add_label)
    public void returnAddLabel() {
        intent = new Intent();
        intent.putExtra("content", content);
        setResult(intentType, intent);
        AddLabelActivity.this.finish();
    }

}
