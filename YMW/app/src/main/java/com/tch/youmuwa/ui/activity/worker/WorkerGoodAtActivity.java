package com.tch.youmuwa.ui.activity.worker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.youmuwa.R;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人擅长
 */
public class WorkerGoodAtActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.etWorkerGoodAtContent)
    EditText etWorkerGoodAtContent;
    @BindView(R.id.tvWorkerGoodAtNumber)
    TextView tvWorkerGoodAtNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_good_at);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("个人擅长");
        if (getIntent().getStringExtra("good") != null) {
            etWorkerGoodAtContent.setText(getIntent().getStringExtra("good"));
        }
        etWorkerGoodAtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etWorkerGoodAtContent.length() <= 500) {
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                tvWorkerGoodAtNumber.setText(String.valueOf(etWorkerGoodAtContent.length()));
            }
        }
    };

    /**
     * 确定
     */
    @OnClick(R.id.btGoodAtDetermine)
    public void goodAtDetermine() {
        if (!TextUtils.isEmpty(etWorkerGoodAtContent.getText().toString())) {
            if (etWorkerGoodAtContent.getText().toString().length() >= 10) {
                Intent intent = new Intent();
                intent.putExtra("workerGoodAtContent", etWorkerGoodAtContent.getText().toString());
                setResult(RESULT_OK, intent);
                WorkerGoodAtActivity.this.finish();
            } else {
                Toast.makeText(WorkerGoodAtActivity.this, "不能低于10个字符！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWorkerGoodAt() {
        WorkerGoodAtActivity.this.finish();
    }
}
