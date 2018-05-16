package com.tch.zx.activity.mine;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.RecommentTitleAdapter;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.LoginResultBean;
import com.tch.zx.http.bean.result.UserInfoResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * 我的/公司信息
 */
public class CompanyInfoMainActivity extends BaseActivity {
    /**
     * 公司简介信息内容
     */
    @BindView(R.id.tv_company_info_content)
    TextView tv_company_info_content;
    /**
     * 查看所有的按钮布局
     */
    @BindView(R.id.ll_company_info_look_all)
    LinearLayout ll_company_info_look_all;
    /**
     * 查看全部内容的图标
     */
    @BindView(R.id.iv_company_info_look_all)
    ImageView iv_company_info_look_all;
    /**
     * 图片滑动展示器
     */
    @BindView(R.id.indicator_company_info_main)
    CircleIndicator indicator_company_info_main;
    /**
     * 展示子页
     */
    @BindView(R.id.viewpager_company_info_main)
    ViewPager viewpager_company_info_main;
    /**
     * 公司logo
     */
    @BindView(R.id.ivCompanyLogo)
    ImageView ivCompanyLogo;
    /**
     * 公司名称
     */
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    /**
     * 公司地址
     */
    @BindView(R.id.tvCompanyAddress)
    TextView tvCompanyAddress;
    /**
     * 行业
     */
    @BindView(R.id.tvCompanyCategory)
    TextView tvCompanyCategory;
    @BindView(R.id.tvCompanyIndustryStype)
    TextView tvCompanyIndustryStype;

    /**
     * 判断是否展示了全部内容
     */
    private boolean isAll = false;
    /**
     * 数据库操作
     */
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private UserBean userBean;

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
        setContentView(R.layout.activity_company_info_main);
        ButterKnife.bind(this);

        initView();

    }

    /**
     * 初始化布局
     */
    private void initView() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
        userBean = userBeanDao.loadAll().get(0);
        if (getIntent().getSerializableExtra("userInfo") != null) {
            userInfo = ((UserInfoResult) getIntent().getSerializableExtra("userInfo")).getResponseObject();
            setOtherUserInfo();
        }
        if (getIntent().getStringExtra("activity") != null) {
            activity = getIntent().getStringExtra("activity");
            if (activity.equals("Other")) {

            } else if (activity.equals("Mine")) {
                setLocationInfo();
            }
        }
    }

    /**
     * 加载本地数据展示
     */
    private void setLocationInfo() {
        if (userBean != null) {
            Type type = new TypeToken<List<LoginResultBean.ResultBean.ResponseObjectBean.CompanyPicListBean>>() {
            }.getType();
            List<LoginResultBean.ResultBean.ResponseObjectBean.CompanyPicListBean> picList = new Gson().fromJson(userBean.getCompanyPicList(), type);
            if (picList != null && picList.size() > 0) {
                setCircleIndicator(picList);
            }
            Glide.with(CompanyInfoMainActivity.this).load(userBean.getCompanyLogo()).into(ivCompanyLogo);
            tvCompanyName.setText(userBean.getCompanyName());
            tvCompanyCategory.setText(userBean.getCategoryName());
            tvCompanyIndustryStype.setText(userBean.getStypeName());
            tvCompanyAddress.setText(userBean.getCompanyAddress());
            tv_company_info_content.setText(userBean.getCompanyIntroduce());
        }
    }

    /**
     * 加载用户信息
     */
    private void setOtherUserInfo() {
//        Type type = new TypeToken<List<LoginResultBean.ResultBean.ResponseObjectBean.CompanyPicListBean>>() {
//        }.getType();
//        List<LoginResultBean.ResultBean.ResponseObjectBean.CompanyPicListBean> picList = new Gson().fromJson(, type);
//        if (picList != null && picList.size() > 0) {
//            setCircleIndicator(picList);
//        }
        Glide.with(CompanyInfoMainActivity.this).load(userInfo.getCompany_logo()).into(ivCompanyLogo);
        tvCompanyName.setText(userInfo.getCompany_name());
//        tvCompanyCategory.setText();
        tvCompanyIndustryStype.setText(userInfo.getCompany_position());
        tvCompanyAddress.setText(userInfo.getAdr_province()+" "+userInfo.getAdr_city()+" "+userInfo.getAdr_county());
        tv_company_info_content.setText(userInfo.getCompany_introduce());
    }

    /**
     * 展示所有公司简介信息
     */
    @OnClick(R.id.ll_company_info_look_all)
    public void showAllContent() {
        String context = tv_company_info_content.getText().toString();
        int length = context.length();
        int i = length * 16 / 350;
        if (isAll) {
            tv_company_info_content.setLines(3);
            iv_company_info_look_all.setImageResource(R.mipmap.select_all);
            isAll = false;
        } else {
            tv_company_info_content.setLines(i);
            iv_company_info_look_all.setImageResource(R.mipmap.top_point_content);
            isAll = true;
        }
    }

    /**
     * 加载主页推荐tabView栏图片
     */
    private void setCircleIndicator(List<LoginResultBean.ResultBean.ResponseObjectBean.CompanyPicListBean> list) {
        List<String> urlList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            urlList.add(list.get(i).getCompanyPicture());
        }
        RecommentTitleAdapter recommentTitleAdapter = new RecommentTitleAdapter(this, urlList);
        viewpager_company_info_main.setAdapter(recommentTitleAdapter);
        indicator_company_info_main.setFocusable(false);
        indicator_company_info_main.setViewPager(viewpager_company_info_main);
    }

    /**
     * 编辑
     */
    @OnClick(R.id.tv_edit_company_info)
    public void editInfo() {
        Intent intent = new Intent(this, CompanyDetailsActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_return_company_info_main)
    public void returnCompanyInfoMain() {
        this.finish();
    }
}
