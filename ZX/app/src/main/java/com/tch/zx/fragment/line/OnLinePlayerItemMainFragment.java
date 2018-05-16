package com.tch.zx.fragment.line;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.LiveDetailsResultBean;
import com.tch.zx.http.presenter.ConcernCancelPresenter;
import com.tch.zx.http.presenter.ConcernInsertPresenter;
import com.tch.zx.http.view.ConcernCancelView;
import com.tch.zx.http.view.ConcernInsertView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tch.zx.application.RxRetrofitApp.getApplication;

/**
 * 直播/信息
 */

public class OnLinePlayerItemMainFragment extends Fragment {

    /**
     * Fragment父布局
     */
    private View viewRoot;
    /**
     * 底部立即报名项
     */
    @BindView(R.id.tv_sign_up_now)
    TextView tv_sign_up_now;
    /**
     * 底部选择支付项
     */
    @BindView(R.id.ll_pay_online_player_info)
    LinearLayout ll_pay_online_player_info;
    /**
     * 头像
     */
    @BindView(R.id.civ_user_photo_player)
    CircleImageView civ_user_photo_player;
    /**
     * 标题
     */
    @BindView(R.id.tvLiveName)
    TextView tvLiveName;
    /**
     * 职位
     */
    @BindView(R.id.tvLivePosition)
    TextView tvLivePosition;
    /**
     * 作者名称
     */
    @BindView(R.id.tvLiveUserName)
    TextView tvLiveUserName;
    /**
     * 观看人数
     */
    @BindView(R.id.tvViewNum)
    TextView tvViewNum;
    /**
     * 时间
     */
    @BindView(R.id.tvLiveTime)
    TextView tvLiveTime;
    /**
     * 价格
     */
    @BindView(R.id.tvLiveMoney)
    TextView tvLiveMoney;
    /**
     * 是否关注
     */
    @BindView(R.id.ivIsConcern)
    ImageView ivIsConcern;
    /**
     * 讲师介绍
     */
    @BindView(R.id.tvUserIntroduce)
    TextView tvUserIntroduce;
    /**
     * 课程介绍
     */
    @BindView(R.id.tvLiveIntroduce)
    TextView tvLiveIntroduce;

    private ConcernInsertPresenter concernInsertPresenter;
    private ConcernCancelPresenter cancelPresenter;

    private LiveDetailsResultBean.ResultBean.ResponseObjectBean mliveBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_online_player_class_info, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        if (getArguments().getSerializable("liveBean") != null) {
            mliveBean = (LiveDetailsResultBean.ResultBean.ResponseObjectBean) getArguments().getSerializable("liveBean");
        }

        initView();
        return viewRoot;
    }

    /**
     * 初始化
     */
    public void initView() {
        if (mliveBean != null) {
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    civ_user_photo_player.setImageBitmap(resource);
                }
            };
            if (mliveBean.getAppUserPic() != null && !mliveBean.getAppUserPic().equals("")) {
                Glide.with(getContext()).asBitmap().load(mliveBean.getAppUserPic()).into(target);
            }
            tvLiveName.setText(mliveBean.getLiveName());
            tvLivePosition.setText(mliveBean.getPosition());
            tvLiveUserName.setText(mliveBean.getAppUserName());
            tvViewNum.setText(mliveBean.getLiveViewNum() + "观看");
            tvLiveTime.setText(mliveBean.getLiveTime());
            //是否支付,或者是否免费
            if (mliveBean.getLiveMoney() != 0) {
                if (mliveBean.getConfirmPay() == 0) {
                    tvLiveMoney.setText("￥" + mliveBean.getLiveMoney() + "元");
                } else {
                    tvLiveMoney.setText("已付费");
                }
            } else {
                tvLiveMoney.setText("免费");
            }
            //是否关注
            if (mliveBean.getIsConcern() == 0) {
                ivIsConcern.setImageResource(R.mipmap.add_attention_player);
            } else {
                ivIsConcern.setImageResource(R.mipmap.has_attention);
            }
            tvUserIntroduce.setText(mliveBean.getAppUserIntroduce());
            tvLiveIntroduce.setText(mliveBean.getLiveIntroduce());
        }
    }

    @OnClick(R.id.ivIsConcern)
    public void isConcern() {
        ivIsConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivIsConcern.getDrawable().getCurrent().getConstantState()
                        .equals(getResources().getDrawable(R.mipmap.add_attention_player).getConstantState())) {
                    concernInsert();
                } else {
                    concernCancel();
                }
            }
        });
    }


    /**
     * 关注
     */
    private void concernInsert() {
        concernInsertPresenter = new ConcernInsertPresenter(getContext());
        concernInsertPresenter.onCreate();
        concernInsertPresenter.attachView(concernInsertView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", getAppId());
        map.put("appUserConcern", mliveBean.getAppUserId());

        String data = HelperUtil.getParameter(map);
        concernInsertPresenter.concernInsert(data);
    }

    /**
     * 取消关注
     */
    private void concernCancel() {
        cancelPresenter = new ConcernCancelPresenter(getContext());
        cancelPresenter.onCreate();
        cancelPresenter.attachView(cancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", getAppId());
        map.put("appUserConcern", mliveBean.getAppUserId());

        String data = HelperUtil.getParameter(map);
        cancelPresenter.concernCancel(data);
    }

    /**
     * 获取当前用户id
     */
    private String getAppId() {
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        return userBeanDao.loadAll().get(0).getAppUserId();
    }

    /**
     * 立即报名
     */
    @OnClick(R.id.tv_sign_up_now)
    public void signUpNow() {
        tv_sign_up_now.setVisibility(View.GONE);
        ll_pay_online_player_info.setVisibility(View.VISIBLE);
    }

    /**
     * 关注回调
     */
    private ConcernInsertView concernInsertView = new ConcernInsertView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    ivIsConcern.setImageResource(R.mipmap.has_attention);
                } else {
                    Toast.makeText(getContext(), "关注失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "concernInsertView:==" + result);
        }
    };

    /**
     * 取消关注
     */
    private ConcernCancelView cancelView = new ConcernCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean.getRet().equals("1")) {
                ivIsConcern.setImageResource(R.mipmap.add_attention_player);
            } else {
                Toast.makeText(getContext(), "取消关注失败!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "cancelView:==" + result);
        }
    };

}
