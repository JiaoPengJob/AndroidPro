package com.tch.youmuwa.ui.activity.employer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.parameters.RequireInfoParam;
import com.tch.youmuwa.bean.parameters.ResponseWorkerParam;
import com.tch.youmuwa.bean.parameters.WorkerInfoParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.RequireInfoResult;
import com.tch.youmuwa.bean.result.WorkerInfo;
import com.tch.youmuwa.bean.result.WorkerSofRequireResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
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
import cn.jpush.android.api.JPushInterface;

/**
 * 需求中心
 */
public class OrderCenterActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rvOrderCenter)
    RecyclerView rvOrderCenter;
    @BindView(R.id.refreshOrderCenter)
    SmartRefreshLayout refreshOrderCenter;
    @BindView(R.id.tvOrderClear)
    TextView tvOrderClear;
    @BindView(R.id.tvOrderCenterTitle)
    TextView tvOrderCenterTitle;
    @BindView(R.id.tvOrderCenterTime)
    TextView tvOrderCenterTime;
    @BindView(R.id.tvOrderCenterWorkerType)
    TextView tvOrderCenterWorkerType;
    @BindView(R.id.tvOrderCenterNeedWorkerType)
    TextView tvOrderCenterNeedWorkerType;
    @BindView(R.id.tvOrderCenterDayPrice)
    TextView tvOrderCenterDayPrice;
    @BindView(R.id.tvOrderCenterGotoPrice)
    TextView tvOrderCenterGotoPrice;
    @BindView(R.id.tvOrderCenterArea)
    TextView tvOrderCenterArea;
    @BindView(R.id.tvOrderCenterWorkTime)
    TextView tvOrderCenterWorkTime;
    @BindView(R.id.tvOrderCenterDescription)
    TextView tvOrderCenterDescription;
    @BindView(R.id.tvOrderCenterContact)
    TextView tvOrderCenterContact;
    @BindView(R.id.tvOrderCenterPhone)
    TextView tvOrderCenterPhone;
    @BindView(R.id.tvSecondTitle)
    TextView tvSecondTitle;
    @BindView(R.id.tvLookOrder)
    TextView tvLookOrder;
    @BindView(R.id.ivOrderCenterIcon)
    ImageView ivOrderCenterIcon;
    @BindView(R.id.llOrderClear)
    LinearLayout llOrderClear;
    @BindView(R.id.llOrderClearTop)
    LinearLayout llOrderClearTop;
    @BindView(R.id.tvOrderGrap)
    TextView tvOrderGrap;
    @BindView(R.id.llOrderCenterDayPrice)
    LinearLayout llOrderCenterDayPrice;
    @BindView(R.id.llOrderCenterGotoPrice)
    LinearLayout llOrderCenterGotoPrice;
    @BindView(R.id.llRejectSingleReason)
    LinearLayout llRejectSingleReason;
    @BindView(R.id.tvRejectSingleReason)
    TextView tvRejectSingleReason;
    /**
     * 设置的参数
     */
    private CommonAdapter commonAdapter;//列表适配器
    private Intent intent;//跳转
    private int requireId;//需求单id
    private RequireInfoResult requireInfoResult;//需求单信息返回结果
    private WorkerSofRequireResult workerSofRequireResult;//已抢单工人的信息
    private ImageView ivOcWorkerItemPhoto;//工人头像
    private PresenterImpl<Object> presenter;//接口
    private int type = -1;//抢单还是指定工人
    private String bottomType = "";//区分页面底部状态显示
    private SVProgressHUD mSVProgressHUD;//加载显示
    private JpushParam jp;
    private List<WorkerSofRequireResult.MsgListBean> workerSofs;
    private LinearLayout llBottomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_center);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clientRequireInfo();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("需求中心");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        requireId = getIntent().getIntExtra("requireId", 0);
        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
            requireId = Integer.parseInt(jp.getRequire_id());
        }

        refreshOrderCenter.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                clientRequireInfo();
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                clientWorkerSofRequire();
            } else if (msg.what == 2) {
                tvSecondTitle.setVisibility(View.VISIBLE);
                tvSecondTitle.setText("已下单工人");
                clientWorkerInfo();
            }
        }
    };

    /**
     * 获取工人信息
     */
    private void clientWorkerInfo() {
        WorkerInfoParam workerInfoParam = new WorkerInfoParam(requireInfoResult.getWorker_id());
        PresenterImpl<Object> workerInfoPresenter = new PresenterImpl<Object>(OrderCenterActivity.this);
        workerInfoPresenter.onCreate();
        workerInfoPresenter.getworkerintro(workerInfoParam);
        workerInfoPresenter.attachView(workerInfoView);
    }

    private ClientBaseView<Object> workerInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                WorkerInfo workerInfo = (WorkerInfo) GsonUtil.parseJson(baseBean.getData(), WorkerInfo.class);
                WorkerSofRequireResult.MsgListBean mb = new WorkerSofRequireResult.MsgListBean();
                mb.setAge(workerInfo.getAge());
                mb.setHead_image_path(workerInfo.getHead_image_path());
                mb.setId(workerInfo.getId());
                mb.setName(workerInfo.getName());
                mb.setOrder_count(workerInfo.getOrder_count());
                mb.setStar_level(workerInfo.getStar_level());
                mb.setStrength(workerInfo.getStrength());
                mb.setWork_age(String.valueOf(workerInfo.getWork_age()));
                mb.setWork_area(workerInfo.getWork_area());
                mb.setWorker_type_name(workerInfo.getWorker_type_name());
                workerSofs = new ArrayList<WorkerSofRequireResult.MsgListBean>();
                workerSofs.add(mb);
                initListData();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerInfoView:==" + result);
        }
    };

    /**
     * 获取需求单信息
     */
    private void clientRequireInfo() {
        mSVProgressHUD.showWithStatus("加载中...");
        RequireInfoParam requireInfoParam = new RequireInfoParam(requireId);
        presenter = new PresenterImpl<Object>(OrderCenterActivity.this);
        presenter.onCreate();
        presenter.getrequireinfo(requireInfoParam);
        presenter.attachView(requireInfoView);
    }

    private ClientBaseView<Object> requireInfoView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (refreshOrderCenter.isRefreshing()) {
                refreshOrderCenter.finishRefresh();
            }
            if (baseBean.getState() == 0) {
                Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                requireInfoResult = (RequireInfoResult) GsonUtil.parseJson(baseBean.getData(), RequireInfoResult.class);
                initViewData();
                if (requireInfoResult.getWorker_id() == 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshOrderCenter.isRefreshing()) {
                refreshOrderCenter.finishRefresh();
            }

            Log.e("Error", "requireInfoView==" + result);
        }
    };

    /**
     * 加载信息
     */
    private void initViewData() {
        type = requireInfoResult.getType();
        tvOrderCenterTitle.setText(requireInfoResult.getTitle());
        tvOrderCenterTime.setText(requireInfoResult.getCreate_date());
        if (requireInfoResult.getRequire_type() == 1) {
            tvOrderCenterWorkerType.setText("点工");
            llOrderCenterGotoPrice.setVisibility(View.GONE);
        } else {
            tvOrderCenterWorkerType.setText("包工");
            llOrderCenterDayPrice.setVisibility(View.GONE);
            llOrderClearTop.setVisibility(View.GONE);
        }
        tvOrderGrap.setText(requireInfoResult.getGrab_count() + "/" + requireInfoResult.getMax_count());
        tvOrderCenterNeedWorkerType.setText(requireInfoResult.getWorker_types());
        tvOrderCenterDayPrice.setText(requireInfoResult.getPrice() + "元");
        tvOrderCenterGotoPrice.setText(requireInfoResult.getGoto_price() + "元");
        tvOrderCenterArea.setText(requireInfoResult.getAddress());
        tvOrderCenterWorkTime.setText(HelperUtil.StringDateToSingle(requireInfoResult.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(requireInfoResult.getEnd_date() + " "));
        tvOrderCenterDescription.setText(requireInfoResult.getDescription());
        tvOrderCenterContact.setText(requireInfoResult.getContacts());
        tvOrderCenterPhone.setText(requireInfoResult.getContact_number());

        if (requireInfoResult.getState() == 1) {
            if (type == 1) {
                llOrderClear.setVisibility(View.VISIBLE);
                llOrderClearTop.setVisibility(View.VISIBLE);
                ivOrderCenterIcon.setVisibility(View.GONE);
            } else {
                llOrderClear.setVisibility(View.GONE);
                llOrderClearTop.setVisibility(View.GONE);
                ivOrderCenterIcon.setVisibility(View.GONE);
            }
            llRejectSingleReason.setVisibility(View.GONE);
        } else if (requireInfoResult.getState() == 2) {//已取消
            llOrderClear.setVisibility(View.GONE);
            llOrderClearTop.setVisibility(View.GONE);
            ivOrderCenterIcon.setVisibility(View.VISIBLE);
            ivOrderCenterIcon.setImageResource(R.mipmap.cancelled_icon);
            llRejectSingleReason.setVisibility(View.GONE);
        } else if (requireInfoResult.getState() == 3) {//已接单
            llOrderClear.setVisibility(View.GONE);
            llOrderClearTop.setVisibility(View.GONE);
            ivOrderCenterIcon.setVisibility(View.VISIBLE);
            ivOrderCenterIcon.setImageResource(R.mipmap.received_icon);
            llRejectSingleReason.setVisibility(View.GONE);
        } else if (requireInfoResult.getState() == 4) {//已拒单
            llOrderClear.setVisibility(View.GONE);
            llOrderClearTop.setVisibility(View.GONE);
            ivOrderCenterIcon.setVisibility(View.VISIBLE);
            ivOrderCenterIcon.setImageResource(R.mipmap.rejected_icon);
            llRejectSingleReason.setVisibility(View.VISIBLE);
            tvRejectSingleReason.setText(requireInfoResult.getRefuse_reason());
        }
    }

    /**
     * 获取已抢单工人信息
     */
    private void clientWorkerSofRequire() {
        RequireInfoParam requireInfoParam = new RequireInfoParam(requireId);
        PresenterImpl<Object> workerSofRequirePresenter = new PresenterImpl<Object>(OrderCenterActivity.this);
        workerSofRequirePresenter.onCreate();
        workerSofRequirePresenter.getworkersofrequire(requireInfoParam);
        workerSofRequirePresenter.attachView(workerSofRequireView);
    }

    private ClientBaseView<Object> workerSofRequireView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() != 1) {
                Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
                tvSecondTitle.setVisibility(View.GONE);
                rvOrderCenter.setVisibility(View.GONE);
            } else {
                workerSofRequireResult = (WorkerSofRequireResult) GsonUtil.parseJson(baseBean.getData(), WorkerSofRequireResult.class);
                if (workerSofRequireResult.getMsg_list() != null && workerSofRequireResult.getMsg_list().size() > 0) {
                    workerSofs = workerSofRequireResult.getMsg_list();
                    initListData();
                } else {
                    tvSecondTitle.setVisibility(View.GONE);
                    rvOrderCenter.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "workerSofRequireView==" + result);
        }
    };

    /**
     * 加载列表信息
     */
    private void initListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderCenterActivity.this);
        rvOrderCenter.setLayoutManager(layoutManager);
        rvOrderCenter.setAdapter(commonAdapter = new CommonAdapter<WorkerSofRequireResult.MsgListBean>(OrderCenterActivity.this, R.layout.item_order_center, workerSofs) {
            @Override
            protected void convert(final ViewHolder holder, final WorkerSofRequireResult.MsgListBean workerSof, int position) {
                llBottomButton = (LinearLayout) holder.getView(R.id.llBottomButton);
                ivOcWorkerItemPhoto = (ImageView) holder.getView(R.id.ivOcWorkerItemPhoto);
                Glide.with(OrderCenterActivity.this)
                        .asBitmap()
                        .load(workerSof.getHead_image_path())
                        .into(new BitmapImageViewTarget(ivOcWorkerItemPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(OrderCenterActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.setImageDrawable(R.id.ivOcWorkerItemPhoto, circularBitmapDrawable);
                            }
                        });
                holder.setText(R.id.tvOcWorkerItemName, workerSof.getName());
                holder.setText(R.id.tvOcWorkerItemAge, String.valueOf(workerSof.getAge()));
                holder.setText(R.id.tvOcWorkerItemWorkerAge, workerSof.getWork_age());
                holder.setText(R.id.tvOcWorkerItemWorkerType, workerSof.getWorker_type_name());
                holder.setText(R.id.tvOcWorkerItemGoodAt, workerSof.getStrength());
                LinearLayout llStartsShow = (LinearLayout) holder.getView(R.id.llStartsShow);
                for (int i = 0; i < workerSof.getStar_level(); i++) {
                    ImageView star = new ImageView(OrderCenterActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(6, 0, 0, 0);
                    star.setLayoutParams(lp);
                    star.setImageResource(R.mipmap.star);
                    llStartsShow.addView(star);
                }

                if (requireInfoResult.getType() == 2) {
                    if (requireInfoResult.getState() == 1) {
                        holder.setText(R.id.tvRefuseRecruitment, "提醒接单");
                        holder.setOnClickListener(R.id.tvRefuseRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requireNotifyWorker();
                            }
                        });
                        holder.setText(R.id.tvConfirmRecruitment, "取消下单");
                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clientCancelRequire();
                            }
                        });
                    } else if (requireInfoResult.getState() == 2) {
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                        bottomType = "";
                    } else if (requireInfoResult.getState() == 3) {
                        tvLookOrder.setVisibility(View.VISIBLE);
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                        bottomType = "";
                    } else if (requireInfoResult.getState() == 4) {
                        tvLookOrder.setVisibility(View.GONE);
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                    } else {
                        holder.setVisible(R.id.tvRefuseRecruitment, true);
                        holder.setVisible(R.id.tvConfirmRecruitment, true);
                        holder.setText(R.id.tvRefuseRecruitment, "拒绝招用");
                        holder.setText(R.id.tvConfirmRecruitment, "确认招用");
                        bottomType = "Recruitment";
                        holder.setOnClickListener(R.id.tvRefuseRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                responseWorker(workerSof.getId(), 0);
                            }
                        });
                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                responseWorker(workerSof.getId(), 1);
                            }
                        });
                    }
                } else {
                    tvSecondTitle.setText("已下单工人");
                    holder.setText(R.id.tvRefuseRecruitment, "提醒接单");
                    holder.setOnClickListener(R.id.tvRefuseRecruitment, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requireNotifyWorker();
                        }
                    });
                    holder.setText(R.id.tvConfirmRecruitment, "取消下单");

                    if (requireInfoResult.getWorker_id() == 0) {
                        tvLookOrder.setVisibility(View.GONE);
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                        bottomType = "Else";
                    } else {
                        tvLookOrder.setVisibility(View.GONE);
                        holder.setVisible(R.id.tvWaitOrder, true);
                        holder.setVisible(R.id.tvRefuseRecruitment, true);
                        holder.setVisible(R.id.tvConfirmRecruitment, true);
                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clientCancelRequire();
                            }
                        });
                        bottomType = "Orders";
                    }

                    if (requireInfoResult.getState() == 1) {
//                        holder.setText(R.id.tvRefuseRecruitment, "提醒接单");
//                        holder.setOnClickListener(R.id.tvRefuseRecruitment, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                requireNotifyWorker();
//                            }
//                        });
//                        holder.setText(R.id.tvConfirmRecruitment, "取消下单");
//                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                clientCancelRequire();
//                            }
//                        });
                        holder.setVisible(R.id.tvRefuseRecruitment, true);
                        holder.setVisible(R.id.tvConfirmRecruitment, true);
                        holder.setText(R.id.tvRefuseRecruitment, "拒绝招用");
                        holder.setText(R.id.tvConfirmRecruitment, "确认招用");
                        bottomType = "Recruitment";
                        holder.setOnClickListener(R.id.tvRefuseRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                responseWorker(workerSof.getId(), 0);
                            }
                        });
                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                responseWorker(workerSof.getId(), 1);
                            }
                        });
                    } else if (requireInfoResult.getState() == 2) {
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                        bottomType = "";
                    } else if (requireInfoResult.getState() == 3) {
                        tvLookOrder.setVisibility(View.VISIBLE);
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                        bottomType = "";
                    } else if (requireInfoResult.getState() == 4) {
                        tvLookOrder.setVisibility(View.GONE);
                        holder.setVisible(R.id.tvWaitOrder, false);
                        holder.setVisible(R.id.tvRefuseRecruitment, false);
                        holder.setVisible(R.id.tvConfirmRecruitment, false);
                    } else {
                        holder.setVisible(R.id.tvWaitOrder, true);
                        holder.setVisible(R.id.tvRefuseRecruitment, true);
                        holder.setVisible(R.id.tvConfirmRecruitment, true);
                        holder.setOnClickListener(R.id.tvConfirmRecruitment, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clientCancelRequire();
                            }
                        });
                        bottomType = "Orders";
                    }
                }
            }
        });
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                intent = new Intent(OrderCenterActivity.this, WorkerInfoActivity.class);
                intent.putExtra("workerId", workerSofs.get(position).getId());
                intent.putExtra("bottomType", bottomType);
                intent.putExtra("requireId", requireId);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 查看订单
     */
    @OnClick(R.id.tvLookOrder)
    public void lookOrder() {
        intent = new Intent(OrderCenterActivity.this, OrderDetailsActivity.class);
        intent.putExtra("number", requireInfoResult.getOrder_number());
        startActivity(intent);
    }

    /**
     * 拒绝或招用工人
     */
    private void responseWorker(int workerId, int type) {
        mSVProgressHUD.showWithStatus("加载中...");
        ResponseWorkerParam responseWorkerParam = new ResponseWorkerParam(
                requireId,
                workerId,
                type
        );
        presenter = new PresenterImpl<Object>(OrderCenterActivity.this);
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

            if (baseBean.getState() == 1) {
                clientRequireInfo();
                if (llBottomButton != null) {
                    llBottomButton.setVisibility(View.GONE);
                }
//                if (requireInfoResult.getWorker_id() == 0) {
//                    handler.sendEmptyMessage(1);
//                } else {
//                    handler.sendEmptyMessage(2);
//                }
            } else {
                Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
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
     * 提醒工人接单
     */
    private void requireNotifyWorker() {
        mSVProgressHUD.showWithStatus("加载中...");
        RequireInfoParam requireParam = new RequireInfoParam(requireId);
        presenter = new PresenterImpl<Object>(OrderCenterActivity.this);
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
            Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                if (requireInfoResult.getWorker_id() == 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
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
     * 取消订单
     */
    @OnClick(R.id.tvOrderClear)
    public void orderClear() {
        clientCancelRequire();
    }

    /**
     * 取消需求单
     */
    private void clientCancelRequire() {
        mSVProgressHUD.showWithStatus("加载中...");
        RequireInfoParam requireParam = new RequireInfoParam(requireId);
        presenter = new PresenterImpl<Object>(OrderCenterActivity.this);
        presenter.onCreate();
        presenter.cancelrequire(requireParam);
        presenter.attachView(cancelRequireView);
    }

    private ClientBaseView<Object> cancelRequireView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(OrderCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                OrderCenterActivity.this.finish();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "cancelRequireView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatOrderCenter() {
        if (jp != null) {
            intent = new Intent(OrderCenterActivity.this, EmployerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            OrderCenterActivity.this.finish();
        }
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
                if (jp != null) {
                    intent = new Intent(OrderCenterActivity.this, EmployerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    OrderCenterActivity.this.finish();
                }
                bl = true;
            }
        }
        return bl;
    }

}
