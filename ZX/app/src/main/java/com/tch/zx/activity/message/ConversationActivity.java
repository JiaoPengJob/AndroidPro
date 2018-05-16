package com.tch.zx.activity.message;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.bean.TestBean;
import com.tch.zx.http.bean.result.UserInfoResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 聊天页面
 */
public class ConversationActivity extends BaseActivity {

    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    private UserInfoResult.ResponseObjectBean userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if (getIntent().getSerializableExtra("userInfo") != null) {
            userInfo = (UserInfoResult.ResponseObjectBean) getIntent().getSerializableExtra("userInfo");
            tv_title_top_all.setText(userInfo.getName());
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userInfo.getApp_user_id(), userInfo.getName(), Uri.parse(userInfo.getUser_picture())));
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnConversation() {
        this.finish();
    }
}
