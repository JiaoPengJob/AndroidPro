package com.tch.zx.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
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

/***
 * 成为大咖
 */
public class ToBeBigCastActivity extends BaseActivity {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.etBigCastName)
    EditText etBigCastName;
    @BindView(R.id.etBigCastCompanyName)
    EditText etBigCastCompanyName;
    @BindView(R.id.etBigCastPosition)
    EditText etBigCastPosition;
    @BindView(R.id.etBigCastPhone)
    EditText etBigCastPhone;
    @BindView(R.id.etBigCastEmail)
    EditText etBigCastEmail;

    private BasePresenter<Object> presenter;
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
        setContentView(R.layout.activity_to_be_big_cast);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("成为大咖");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
    }

    /**
     * 确定发布
     */
    @OnClick(R.id.tvBigCastSubmit)
    public void bigCastSubmit() {
        if (!TextUtils.isEmpty(etBigCastName.getText().toString())
                && !TextUtils.isEmpty(etBigCastCompanyName.getText().toString())
                && !TextUtils.isEmpty(etBigCastPosition.getText().toString())
                && !TextUtils.isEmpty(etBigCastPhone.getText().toString())
                && !TextUtils.isEmpty(etBigCastEmail.getText().toString())) {
            if (HelperUtil.isMobileNO(etBigCastPhone.getText().toString())) {
                if (HelperUtil.isEmail(etBigCastEmail.getText().toString())) {
                    insertApplyInfo();
                } else {
                    Toast.makeText(ToBeBigCastActivity.this, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ToBeBigCastActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertApplyInfo() {
        presenter = new BasePresenter<Object>(ToBeBigCastActivity.this);
        presenter.onCreate();
        presenter.attachView(insertApplyInfoView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("applyStatus", "2");
        map.put("appUserName", etBigCastName.getText().toString());
        map.put("companyPosition", etBigCastPosition.getText().toString());
        map.put("companyName", etBigCastCompanyName.getText().toString());
        map.put("appUserPhone", etBigCastPhone.getText().toString());
        map.put("appUserEmail", etBigCastEmail.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.insertApplyInfo(data);
    }

    private BaseView<Object> insertApplyInfoView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            Toast.makeText(ToBeBigCastActivity.this, baseResultBean.getResult().toString(), Toast.LENGTH_SHORT).show();
            if (baseResultBean.getRet().equals("1")) {
                ToBeBigCastActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "insertApplyInfoView接口错误" + result);
        }
    };

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnToBeCast() {
        ToBeBigCastActivity.this.finish();
    }
}
