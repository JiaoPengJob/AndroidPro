package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.ContractorFinishedParam;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.OrderDetailResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 工人订单详情
 */
public class WorkerOrderInfoActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvWOInfoButton)
    TextView tvWOInfoButton;
    @BindView(R.id.tvWOInfoNumber)
    TextView tvWOInfoNumber;
    @BindView(R.id.tvWOInfoPhone)
    TextView tvWOInfoPhone;
    @BindView(R.id.tvWOInfoUser)
    TextView tvWOInfoUser;
    @BindView(R.id.tvWOInfoDescription)
    TextView tvWOInfoDescription;
    @BindView(R.id.tvWOInfoWorkTime)
    TextView tvWOInfoWorkTime;
    @BindView(R.id.tvWOInfoWorkArea)
    TextView tvWOInfoWorkArea;
    @BindView(R.id.tvWOInfoDoorPrice)
    TextView tvWOInfoDoorPrice;
    @BindView(R.id.tvWOInfoDayPrice)
    TextView tvWOInfoDayPrice;
    @BindView(R.id.tvWOInfoNeedType)
    TextView tvWOInfoNeedType;
    @BindView(R.id.llWOInfoDayPrice)
    LinearLayout llWOInfoDayPrice;
    @BindView(R.id.llWOInfoDoorPrice)
    LinearLayout llWOInfoDoorPrice;
    @BindView(R.id.tvWOInfoWorkerType)
    TextView tvWOInfoWorkerType;
    @BindView(R.id.tvWOInfoSendTime)
    TextView tvWOInfoSendTime;
    @BindView(R.id.tvWOInfoTitle)
    TextView tvWOInfoTitle;
    @BindView(R.id.tvWOInfoState)
    TextView tvWOInfoState;
    @BindView(R.id.tvWOInfoGetTime)
    TextView tvWOInfoGetTime;
    @BindView(R.id.llWOInfoDismissCause)
    LinearLayout llWOInfoDismissCause;
    @BindView(R.id.tvWOInfoDismissCause)
    TextView tvWOInfoDismissCause;
    @BindView(R.id.llWOInfoPrice)
    LinearLayout llWOInfoPrice;
    @BindView(R.id.tvWOInfoPrice)
    TextView tvWOInfoPrice;
    @BindView(R.id.llWOInfoDays)
    LinearLayout llWOInfoDays;
    @BindView(R.id.tvWOInfoDays)
    TextView tvWOInfoDays;
    @BindView(R.id.llWOInfoDPrice)
    LinearLayout llWOInfoDPrice;
    @BindView(R.id.tvWOInfoDPrice)
    TextView tvWOInfoDPrice;
    @BindView(R.id.llWOInfoHasDate)
    LinearLayout llWOInfoHasDate;
    @BindView(R.id.tvWOInfoHasDate)
    TextView tvWOInfoHasDate;
    @BindView(R.id.llWOInfoDate)
    LinearLayout llWOInfoDate;
    @BindView(R.id.tvWOInfoDateInfo)
    TextView tvWOInfoDateInfo;
    @BindView(R.id.tvWOInfoDate)
    TextView tvWOInfoDate;
    @BindView(R.id.llWOInfoDisPrice)
    LinearLayout llWOInfoDisPrice;
    @BindView(R.id.tvWOInfoDisPrice)
    TextView tvWOInfoDisPrice;

    private Intent intent;
    private Dialog dialog;
    private TextView tvWorkerOrdersCancel, tvWorkerOrdersConfirm;
    private int id;
    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private boolean isPoint = true;
    private OrderDetailResult orderDetail;
    private JpushParam jp;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_order_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("订单详情");
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(WorkerOrderInfoActivity.this);
        id = getIntent().getIntExtra("id", 0);
        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
            if (jp.getOrder_id() != null) {
                id = Integer.parseInt(jp.getOrder_id());
            } else if (jp.getOrder_number() != null) {
                id = Integer.parseInt(jp.getOrder_number());
            }
        }
