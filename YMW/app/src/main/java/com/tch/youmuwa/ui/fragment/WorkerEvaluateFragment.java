package com.tch.youmuwa.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.WorkerEvaluateResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.employer.ImageLookActivity;
import com.tch.youmuwa.ui.activity.employer.OrderEvaluationActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 工人的评价
 */

public class WorkerEvaluateFragment extends ViewPagerFragment implements FragmentBackHandler {

    @BindView(R.id.refreshWorkerEvaluate)
    SmartRefreshLayout refreshWorkerEvaluate;
    @BindView(R.id.rvWorkerEvaluate)
    RecyclerView rvWorkerEvaluate;
    @BindView(R.id.llEvaluateNet)
    LinearLayout llEvaluateNet;
    @BindView(R.id.llNoMsg)
    LinearLayout llNoMsg;

    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private int index = 0;
    private boolean isLoadMore = false;
    private int workerId = 0;
    private WorkerEvaluateResult workerEvaluateResult;
    private List<WorkerEvaluateResult.MsgListBean> list;
    private CommonAdapter adapter;

    public static WorkerEvaluateFragment newInstance(int workerId) {
        WorkerEvaluateFragment fragment = new WorkerEvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("workerId", workerId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_evaluate, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            initView();
        } else {
            list = null;
            index = 0;
            workerId = 0;
            isLoadMore = false;
            workerEvaluateResult = null;
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
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(getContext());
        workerId = getArguments().getInt("workerId");
        list = new ArrayList<WorkerEvaluateResult.MsgListBean>();
        netShow();
        refreshWorkerEvaluate.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                index = 0;
                isLoadMore = false;
                list.clear();
                netShow();
            }
        });
        refreshWorkerEvaluate.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                if (workerEvaluateResult != null && workerEvaluateResult.getCan_msg_more() != 0) {
                    index++;
                    netShow();
                } else {
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }

    private void netShow() {
        if (HelperUtil.isNetworkConnected(getContext())) {
            llEvaluateNet.setVisibility(View.GONE);
            refreshWorkerEvaluate.setVisibility(View.VISIBLE);
            clientWorkerEvaluate();
        } else {
            llEvaluateNet.setVisibility(View.VISIBLE);
            refreshWorkerEvaluate.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新
     */
    @OnClick(R.id.btRefreshNet)
    public void refreshNet() {
        netShow();
    }

    private CommonAdapter ninePhotos;

    /**
     * 默认列表信息
     */
    private void defaultListData() throws NullPointerException{
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvWorkerEvaluate.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<WorkerEvaluateResult.MsgListBean>(getContext(), R.layout.item_worker_evaluate, list) {
            @Override
            protected void convert(ViewHolder holder, final WorkerEvaluateResult.MsgListBean evaluateResult, int position) {
                GridLayoutManager gridLayout = new GridLayoutManager(getContext(), 3);
                ((RecyclerView) holder.getView(R.id.rvEvaluatePhotoShow)).setLayoutManager(gridLayout);
                ((RecyclerView) holder.getView(R.id.rvEvaluatePhotoShow)).setAdapter(ninePhotos = new CommonAdapter<String>(getContext(), R.layout.item_nine_img, evaluateResult.getEvaluate_img_paths()) {
                    @Override
                    protected void convert(ViewHolder holder, String s, int position) {
                        Glide.with(getContext()).load(s).into(((ImageView) holder.getView(R.id.ivNineImg)));
                    }
                });

                if (ninePhotos != null) {
                    ninePhotos.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int index) {
                            Intent intent = new Intent(getContext(), ImageLookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("urls", evaluateResult.getEvaluate_img_paths());
                            bundle.putInt("index", index);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    });
                }

                final ImageView ivEvaluateItemPhoto = (ImageView) holder.getView(R.id.ivEvaluateItemPhoto);
                Glide.with(getContext())
                        .asBitmap()
                        .load(evaluateResult.getHead_image_path())
                        .into(new BitmapImageViewTarget(ivEvaluateItemPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (resource != null) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getActivity().getApplication().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivEvaluateItemPhoto.setImageDrawable(circularBitmapDrawable);
                                }
                            }
                        });
                holder.setText(R.id.tvEvaluateItemName, evaluateResult.getName());
                LinearLayout llEvaluateItemStar = (LinearLayout) holder.getView(R.id.llEvaluateItemStar);
                llEvaluateItemStar.removeAllViews();
                for (int i = 0; i < evaluateResult.getEvaluate_grade(); i++) {
                    ImageView star = new ImageView(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(6, 0, 0, 0);
                    star.setLayoutParams(lp);
                    star.setImageResource(R.mipmap.star);
                    llEvaluateItemStar.addView(star);
                }
                holder.setText(R.id.tvEvaluateItemTime, HelperUtil.StringDateToSingle(evaluateResult.getEvaluate_date() + " "));
                holder.setText(R.id.tvEvaluateItemLabel, evaluateResult.getTitle());
                holder.setText(R.id.tvEvaluateItemContent, evaluateResult.getEvaluate_content());
            }
        };
        if (adapter != null) {
            rvWorkerEvaluate.setAdapter(adapter);
        }
        if (list != null && list.size() >= 10) {
            HelperUtil.moveToPosition(layoutManager, rvWorkerEvaluate, (index + 10));
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 访问工人评价列表
     */
    private void clientWorkerEvaluate() {
        mSVProgressHUD.showWithStatus("加载中...");
        GetWorkerExpParam workerExpParam = new GetWorkerExpParam(
                workerId,
                index,
                10
        );
        presenter = new PresenterImpl<Object>(getContext());
        presenter.onCreate();
        presenter.getworkerevaluate(workerExpParam);
        presenter.attachView(workerEvaluateView);
    }

    private ClientBaseView<Object> workerEvaluateView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshWorkerEvaluate.isRefreshing()) {
                refreshWorkerEvaluate.finishRefresh();
            }

            if (refreshWorkerEvaluate.isLoading()) {
                refreshWorkerEvaluate.finishLoadmore();
            }

            if (baseBean.getState() != 1) {
                Toast.makeText(getContext(), baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                rvWorkerEvaluate.setVisibility(View.GONE);
                llNoMsg.setVisibility(View.VISIBLE);
            } else {
                workerEvaluateResult = (WorkerEvaluateResult) GsonUtil.parseJson(baseBean.getData(), WorkerEvaluateResult.class);
                if (workerEvaluateResult.getMsg_list().size() <= 0) {
                    rvWorkerEvaluate.setVisibility(View.GONE);
                    llNoMsg.setVisibility(View.VISIBLE);
                } else {
                    rvWorkerEvaluate.setVisibility(View.VISIBLE);
                    llNoMsg.setVisibility(View.GONE);
                }
                if (isLoadMore) {
                    for (WorkerEvaluateResult.MsgListBean mb : workerEvaluateResult.getMsg_list()) {
                        list.add(mb);
                    }
                } else {
                    list = workerEvaluateResult.getMsg_list();
                }
                defaultListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshWorkerEvaluate.isRefreshing()) {
                refreshWorkerEvaluate.finishRefresh();
            }

            if (refreshWorkerEvaluate.isLoading()) {
                refreshWorkerEvaluate.finishLoadmore();
            }
            Log.e("Error", "workerEvaluateView:==" + result);
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
