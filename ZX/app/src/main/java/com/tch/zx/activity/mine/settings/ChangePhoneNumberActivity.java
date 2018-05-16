package com.tch.zx.activity.mine.settings;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更换手机号/第一步
 */
public class ChangePhoneNumberActivity extends BaseActivity {
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.etExPhonePwdFirst)
    EditText etExPhonePwdFirst;
    @BindView(R.id.ivExPhonePwdFirstEye)
    ImageView ivExPhonePwdFirstEye;

    private UserBeanDao userBeanDao;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_change_phone_number);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("更换手机号");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
    }

    private boolean isVisiblePwd;

    /**
     * 密码是否可见
     */
    @OnClick(R.id.ivExPhonePwdFirstEye)
    public void exPhonePwdFirstEye() {
        if (isVisiblePwd) {
            //不可见图标设置为可见图标
            ivExPhonePwdFirstEye.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
            //密码设置为不可见状态
            etExPhonePwdFirst.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = false;
        } else {
            //可见图标设置为不可见图标
            ivExPhonePwdFirstEye.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
            //密码设置为可见状态
            etExPhonePwdFirst.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //转换是否可见的状态
            isVisiblePwd = true;
        }
    }

    /**
     * 下一步
     */
    @OnClick(R.id.tv_next_exchange_phone)
    public void intentNextChangePhone() {
        if (!TextUtils.isEmpty(etExPhonePwdFirst.getText().toString())) {
            validatePassword();
        } else {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private BasePresenter presenter;

    /**
     * 验证密码
     */
    private void validatePassword() {
        presenter = new BasePresenter<Object>(this);
        presenter.onCreate();
        presenter.attachView(validatePasswordView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPassword", etExPhonePwdFirst.getText().toString());
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.validatePassword(data);
    }

    private BaseView<Object> validatePasswordView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Intent intent = new Intent(ChangePhoneNumberActivity.this, ExchangePhoneNumberActivity.class);
                startActivity(intent);
                ChangePhoneNumberActivity.this.finish();
            } else {
                Toast.makeText(ChangePhoneNumberActivity.this, baseResultBean.getResult().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "validatePasswordView接口错误" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnChangePhone() {
        this.finish();
    }
}
