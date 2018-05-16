package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 修改昵称
 */
public class UpdateNameActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.etUpdateNickName)
    EditText etUpdateNickName;

    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("修改昵称");
        if (getIntent().getStringExtra("nickname") != null) {
            nickname = getIntent().getStringExtra("nickname");
            etUpdateNickName.setText(nickname);
        }
        etUpdateNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";  //只能输入字母或数字
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(editable.toString());
                String str = m.replaceAll("").trim();    //删掉不是字母或数字的字符
                if (!editable.toString().equals(str)) {
                    etUpdateNickName.setText(str);  //设置EditText的字符
                    etUpdateNickName.setSelection(str.length()); //因为删除了字符，要重写设置新的光标所在位置
                }
            }
        });
    }

    /**
     * 确认修改
     */
    @OnClick(R.id.btUpdateNickName)
    public void updateNickName() {
        if (!TextUtils.isEmpty(etUpdateNickName.getText().toString())) {
            if (etUpdateNickName.getText().toString().length() > 3) {
                Intent intent = new Intent();
                intent.putExtra("nickname", etUpdateNickName.getText().toString());
                UpdateNameActivity.this.setResult(0, intent);
                UpdateNameActivity.this.finish();
            } else {
                Toasty.warning(UpdateNameActivity.this, "用户名为4-10位字符！", Toast.LENGTH_SHORT, false).show();
            }
        } else {
            Toasty.warning(UpdateNameActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT, false).show();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void updateNameBack() {
        Intent intent = new Intent();
        intent.putExtra("nickname", nickname);
        UpdateNameActivity.this.setResult(0, intent);
        UpdateNameActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("nickname", nickname);
            UpdateNameActivity.this.setResult(0, intent);
            UpdateNameActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
