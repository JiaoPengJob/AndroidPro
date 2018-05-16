package com.tch.zx.activity.mine.settings;

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

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    /**
     * 内容标题
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;
    @BindView(R.id.etFeedBack)
    EditText etFeedBack;

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
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        tv_title_top_all.setText("意见反馈");
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
    }

    private void insertFeedback() {
        presenter = new BasePresenter<Object>(FeedBackActivity.this);
        presenter.onCreate();
        presenter.attachView(insertFeedbackView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("content", etFeedBack.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.insertFeedback(data);
    }

    private BaseView<Object> insertFeedbackView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getRet().equals("1")) {
                Toast.makeText(FeedBackActivity.this, "反馈成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FeedBackActivity.this, "反馈失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "insertFeedbackView接口错误" + result);
        }
    };

    /**
     * 提交
     */
    @OnClick(R.id.tv_sure_submit)
    public void submitContent() {
        if (!TextUtils.isEmpty(etFeedBack.getText().toString())) {
            insertFeedback();
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnFeedBack() {
        this.finish();
    }
}
