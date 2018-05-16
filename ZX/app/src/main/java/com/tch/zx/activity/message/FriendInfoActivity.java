package com.tch.zx.activity.message;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.activity.mine.CompanyInfoMainActivity;
import com.tch.zx.activity.mine.DynamicMineActivity;
import com.tch.zx.activity.personal.FriendVerificationActivity;
import com.tch.zx.activity.personal.PersonalContentActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.SearchFriendResult;
import com.tch.zx.http.bean.result.UserInfoResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.GsonUtil;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 好友详细资料
 */
public class FriendInfoActivity extends AppCompatActivity {

    @BindView(R.id.civ_user_info_photo)
    CircleImageView civUserInfoPhoto;
    @BindView(R.id.tvUserInfoName)
    TextView tvUserInfoName;
    @BindView(R.id.tvUserInfoVer)
    TextView tvUserInfoVer;
    @BindView(R.id.ivCompanyPhoto)
    ImageView ivCompanyPhoto;
    @BindView(R.id.tvUserCompanyTitle)
    TextView tvUserCompanyTitle;
    @BindView(R.id.tvCompanyContent)
    TextView tvCompanyContent;
    @BindView(R.id.tvFriendInfoAdd)
    TextView tvFriendInfoAdd;
    @BindView(R.id.rlClassInfo)
    RelativeLayout rlClassInfo;
    @BindView(R.id.llFriendInfoDynamic)
    LinearLayout llFriendInfoDynamic;

    private String userId = "";
    private BasePresenter presenter;
    private UserInfoResult userInfoResult;
    private Intent intent;
    private String activity = "";
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
        setContentView(R.layout.activity_friend_info);
        ButterKnife.bind(this);
        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initView();
    }

    private void initView() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        if (getIntent().getStringExtra("userId") != null) {
            userId = getIntent().getStringExtra("userId");
        }
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("Other")) {
                rlClassInfo.setVisibility(View.GONE);
            }
        }
        getUserInfoByAppUserId();
    }

    private void getUserInfoByAppUserId() {
        presenter = new BasePresenter<Object>(FriendInfoActivity.this);
        presenter.onCreate();
        presenter.attachView(getUserInfoByAppUserIdView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", userId);

        String data = HelperUtil.getParameter(map);
        presenter.getUserInfoByAppUserId(data);
    }

    private BaseView<Object> getUserInfoByAppUserIdView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                userInfoResult = (UserInfoResult) GsonUtil.parseJson(baseResultBean.getResult(), UserInfoResult.class);
                setUserInfoData();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "getUserInfoByAppUserIdView接口错误" + result);
        }
    };

    /**
     * 加载用户信息
     */
    private void setUserInfoData() {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                civUserInfoPhoto.setImageBitmap(resource);
            }
        };
        Glide.with(FriendInfoActivity.this).asBitmap().load(userInfoResult.getResponseObject().getUser_picture()).into(target);
        tvUserInfoName.setText(userInfoResult.getResponseObject().getName());
        tvUserInfoVer.setText(userInfoResult.getResponseObject().getUser_introduce());
        if (userInfoResult.getResponseObject().isIs_friend()) {
            tvFriendInfoAdd.setText("发送消息");
        } else {
            tvFriendInfoAdd.setText("添加好友");
            if (!activity.equals("NewFridensActivity")) {
                //被接受
            } else {
                //接受
                processAppyFriendRequest();
            }
        }
        if (userInfoResult.getResponseObject().getDynamic_content() != null
                && !userInfoResult.getResponseObject().getDynamic_content().equals("")) {
            Glide.with(FriendInfoActivity.this)
                    .load(userInfoResult.getResponseObject().getContent_picture())
                    .into(ivCompanyPhoto);
            tvCompanyContent.setText(userInfoResult.getResponseObject().getDynamic_content());
        } else {
            llFriendInfoDynamic.setVisibility(View.GONE);
        }
    }

    private void processAppyFriendRequest() {
        presenter = new BasePresenter<Object>(FriendInfoActivity.this);
        presenter.onCreate();
        presenter.attachView(processAppyFriendView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appyId", userId);

        String data = HelperUtil.getParameter(map);
        presenter.processAppyFriendRequest(data);
    }

    private BaseView<Object> processAppyFriendView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                tvFriendInfoAdd.setText("发送消息");
                Toast.makeText(FriendInfoActivity.this, "处理成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FriendInfoActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "processAppyFriendView接口错误" + result);
        }
    };

    /*-------点击事件----------*/

    /**
     * 个人信息
     */
    @OnClick(R.id.rlPersonInfo)
    public void personInfoIntent() {
        intent = new Intent(this, PersonalContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfoResult);
        bundle.putString("activity", activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 公司信息
     */
    @OnClick(R.id.rlCompanyInfo)
    public void companyInfoIntent() {
        intent = new Intent(this, CompanyInfoMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfoResult);
        bundle.putString("activity", activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 课程信息
     */
    @OnClick(R.id.rlClassInfo)
    public void classInfo() {

    }

    /**
     * 动态点击
     */
    @OnClick(R.id.llFriendInfoDynamic)
    public void friendInfoDynamic() {
        intent = new Intent(this, DynamicMineActivity.class);
        intent.putExtra("activity", "Friend");
        intent.putExtra("id", userInfoResult.getResponseObject().getApp_user_id());
        startActivity(intent);
    }

    /**
     * 添加好友或发送信息
     */
    @OnClick(R.id.tvFriendInfoAdd)
    public void friendInfoAdd() {
        if (userInfoResult != null) {
            if (userInfoResult.getResponseObject().isIs_friend()) {
//            tvFriendInfoAdd.setText("发送消息");
            } else {
//            tvFriendInfoAdd.setText("添加好友");
//                if (activity.equals("NewFridensActivity")) {
//                    //被接受
//                    processAppyFriendRequest();
//                } else {
//                    //接受
//                    intent = new Intent(FriendInfoActivity.this, FriendVerificationActivity.class);
//                    intent.putExtra("addAppUserId", userId);
//                    startActivity(intent);
//                }
                sendPrivateMessages();
            }
        }
    }

    /**
     * 发送私聊消息
     */
    private void sendPrivateMessages() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String id) {
                return new UserInfo(userInfoResult.getResponseObject().getApp_user_id(), userBeanDao.loadAll().get(0).getAppUserName(), Uri.parse(userBeanDao.loadAll().get(0).getAppUserPic()));
            }
        }, true);
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userBeanDao.loadAll().get(0).getAppUserId(), userBeanDao.loadAll().get(0).getAppUserName(), Uri.parse(userBeanDao.loadAll().get(0).getAppUserPic())));
//        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userInfoResult.getResponseObject().getApp_user_id(), userInfoResult.getResponseObject().getName(), Uri.parse(userInfoResult.getResponseObject().getUser_picture())));
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        Uri uri = Uri.parse("rong://" + "com.tch.zx.activity.message").buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", userInfoResult.getResponseObject().getApp_user_id())
                .appendQueryParameter("title", userInfoResult.getResponseObject().getName())
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfoResult.getResponseObject());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 后退
     */
    @OnClick(R.id.iv_return_back_fi)
    public void returnBack() {
        FriendInfoActivity.this.finish();
    }
}
