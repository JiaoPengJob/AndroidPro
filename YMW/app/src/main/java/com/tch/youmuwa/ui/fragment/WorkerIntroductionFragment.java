package com.tch.youmuwa.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.GetWorkerExpParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.SearchWorkerResult;
import com.tch.youmuwa.bean.result.WorkerExpResult;
import com.tch.youmuwa.bean.result.WorkerInfo;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人的简介
 */

public class WorkerIntroductionFragment extends ViewPagerFragment implements FragmentBackHandler {
    /**
     * 加载组件
     */
    @BindView(R.id.rvEngineeringExperience)
    RecyclerView rvEngineeringExperience;
    //    @BindView(R.id.refreshWorkerIntroduction)
//    SmartRefreshLayout refreshWorkerIntroduction;
    @BindView(R.id.tvIntroductionWorkerName)
    TextView tvIntroductionWorkerName;
    @BindView(R.id.llIntroductionWorkerStars)
    LinearLayout llIntroductionWorkerStars;
    @BindView(R.id.tvIntroductionWorkerPosition)
    TextView tvIntroductionWorkerPosition;
    @BindView(R.id.tvIntroductionWorkerAge)
    TextView tvIntroductionWorkerAge;
    @BindView(R.id.tvIntroductionWorkerTime)
    TextView tvIntroductionWorkerTime;
    @BindView(R.id.tvIntroductionWorkerArea)
    TextView tvIntroductionWorkerArea;
    @BindView(R.id.ivIntroductionWorkerPhoto)
    ImageView ivIntroductionWorkerPhoto;
    @BindView(R.id.tvIntroductionWorkerIdle)
    TextView tvIntroductionWorkerIdle;
    @BindView(R.id.tvIntroductionWorkerGoodAt)
    TextView tvIntroductionWorkerGoodAt;
    @BindView(R.id.tvIntroductionWorkerOrdersNumber)
    TextView tvIntroductionWorkerOrdersNumber;
    @BindView(R.id.tvIntroductionWorkerPhone)
    TextView tvIntroductionWorkerPhone;

    private PresenterImpl<Object> presenter;
    private int workerId;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int index = 0;
    private List<WorkerExpResult.MsgListBean> workerExps;
    private CommonAdapter adapter;
    private WorkerInfo workerInfo;

    public static WorkerIntroductionFragment newInstance(int workerId) {
        WorkerIntroductionFragment fragment = new WorkerIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("workerId", workerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_introduction, container, false);
        ButterKnife.bind(this, view);
//        initView();
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            workerExps = null;
            index = 0;
            workerId = 0;
            if (presenter != null) {
                presenter.onStop();
                presenter = null;
            }
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        workerExps = new ArrayList<WorkerExpResult.MsgListBean>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        workerId = getArguments().getInt("workerId");
        clientWorkerInfo();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initViewData();
                handler.sendEmptyMessage(0);
            } else {
                clientWorkerExp();
            }
        }
    };

    /**
     * 加载工人信息
     */
    private void initViewData() {
        tvIntroductionWorkerName.setText(workerInfo.getName());
        for (int i = 0; i < workerInfo.getStar_level(); i++) {
            ImageView star = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(6, 0, 0, 0);
            star.setLayoutParams(lp);
            star.setImageResource(R.mipmap.star);
            llIntroductionWorkerStars.addView(star);
        }
        tvIntroductionWorkerPosition.setText(workerInfo.getWorker_type_name());
        tvIntroductionWorkerArea.setText(workerInfo.getWork_area());
        tvIntroductionWorkerAge.setText(workerInfo.getAge() + "岁");
        tvIntroductionWorkerTime.setText("工作" + String.valueOf(workerInfo.getWork_age()) + "年");
        Glide.with(getContext())
                .asBitmap()
                .load(workerInfo.getHead_image_path())
                .into(new BitmapImageViewTarget(ivIntroductionWorkerPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (resource != null) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getApplication().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivIntroductionWorkerPhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });
        tvIntroductionWorkerIdle.setText(workerInfo.getWork_state());
        if (workerInfo.getWork_state().equals("空闲")) {
            tvIntroductionWorkerIdle.setBackgroundResource(R.drawable.oval_guide_button);
        } else if (workerInfo.getWork_state().equals("施工")) {
            tvIntroductionWorkerIdle.setBackgroundResource(R.drawable.oval_worker_type_select);
        }
        tvIntroductionWorkerGoodAt.setText(workerInfo.getStrength());
        tvIntroductionWorkerOrdersNumber.setText(String.valueOf(workerInfo.getOrder_count()));
        tvIntroductionWorkerPhone.setText(workerInfo.getMobile());
    }

    @OnClick(R.id.ivIntroductionWorkerPhone)
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + workerInfo.getMobile());
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 设置默认列表信息
     */
    private void initDefaultListData() throws NullPointerException {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvEngineeringExperience.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<WorkerExpResult.MsgListBean>(getContext(), R.layout.item_engineering_experience, workerExps) {
            @Override
            protected void convert(ViewHolder holder, WorkerExpResult.MsgListBean workerExp, int position) {
                holder.setText(R.id.tvExperienceProjectName, workerExp.getTitle());
//                String startDate = workerExp.getFromto_date().substring(0, workerExp.getFromto_date().indexOf(" "));
//                String endDate = workerExp.getFromto_date().substring(workerExp.getFromto_date().indexOf("--"), workerExp.getFromto_date().indexOf(" "));
                holder.setText(R.id.tvExperienceProjectTime, workerExp.getFromto_date());
                holder.setText(R.id.tvExperienceProjectContent, workerExp.getDescription());
            }
        };
        if (adapter != null) {
            rvEngineeringExperience.setAdapter(adapter);
        }
    }

    /**
     * 获取工人工程经历
     */
    private void clientWorkerExp() {
        GetWorkerExpParam workerExpParam = new GetWorkerExpParam(
                workerId,
                index,
                10
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getworkerexp(workerExpParam);
        presenter.attachView(getWorkerExp);
    }

    private ClientBaseView<Object> getWorkerExp = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                WorkerExpResult workerExpResult = (WorkerExpResult) GsonUtil.parseJson(baseBean.getData(), WorkerExpResult.class);
                for (WorkerExpResult.MsgListBean bean : workerExpResult.getMsg_list()) {
                    workerExps.add(bean);
                }
                initDefaultListData();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "getWorkerExp:==" + result);
        }
    };

    /**
     * 获取工人信息
     */
    private void clientWorkerInfo() {
        WorkerInfoParam workerInfoParam = new WorkerInfoParam(workerId);
        PresenterImpl<Object> workerInfoPresenter = new PresenterImpl<Object>(getContext());
        workerInfoPresenter.onCreate();
        workerInfoPresenter.getworkerintro(workerInfoParam);
        workerInfoPresenter.attachView(workerInfoView);
    }

    private ClientBaseView<Object> workerInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                workerInfo = (WorkerInfo) GsonUtil.parseJson(baseBean.getData(), WorkerInfo.class);
                if (workerInfo != null) {
                    handler.sendEmptyMessage(1);
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerInfoView:==" + result);
        }
    };

    @Override
    public boolean onBackPressed() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();
            if (presenter != null) {
                presenter.onStop();
            }
            return true;
        } else {
            return false;
        }
    }

}
