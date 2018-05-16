package com.tch.zx.activity.mine.settings;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.tch.zx.view.SwitchView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 加好友设置
 */
public class AddFriendSettingActivity extends BaseActivity {
    /**
     * 标题内容
     */
    @BindView(R.id.tv_title_top_all)
    TextView tv_title_top_all;

    /**
     * 允许任何人的符号
     */
    @BindView(R.id.iv_allow_anyone)
    ImageView iv_allow_anyone;
    /**
     * 需要验证的符号
     */
    @BindView(R.id.iv_need_verification)
    ImageView iv_need_verification;
    /**
     * 只允许加关注
     */
    @BindView(R.id.iv_only_attention)
    ImageView iv_only_attention;

    /**
     * 只允许大咖加好友
     */
    @BindView(R.id.sv_only_cast_add_friend)
    SwitchView sv_only_cast_add_friend;

    /**
     * 是否允许任何人/默认为是/只允许加关注
     */
    private int isAnyOne = 0;
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
        setContentView(R.layout.activity_add_friend_setting);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        tv_title_top_all.setText("加好友设置");

        if (userBeanDao.loadAll().get(0).getAdd_frid_set().equals("0")) {
            iv_allow_anyone.setVisibility(View.VISIBLE);
            iv_need_verification.setVisibility(View.GONE);
            iv_only_attention.setVisibility(View.GONE);
        } else if (userBeanDao.loadAll().get(0).getAdd_frid_set().equals("1")) {
            iv_allow_anyone.setVisibility(View.GONE);
            iv_need_verification.setVisibility(View.VISIBLE);
            iv_only_attention.setVisibility(View.GONE);
        } else if (userBeanDao.loadAll().get(0).getAdd_frid_set().equals("2")) {
            iv_allow_anyone.setVisibility(View.GONE);
            iv_need_verification.setVisibility(View.GONE);
            iv_only_attention.setVisibility(View.VISIBLE);
        }

        if (userBeanDao.loadAll().get(0).getOnly_dk().equals("0")) {
            sv_only_cast_add_friend.setState(false);
        } else {
            sv_only_cast_add_friend.setState(true);
        }

        //只允许大咖加好友
        sv_only_cast_add_friend.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                sv_only_cast_add_friend.setState(true);
                addFriendSetDown(1);
            }

            @Override
            public void toggleToOff() {
                sv_only_cast_add_friend.setState(false);
                addFriendSetDown(0);
            }
        });
    }

    /**
     * 允许任何人
     */
    @OnClick(R.id.rl_allow_anyone)
    public void allowAnyone() {
        iv_allow_anyone.setVisibility(View.VISIBLE);
        iv_need_verification.setVisibility(View.GONE);
        iv_only_attention.setVisibility(View.GONE);
        addFriendSet(0);
    }

    /**
     * 需要验证
     */
    @OnClick(R.id.rl_need_verification)
    public void needVerification() {
        iv_allow_anyone.setVisibility(View.GONE);
        iv_need_verification.setVisibility(View.VISIBLE);
        iv_only_attention.setVisibility(View.GONE);
        addFriendSet(1);
    }

    /**
     * 只允许加关注
     */
    @OnClick(R.id.rl_only_attention)
    public void onlyAttention() {
        iv_allow_anyone.setVisibility(View.GONE);
        iv_need_verification.setVisibility(View.GONE);
        iv_only_attention.setVisibility(View.VISIBLE);
        addFriendSet(2);
    }

    /**
     * 上面三个修改
     */
    private void addFriendSet(int isAnyOne) {
        presenter = new BasePresenter<Object>(AddFriendSettingActivity.this);
        presenter.onCreate();
        presenter.attachView(addFriendSetView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("addFriendSet", isAnyOne);

        String data = HelperUtil.getParameter(map);
        presenter.addFriendSet(data);
    }

    /**
     * 大咖修改
     */
    private void addFriendSetDown(int isSel) {
        presenter = new BasePresenter<Object>(AddFriendSettingActivity.this);
        presenter.onCreate();
        presenter.attachView(addFriendSetView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userBeanDao.loadAll().get(0).getAppUserId());
        map.put("onlyDk", isSel);

        String data = HelperUtil.getParameter(map);
        presenter.addFriendSet(data);
    }

    private BaseView<Object> addFriendSetView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                Toast.makeText(AddFriendSettingActivity.this, baseResultBean.getResult().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "addFriendSetView接口错误" + result);
        }
    };


    /**
     * 返回
     */
    @OnClick(R.id.ll_return_back_top_all)
    public void returnAddFriendSetitng() {
        this.finish();
    }
}
