package com.tch.youmuwa.ui.activity.employer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.bean.parameters.RequireInfoParam;
import com.tch.youmuwa.bean.parameters.ResponseWorkerParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.BusyListResult;
import com.tch.youmuwa.bean.result.SearchWorkerResult;
import com.tch.youmuwa.bean.result.WorkerInfo;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.myinterface.ShareInterface;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.worker.WorkerTDActivity;
import com.tch.youmuwa.ui.fragment.WorkerEvaluateFragment;
import com.tch.youmuwa.ui.fragment.WorkerIntroductionFragment;
import com.tch.youmuwa.ui.popupWindow.CalendarPopupWindow;
import com.tch.youmuwa.ui.popupWindow.SharePopupWindow;
import com.tch.youmuwa.ui.view.CustomViewPager;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选中的工人的信息
 */
public class WorkerInfoActivity extends BaseActivtiy implements CalenddarPopupInterface, ShareInterface {
    /**
     * 加载组件
     */
    @BindView(R.id.vpWorkerInfo)
    CustomViewPager vpWorkerInfo;
    @BindView(R.id.tvWorkerIntroduction)
    TextView tvWorkerIntroduction;
    @BindView(R.id.tvWorkerAppraisal)
    TextView tvWorkerAppraisal;
    @BindView(R.id.rlWorkerInfo)
    RelativeLayout rlWorkerInfo;
    @BindView(R.id.rlAreaWorkerTitle)
    RelativeLayout rlAreaWorkerTitle;
    @BindView(R.id.llRecruitmentShow)
    LinearLayout llRecruitmentShow;
    @BindView(R.id.tvPlaceOrder)
    TextView tvPlaceOrder;
    @BindView(R.id.tvRecruitmentLeft)
    TextView tvRecruitmentLeft;
    @BindView(R.id.tvRecruitmentRight)
    TextView tvRecruitmentRight;
    @BindView(R.id.llWInfoBottomView)
    LinearLayout llWInfoBottomView;
    /**
     * 设置的参数
     */
    private List<Fragment> fragments;//联动页面
    private String shareTitle, shareUrl, shareDescription, imageurl;//用于分享的信息
    private int workerId;//工人id
    private String bottomType = "";//底部显示标识
    private PresenterImpl<Object> presenter;//接口
    private int requireId;//需求id
    private String isSpecify = "";//是否为指定的类型
    private SVProgressHUD mSVProgressHUD;//加载显示
    private WorkerInfo workerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        workerId = getIntent().getIntExtra("workerId", 0);
        Uri uri = getIntent().getData();
        if (uri != null) {
            switchRoles();
            workerId = Integer.parseInt(uri.getQueryParameter("worker_id"));
        }
        if (getIntent().getStringExtra("isSpecify") != null) {
            isSpecify = getIntent().getStringExtra("isSpecify");
        }
        if (getIntent().getStringExtra("bottomType") != null) {
            bottomType = getIntent().getStringExtra("bottomType");
            if (bottomType.equals("Recruitment")) {
                tvPlaceOrder.setVisibility(View.GONE);
                llRecruitmentShow.setVisibility(View.VISIBLE);
                tvRecruitmentLeft.setText("拒绝招用");
                tvRecruitmentRight.setText("确认招用");
            } else if (bottomType.equals("Orders")) {
                tvPlaceOrder.setVisibility(View.GONE);
                llRecruitmentShow.setVisibility(View.VISIBLE);
                tvRecruitmentLeft.setText("提醒接单");
                tvRecruitmentRight.setText("取消下单");
            } else if (bottomType.equals("Else")) {
//                tvPlaceOrder.setVisibility(View.GONE);
//                llRecruitmentShow.setVisibility(View.GONE);
                llWInfoBottomView.setVisibility(View.GONE);
            } else {
                tvPlaceOrder.setVisibility(View.VISIBLE);
                llRecruitmentShow.setVisibility(View.GONE);
                tvPlaceOrder.setText("立即招用");
            }
        }
        requireId = getIntent().getIntExtra("requireId", 0);
        initFragment();
        clientWorkerInfo();
        getWorkerBusyList();
    }

    /**
     * 切换角色
     */
    private void switchRoles() {
        presenter = new PresenterImpl<Object>(WorkerInfoActivity.this);
        presenter.onCreate();
        presenter.switchroles(1);
        presenter.attachView(SwitchRolesView);
    }

    private ClientBaseView<Object> SwitchRolesView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                SharedPrefsUtil.putValue(WorkerInfoActivity.this, "isEmployer", true);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "SwitchRolesView==" + result);
        }
    };

    /**
     * 底部左侧
     */
    @OnClick(R.id.tvRecruitmentLeft)
    public void recruitmentLeft() {
        if (bottomType.equals("Recruitment")) {
            responseWorker(0);
        } else if (bottomType.equals("Orders")) {
            requireNotifyWorker();
        }
    }

    /**
     * 底部右侧
     */
    @OnClick(R.id.tvRecruitmentRight)
    public void recruitmentRight() {
        if (bottomType.equals("Recruitment")) {
            responseWorker(1);
        } else if (bottomType.equals("Orders")) {

        }
    }

    /**
     * 拒绝或招用工人
     */
    private void responseWorker(int type) {
        mSVProgressHUD.showWithStatus("加载中...");
        ResponseWorkerParam responseWorkerParam = new ResponseWorkerParam(
                requireId,
                workerId,
                type
        );
        presenter = new PresenterImpl<Object>(WorkerInfoActivity.this);
        presenter.onCreate();
        presenter.requireresponseworker(responseWorkerParam);
        presenter.attachView(responseWorkerView);
    }

    private ClientBaseView<Object> responseWorkerView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(WorkerInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                llWInfoBottomView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "responseWorkerView==" + result);
        }
    };

    /**
     * 加载fragment
     */
    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(WorkerIntroductionFragment.newInstance(workerId));
        fragments.add(WorkerEvaluateFragment.newInstance(workerId));
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                if (fragments != null) {
                    return fragments.size();
                } else {
                    return 0;
                }
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        };
        vpWorkerInfo.setAdapter(fragmentPagerAdapter);
        vpWorkerInfo.setPagingEnabled(true);
        vpWorkerInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tvWorkerIntroduction.setBackgroundResource(R.drawable.oval_worker_info_select);
                    tvWorkerAppraisal.setBackgroundResource(R.drawable.oval_worker_appraisal_not_select);
                    tvWorkerIntroduction.setTextColor(Color.parseColor("#FFFFFF"));
                    tvWorkerAppraisal.setTextColor(Color.parseColor("#666666"));
                } else {
                    tvWorkerAppraisal.setBackgroundResource(R.drawable.oval_worker_appraisal_select);
                    tvWorkerIntroduction.setBackgroundResource(R.drawable.oval_worker_info_not_select);
                    tvWorkerAppraisal.setTextColor(Color.parseColor("#FFFFFF"));
                    tvWorkerIntroduction.setTextColor(Color.parseColor("#666666"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 显示简介
     */
    @OnClick(R.id.tvWorkerIntroduction)
    public void workerIntroductionShow() {
        tvWorkerIntroduction.setBackgroundResource(R.drawable.oval_worker_info_select);
        tvWorkerAppraisal.setBackgroundResource(R.drawable.oval_worker_appraisal_not_select);
        tvWorkerIntroduction.setTextColor(Color.parseColor("#FFFFFF"));
        tvWorkerAppraisal.setTextColor(Color.parseColor("#666666"));
        vpWorkerInfo.setCurrentItem(0);
    }

    /**
     * 显示评价
     */
    @OnClick(R.id.tvWorkerAppraisal)
    public void workerAppraisalShow() {
        tvWorkerAppraisal.setBackgroundResource(R.drawable.oval_worker_appraisal_select);
        tvWorkerIntroduction.setBackgroundResource(R.drawable.oval_worker_info_not_select);
        tvWorkerAppraisal.setTextColor(Color.parseColor("#FFFFFF"));
        tvWorkerIntroduction.setTextColor(Color.parseColor("#666666"));
        vpWorkerInfo.setCurrentItem(1);
    }

    private List<DateItem> dates = new ArrayList<DateItem>();

    /**
     * 显示日历
     */
    @OnClick(R.id.ibCalendarShow)
    public void calendarShow() {
        CalendarPopupWindow areaPopupWindow = new CalendarPopupWindow(this, this, true, dates, false);
        areaPopupWindow.showAsDropDown(rlAreaWorkerTitle);
        rlWorkerInfo.setAlpha(0.8f);
        rlAreaWorkerTitle.setAlpha(1f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlWorkerInfo.setAlpha(1f);
            }
        });
    }

    /**
     * 日历数据回调
     *
     * @param list
     */
    @Override
    public void getResult(List<String> list) {
        for (String s : list) {
            Log.e("TAG", "getResult==" + s);
        }
    }

    /**
     * 获取工人忙碌日期区
     */
    private void getWorkerBusyList() {
        WorkerInfoParam workerInfoParam = new WorkerInfoParam(workerId);
        PresenterImpl<Object> workerInfoPresenter = new PresenterImpl<Object>(WorkerInfoActivity.this);
        workerInfoPresenter.onCreate();
        workerInfoPresenter.getworkerbusylist(workerInfoParam);
        workerInfoPresenter.attachView(getWorkerBusyListView);
    }

    private ClientBaseView<Object> getWorkerBusyListView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                BusyListResult br = (BusyListResult) GsonUtil.parseJson(baseBean.getData(), BusyListResult.class);
                for (BusyListResult.MsgListBean mb : br.getMsg_list()) {
                    List<String> date = HelperUtil.betweenDays(HelperUtil.simpleDate(mb.getBegin_date()), HelperUtil.simpleDate(mb.getEnd_date()));

                    for (String s : date) {
                        String year = s.substring(0, 4);
                        String month = s.substring(5, 7);
                        String day = s.substring(8, mb.getBegin_date().length());

                        DateItem di = new DateItem();
                        di.setYear(Integer.parseInt(year));
                        di.setMonth(Integer.parseInt(month));
                        di.setDateOfMonth(Integer.parseInt(day));

                        dates.add(di);
                    }
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerInfoView:==" + result);
        }
    };

    /**
     * 获取工人信息
     */
    private void clientWorkerInfo() {
        WorkerInfoParam workerInfoParam = new WorkerInfoParam(workerId);
        PresenterImpl<Object> workerInfoPresenter = new PresenterImpl<Object>(WorkerInfoActivity.this);
        workerInfoPresenter.onCreate();
        workerInfoPresenter.getworkerintro(workerInfoParam);
        workerInfoPresenter.attachView(workerInfoView);
    }

    private ClientBaseView<Object> workerInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                workerInfo = (WorkerInfo) GsonUtil.parseJson(baseBean.getData(), WorkerInfo.class);
                shareUrl = workerInfo.getShare().getUrl();
                shareTitle = workerInfo.getShare().getTitle();
                imageurl = workerInfo.getShare().getImage();
                shareDescription = workerInfo.getShare().getDescription();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerInfoView:==" + result);
        }
    };

    /**
     * 分享
     */
    @OnClick(R.id.ibShare)
    public void shareToOther() {
        SharePopupWindow areaPopupWindow = new SharePopupWindow(this, this);
        //设置Popupwindow显示位置（从顶部弹出）
        areaPopupWindow.showAtLocation(rlWorkerInfo, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        rlWorkerInfo.setAlpha(0.8f);
        areaPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlWorkerInfo.setAlpha(1f);
            }
        });
    }

    /**
     * 分享点击回调
     *
     * @param platform
     */
    @Override
    public void getShareResult(SHARE_MEDIA platform) {
        uMengShare(platform);
    }

    private void uMengShare(SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        UMImage image = new UMImage(WorkerInfoActivity.this, imageurl);//网络图片
        web.setThumb(image);//缩略图
        web.setDescription(shareDescription);//描述
        new ShareAction(this)
                .setPlatform(platform)//传入平台
                .withMedia(web)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    /**
     * 分享的回调
     */
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(WorkerInfoActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WorkerInfoActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WorkerInfoActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    /**
     * QQ与新浪微博的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 下单
     */
    @OnClick(R.id.tvPlaceOrder)
    public void placeOrder() {
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        intent.putExtra("workerId", workerId);
        intent.putExtra("isSpecify", isSpecify);
        startActivity(intent);
    }

    /**
     * 提醒工人接单
     */
    private void requireNotifyWorker() {
        mSVProgressHUD.showWithStatus("加载中...");
        RequireInfoParam requireParam = new RequireInfoParam(requireId);
        presenter = new PresenterImpl<Object>(WorkerInfoActivity.this);
        presenter.onCreate();
        presenter.requirenotifyworker(requireParam);
        presenter.attachView(requireNotifyWorkerView);
    }

    private ClientBaseView<Object> requireNotifyWorkerView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(WorkerInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "requireNotifyWorkerView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibBackwards)
    public void backwardsWorkerInfo() {
        WorkerInfoActivity.this.finish();
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
                WorkerInfoActivity.this.finish();
                bl = true;
            }
        }
        return bl;
    }

}
