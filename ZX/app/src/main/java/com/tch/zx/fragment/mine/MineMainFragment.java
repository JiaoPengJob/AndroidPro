package com.tch.zx.fragment.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.activity.mine.CollectionMineActivity;
import com.tch.zx.activity.mine.CompanyInfoMainActivity;
import com.tch.zx.activity.mine.DynamicMineActivity;
import com.tch.zx.activity.mine.ExchangeActivity;
import com.tch.zx.activity.mine.MineInfoActivity;
import com.tch.zx.activity.mine.PurchaseHistoryActivity;
import com.tch.zx.activity.mine.SettingsActivity;
import com.tch.zx.activity.mine.ToBeMineActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.util.ConstantData;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.util.SharedPrefsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tch.zx.application.RxRetrofitApp.getApplication;

/**
 * 我的主页
 */

public class MineMainFragment extends Fragment {
    /**
     * 头像
     */
    @BindView(R.id.civ_photo_mine)
    CircleImageView civ_photo_mine;
    /**
     * 姓名
     */
    @BindView(R.id.tvMineName)
    TextView tvMineName;
    /**
     * 职位
     */
    @BindView(R.id.tvMinePosition)
    TextView tvMinePosition;
    /**
     * 个人介绍
     */
    @BindView(R.id.tvMineIntroduce)
    TextView tvMineIntroduce;

    /**
     * Fragment父布局
     */
    private View viewRoot;
    /**
     * 跳转
     */
    private Intent intent;
    /**
     * 用户数据dao
     */
    private UserBeanDao userBeanDao;
    /**
     * 数据库session
     */
    private DaoSession daoSession;
    /**
     * 用户对象
     */
    private UserBean userBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_mine_main, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initMineInfo();
        return viewRoot;
    }

    /**
     * 加载用户信息
     */
    private void initMineInfo() {
        if (!SharedPrefsUtil.getValue(getContext(), "loginType", "").equals(ConstantData.LOGIN_TYPE_CASUALSEE)) {
            daoSession = ((MyApplication) getApplication()).getDaoSession();
            userBeanDao = daoSession.getUserBeanDao();
            userBean = userBeanDao.loadAll().get(0);
            if (userBean != null) {
                SimpleTarget target = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        civ_photo_mine.setImageBitmap(resource);
                    }
                };
                Glide.with(getContext()).asBitmap().load(userBean.getAppUserPic()).into(target);
                tvMineName.setText(userBean.getAppUserName());
                tvMinePosition.setText(userBean.getCompanyPosition());
                tvMineIntroduce.setText(userBean.getAppUserIntroduce());
            }
        }
    }

    /**
     * 编辑
     */
    @OnClick(R.id.iv_edit_my_info)
    public void editMyInfo() {
        intent = new Intent(getContext(), MineInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 我的公司
     */
    @OnClick(R.id.rl_intent_mine_company)
    public void intentMyCompany() {
        intent = new Intent(getContext(), CompanyInfoMainActivity.class);
        intent.putExtra("activity", "Mine");
        startActivity(intent);
    }

    /**
     * 我的收藏
     */
    @OnClick(R.id.rl_intent_mine_collection)
    public void intentCollection() {
        intent = new Intent(getContext(), CollectionMineActivity.class);
        startActivity(intent);
    }


    /**
     * 我的动态
     */
    @OnClick(R.id.rl_intent_mine_dynamic)
    public void intentDynamic() {
        intent = new Intent(getContext(), DynamicMineActivity.class);
        intent.putExtra("activity", "Mine");
        intent.putExtra("id", userBean.getAppUserId());
        startActivity(intent);
    }


    /**
     * 购买记录
     */
    @OnClick(R.id.rl_intent_mine_purchase_history)
    public void intentPurchaseHistory() {
        intent = new Intent(getContext(), PurchaseHistoryActivity.class);
        startActivity(intent);
    }


    /**
     * 我要成为
     */
    @OnClick(R.id.rl_intent_mine_want_to_be)
    public void intentToBe() {
        intent = new Intent(getContext(), ToBeMineActivity.class);
        startActivity(intent);
    }


    /**
     * 兑换码
     */
    @OnClick(R.id.rl_intent_mine_exchange)
    public void intentExchange() {
        intent = new Intent(getContext(), ExchangeActivity.class);
        startActivity(intent);
    }


    /**
     * 设置
     */
    @OnClick(R.id.rl_intent_mine_settings)
    public void intentSettings() {
        intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }
}
