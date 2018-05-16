package com.tch.zx.activity.personal;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.zx.R;
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
 * 好友验证
 */
public class FriendVerificationActivity extends AppCompatActivity {

    @BindView(R.id.etFriendVerContent)
    EditText etFriendVerContent;
    @BindView(R.id.tvFriendVerNumber)
    TextView tvFriendVerNumber;

    private String addAppUserId = "";
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_friend_verification);
        ButterKnife.bind(this);
        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra("addAppUserId") != null) {
            addAppUserId = getIntent().getStringExtra("addAppUserId");
        }
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();

        etFriendVerContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etFriendVerContent.length() <= 60) {
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
            tvFriendVerNumber.setText(String.valueOf(60 - etFriendVerContent.length()));
        }
    };

    /*--------------点击事件-----------------------------*/

    /**
     * 返回
     */
    @OnClick(R.id.llReturnFriendVer)
    public void returnFriendVer() {
        FriendVerificationActivity.this.finish();
    }

    /**
     * 发送
     */
    @OnClick(R.id.llFriendVerSend)
    public void friendVerSend() {
        if (!TextUtils.isEmpty(etFriendVerContent.getText().toString())) {
            getUserInfo();
        } else {
            Toast.makeText(FriendVerificationActivity.this, "验证信息不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfo() {
        presenter = new BasePresenter<Object>(FriendVerificationActivity.this);
        presenter.onCreate();
        presenter.attachView(addFriendView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("addAppUserId", addAppUserId);
        map.put("verification_message", etFriendVerContent.getText().toString());

        String data = HelperUtil.getParameter(map);
        presenter.addFriend(data);
    }

    private BaseView<Object> addFriendView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Toast.makeText(FriendVerificationActivity.this, "申请成功！", Toast.LENGTH_SHORT).show();
                FriendVerificationActivity.this.finish();
            } else {
                Toast.makeText(FriendVerificationActivity.this, "申请失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "addFriendView接口错误" + result);
        }
    };

}