//        getOrderDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderDetail();
    }

    /**
     * 获取订单详情
     */
    private void getOrderDetail() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(WorkerOrderInfoActivity.this);
        presenter.onCreate();
        presenter.getorderdetail(id);
        presenter.attachView(orderDetailView);
    }

    /**
     * 打电话
     */
    @OnClick(R.id.tvWOInfoPhone)
    public void phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + orderDetail.getContact_number());
        intent.setData(data);
        startActivity(intent);
    }

    private ClientBaseView<Object> orderDetailView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerOrderInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                orderDetail = (OrderDetailResult) GsonUtil.parseJson(baseBean.getData(), OrderDetailResult.class);
                initViewData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "orderDetailView==" + result);
        }
    };

    /**
     * 加载页面数据
     */
    private void initViewData() {
        tvWOInfoNumber.setText(orderDetail.getNumber());
        tvWOInfoState.setText(orderDetail.getState_msg());
        tvWOInfoGetTime.setText(HelperUtil.StringDateToSingle(orderDetail.getCreate_date()));
        tvWOInfoTitle.setText(orderDetail.getTitle());
        tvWOInfoSendTime.setText(HelperUtil.StringDateToSingle(orderDetail.getRequire_create_date() + " "));
        if (orderDetail.getRequire_type().equals("点工")) {
            tvWOInfoWorkerType.setText("点工");
            isPoint = true;
            llWOInfoDayPrice.setVisibility(View.VISIBLE);
            llWOInfoDoorPrice.setVisibility(View.GONE);
        } else {
            tvWOInfoWorkerType.setText("包工");
            isPoint = false;
            llWOInfoDayPrice.setVisibility(View.GONE);
            llWOInfoDoorPrice.setVisibility(View.VISIBLE);
        }
        tvWOInfoNeedType.setText(orderDetail.getWorker_types());
        tvWOInfoDayPrice.setText(String.valueOf(orderDetail.getWages()) + "元");
        tvWOInfoDoorPrice.setText(orderDetail.getGoto_price() + "元");
        tvWOInfoWorkArea.setText(orderDetail.getAddress());
        tvWOInfoWorkTime.setText(orderDetail.getWork_time());
        tvWOInfoDescription.setText(orderDetail.getDescription());//施工描述
        tvWOInfoUser.setText(orderDetail.getContacts());
        tvWOInfoPhone.setText(orderDetail.getContact_number());
        tvWOInfoDismissCause.setText(orderDetail.getDismiss_cause());
        tvWOInfoPrice.setText(orderDetail.getTotal_money() + "元");
        tvWOInfoDays.setText(orderDetail.getWork_days() + "天");
        tvWOInfoDPrice.setText(orderDetail.getFront_money() + "元");
        tvWOInfoHasDate.setText(HelperUtil.StringDateToSingle(orderDetail.getCreate_date() + " "));
        tvWOInfoDisPrice.setText(orderDetail.getDismiss_money() + "元");
        switch (orderDetail.getState()) {
            case 201://施工中
                price = orderDetail.getWages();
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.VISIBLE);
                tvWOInfoButton.setText("确认完工");
                llWOInfoHasDate.setVisibility(View.GONE);
//                llWOInfoPrice.setVisibility(View.VISIBLE);
//                llWOInfoDPrice.setVisibility(View.VISIBLE);
                break;
            case 103://包工、施工中
                price = String.valueOf(Double.parseDouble(orderDetail.getTotal_money()) - Double.parseDouble(orderDetail.getFront_money()));
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.VISIBLE);
                tvWOInfoButton.setText("确认完工");
                llWOInfoDPrice.setVisibility(View.VISIBLE);
                llWOInfoHasDate.setVisibility(View.GONE);
                llWOInfoPrice.setVisibility(View.VISIBLE);
                break;
            case 202://已完成
            case 203://待付工资
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.GONE);
                llWOInfoPrice.setVisibility(View.VISIBLE);
                llWOInfoDays.setVisibility(View.VISIBLE);
                llWOInfoDate.setVisibility(View.VISIBLE);
                tvWOInfoDateInfo.setText("完工时间：");
                tvWOInfoDate.setText(HelperUtil.StringDateToSingle(orderDetail.getComplete_date()));
                break;
            case 220://辞退中
                llWOInfoDismissCause.setVisibility(View.VISIBLE);
                tvWOInfoButton.setVisibility(View.VISIBLE);
                tvWOInfoButton.setText("确认辞退");
                llWOInfoDate.setVisibility(View.GONE);
