package com.tch.zx.activity.personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.mine.GiveOrDemandAdapter;
import com.tch.zx.http.bean.result.UserInfoResult;
import com.tch.zx.util.FullyLinearLayoutManager;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息详细内容
 */
public class PersonalContentActivity extends BaseActivity {

    /**
     * 我能提供的列表
     */
    @BindView(R.id.rv_personal_give_info)
    RecyclerView rvPersonalGiveInfo;
    /**
     * 我的需求的列表
     */
    @BindView(R.id.rv_personal_demand_info)
    RecyclerView rvPersonalDemandInfo;
    /**
     * 关注
     */
    @BindView(R.id.iv_personal_if_attention)
    ImageView ivPersonalIfAttention;
    /**
     * 已关注
     */
    @BindView(R.id.tv_personal_has_attention)
    TextView tvPersonalHasAttention;
    @BindView(R.id.tvPersonAddFriend)
    TextView tvPersonAddFriend;
    @BindView(R.id.civPersonPhoto)
    CircleImageView civPersonPhoto;
    @BindView(R.id.tvPersonName)
    TextView tvPersonName;
    @BindView(R.id.ivPersonSex)
    ImageView ivPersonSex;
    @BindView(R.id.tvPersonLineId)
    TextView tvPersonLineId;
    @BindView(R.id.tvPersonRemarks)
    TextView tvPersonRemarks;
    @BindView(R.id.tvPersonSign)
    TextView tvPersonSign;
    @BindView(R.id.tvPersonCompany)
    TextView tvPersonCompany;
    @BindView(R.id.tvPersonCompanyPosition)
    TextView tvPersonCompanyPosition;
    @BindView(R.id.tvPersonArea)
    TextView tvPersonArea;

    /**
     * 列表适配器
     */
    private GiveOrDemandAdapter giveOrDemandAdapter;

    private UserInfoResult.ResponseObjectBean userInfo;
    private String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_personal_content);
        ButterKnife.bind(this);

        initView();


    }

    private void initView() {
        if (getIntent().getSerializableExtra("userInfo") != null) {
            userInfo = ((UserInfoResult) getIntent().getSerializableExtra("userInfo")).getResponseObject();
            initUserData();
        }
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
        }

        setGiveData();
        setDemandData();
    }

    /**
     * 加载用户信息
     */
    private void initUserData() {
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                civPersonPhoto.setImageBitmap(resource);
            }
        };
        Glide.with(PersonalContentActivity.this).asBitmap().load(userInfo.getUser_picture()).into(target);
        tvPersonName.setText(userInfo.getName());
        if (userInfo.getSex().equals("1")) {
            ivPersonSex.setImageResource(R.mipmap.boy_icon);
        } else {
            ivPersonSex.setImageResource(R.mipmap.girl_icon);
        }
        tvPersonLineId.setText(userInfo.getApp_user_id());
        tvPersonRemarks.setText("");//备注
        tvPersonSign.setText(userInfo.getUser_introduce());
        tvPersonCompany.setText(userInfo.getCompany_name());
        tvPersonCompanyPosition.setText(userInfo.getCompany_position());
        tvPersonArea.setText(userInfo.getAdr_province() + " " + userInfo.getAdr_city() + " " + userInfo.getAdr_county());

        //是否是好友
        if (userInfo.isIs_friend()) {
            tvPersonAddFriend.setText("发送消息");
        } else {
            tvPersonAddFriend.setText("添加好友");
            if (!activity.equals("")) {
                //被接受
            } else {
                //接受
            }
        }
    }

    /**
     * 加载我能提供的列表信息
     */
    private void setGiveData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add("");
        }

        giveOrDemandAdapter = new GiveOrDemandAdapter(this, list);
        rvPersonalGiveInfo.setLayoutManager(new FullyLinearLayoutManager(this));
        //设置分割线
        rvPersonalGiveInfo.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 0, true));
        rvPersonalGiveInfo.setAdapter(giveOrDemandAdapter);
        rvPersonalGiveInfo.setNestedScrollingEnabled(false);
    }

    /**
     * 加载我的需求的列表信息
     */
    private void setDemandData() {
        List<String> list1 = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list1.add("");
        }

        giveOrDemandAdapter = new GiveOrDemandAdapter(this, list1);
        rvPersonalDemandInfo.setLayoutManager(new FullyLinearLayoutManager(this));
        //设置分割线
        rvPersonalDemandInfo.addItemDecoration(new RecyclerViewDecoration(this, "#EAEAEA", 0, true));
        rvPersonalDemandInfo.setAdapter(giveOrDemandAdapter);
    }

    /**
     * 备注
     */
    @OnClick(R.id.rl_remarks_personal)
    public void intentRemarks() {
        Intent intent = new Intent(this, RemarksActivity.class);
        startActivity(intent);
    }

    /**
     * 关注
     */
    @OnClick(R.id.iv_personal_if_attention)
    public void ifAttention() {
        ivPersonalIfAttention.setVisibility(View.GONE);
        tvPersonalHasAttention.setVisibility(View.VISIBLE);
    }

    /**
     * 添加好友
     */
    @OnClick(R.id.tvPersonAddFriend)
    public void personAddFriend() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_return_personal_context)
    public void returnPersonalContext() {
        this.finish();
    }
}
