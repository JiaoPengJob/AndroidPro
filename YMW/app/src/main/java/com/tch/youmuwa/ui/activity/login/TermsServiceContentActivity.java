package com.tch.youmuwa.ui.activity.login;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.GetProvisionResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务条款
 */
public class TermsServiceContentActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.tvTermsServiceText)
    TextView tvTermsServiceText;//文本显示信息
    @BindView(R.id.title)
    TextView title;
    /**
     * 设置的参数
     */
    private PresenterImpl<Object> presenter;//接口
    private GetProvisionResult provisionResult;//服务条款的返回结果
    private String activity;//标识
    private SVProgressHUD mSVProgressHUD;//加载显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_service_content);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("GSettingsActivity")) {
                title.setText("用户协议");
                clientHttp();
            } else if (activity.equals("RegisterActivity")) {
                title.setText("服务条款");
                clientHttp();
            } else if (activity.equals("AboutUs")) {
                title.setText("关于我们");
                aboutUs();
            } else if (activity.equals("third")) {
                title.setText("三方协议");
                getAgreement();
            }
        }
    }

    /**
     * 获取服务条款
     */
    private void clientHttp() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(TermsServiceContentActivity.this);
        presenter.onCreate();
        presenter.getprovision();
        presenter.attachView(getProvisionView);
    }

    private ClientBaseView<Object> getProvisionView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(TermsServiceContentActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                provisionResult = (GetProvisionResult) GsonUtil.parseJson(baseBean.getData(), GetProvisionResult.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent()));
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "getProvisionView--" + result);
        }
    };

    /**
     * 获取用户协议
     */
    private void getAgreement() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(TermsServiceContentActivity.this);
        presenter.onCreate();
        presenter.getagreement();
        presenter.attachView(getAgreementView);
    }

    private ClientBaseView<Object> getAgreementView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(TermsServiceContentActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                provisionResult = (GetProvisionResult) GsonUtil.parseJson(baseBean.getData(), GetProvisionResult.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent()));
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "getAgreementView--" + result);
        }
    };

    /**
     * 关于我们
     */
    private void aboutUs() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(TermsServiceContentActivity.this);
        presenter.onCreate();
        presenter.about();
        presenter.attachView(aboutView);
    }

    private ClientBaseView<Object> aboutView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(TermsServiceContentActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                provisionResult = (GetProvisionResult) GsonUtil.parseJson(baseBean.getData(), GetProvisionResult.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvTermsServiceText.setText(Html.fromHtml(provisionResult.getContent()));
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "aboutView--" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatTermsService() {
        TermsServiceContentActivity.this.finish();
    }

    /**
     * 监听后退物理按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bl = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                if (presenter != null) {
                    presenter.onStop();
                }
                bl = false;
            } else {
                TermsServiceContentActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }
}