//                tvWOInfoDateInfo.setText("辞退时间：");
                break;
            case 122:
                llWOInfoDismissCause.setVisibility(View.VISIBLE);
                tvWOInfoButton.setVisibility(View.VISIBLE);
                tvWOInfoButton.setText("确认辞退");
                llWOInfoDate.setVisibility(View.VISIBLE);
                tvWOInfoDateInfo.setText("辞退时间：");
                break;
            case 221://待付款, //辞退待雇主付款
            case 222://已辞退
                llWOInfoDismissCause.setVisibility(View.VISIBLE);
                tvWOInfoButton.setVisibility(View.GONE);
                llWOInfoDays.setVisibility(View.VISIBLE);
                llWOInfoPrice.setVisibility(View.VISIBLE);
                llWOInfoDisPrice.setVisibility(View.VISIBLE);
                break;
            case 123:
            case 124:
                llWOInfoDismissCause.setVisibility(View.VISIBLE);
                tvWOInfoButton.setVisibility(View.GONE);
                break;
            case 101://估价中
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.VISIBLE);
                tvWOInfoButton.setText("定价");
                break;
            case 102://待支付定金
            case 105://待支付尾款
            case 120:
            case 204:
            case 121://已取消
            case 107://已完成
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.GONE);
                llWOInfoDPrice.setVisibility(View.VISIBLE);
                llWOInfoHasDate.setVisibility(View.VISIBLE);
                llWOInfoDate.setVisibility(View.VISIBLE);
                if (orderDetail.getState() == 121) {
                    tvWOInfoDateInfo.setText("取消时间：");
                    tvWOInfoDate.setText(HelperUtil.StringDateToSingle(orderDetail.getCancel_date()) + " ");
                } else if (orderDetail.getState() == 107) {
                    llWOInfoDays.setVisibility(View.VISIBLE);
                    tvWOInfoDateInfo.setText("完工时间：");
                    tvWOInfoDate.setText(HelperUtil.StringDateToSingle(orderDetail.getEnd_date()) + " ");
                } else if (orderDetail.getState() == 204) {
                    llWOInfoDPrice.setVisibility(View.GONE);
                    llWOInfoDays.setVisibility(View.VISIBLE);
                    tvWOInfoDateInfo.setText("完工时间：");
                    tvWOInfoDate.setText(HelperUtil.StringDateToSingle(orderDetail.getComplete_date()) + " ");
                } else if (orderDetail.getState() == 102) {
                    llWOInfoPrice.setVisibility(View.VISIBLE);
                    llWOInfoDPrice.setVisibility(View.VISIBLE);
                }
                break;
            default:
                llWOInfoDismissCause.setVisibility(View.GONE);
                tvWOInfoButton.setVisibility(View.GONE);
                break;
        }
    }

    private String price;

    /**
     * 底部按钮
     */
    @OnClick(R.id.tvWOInfoButton)
    public void tvWOInfoButton() {
        if (tvWOInfoButton.getText().toString().equals("确认完工")) {
            if (isPoint) {
                workDoneSuccess();
            } else {
                contractorFinished(id);
            }
        } else if (tvWOInfoButton.getText().toString().equals("确认辞退")) {
            //点工
            if (isPoint) {
                intent = new Intent(WorkerOrderInfoActivity.this, PointWageActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("price", Double.parseDouble(orderDetail.getWages()));
                startActivity(intent);
            } else {
                //包工
                intent = new Intent(WorkerOrderInfoActivity.this, ActualWageActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        } else if (tvWOInfoButton.getText().toString().equals("定价")) {
            intent = new Intent(WorkerOrderInfoActivity.this, ProjectValuationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    /**
     * 确认完工
     */
    private void workDoneSuccess() {
        dialog = new Dialog(WorkerOrderInfoActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_work_done, null);
        tvWorkerOrdersCancel = (TextView) layout.findViewById(R.id.tvWorkerOrdersCancel);
        tvWorkerOrdersConfirm = (TextView) layout.findViewById(R.id.tvWorkerOrdersConfirm);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerOrderInfoActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        //取消
        tvWorkerOrdersCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //确定
        tvWorkerOrdersConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                intent = new Intent(WorkerOrderInfoActivity.this, WageSettlementActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("price", Double.parseDouble(price));
                startActivity(intent);
            }
        });
    }

    /**
     * 包工确认完工
     */
    private void contractorFinished(int id) {
        mSVProgressHUD.showWithStatus("加载中...");
        ContractorFinishedParam contractorFinished = new ContractorFinishedParam(
                id
        );
        presenter = new PresenterImpl<Object>(WorkerOrderInfoActivity.this);
        presenter.onCreate();
        presenter.achieve2(contractorFinished);
        presenter.attachView(contractorFinishedView);
    }

    private ClientBaseView<Object> contractorFinishedView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Toast.makeText(WorkerOrderInfoActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            if (baseBean.getState() == 1) {
                getOrderDetail();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "contractorFinishedView" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWorkerOrderInfo() {
        if (jp != null) {
            intent = new Intent(WorkerOrderInfoActivity.this, WorkerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            WorkerOrderInfoActivity.this.finish();
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
                    intent = new Intent(WorkerOrderInfoActivity.this, WorkerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    WorkerOrderInfoActivity.this.finish();
                }
                bl = true;
            }
        }
        return bl;
    }
}
