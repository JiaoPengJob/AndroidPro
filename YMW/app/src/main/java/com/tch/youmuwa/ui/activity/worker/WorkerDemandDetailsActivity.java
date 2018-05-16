package com.tch.youmuwa.ui.activity.worker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.RequireDetailsResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 工人/需求中心/详细信息
 */
public class WorkerDemandDetailsActivity extends BaseActivtiy {
    /**
     * 加载组件
     */
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvWorkerGrabSingle)
    TextView tvWorkerGrabSingle;
    @BindView(R.id.tvWorkerDemandTitle)
    TextView tvWorkerDemandTitle;
    @BindView(R.id.tvWorkerDemandTime)
    TextView tvWorkerDemandTime;
    @BindView(R.id.tvWorkerDemandType)
    TextView tvWorkerDemandType;
    @BindView(R.id.tvWorkerDemandNeedType)
    TextView tvWorkerDemandNeedType;
    @BindView(R.id.tvWorkerDemandDayPrice)
    TextView tvWorkerDemandDayPrice;
    @BindView(R.id.tvWorkerDemandGotoPrice)
    TextView tvWorkerDemandGotoPrice;
    @BindView(R.id.tvWorkerDemandWorkArea)
    TextView tvWorkerDemandWorkArea;
    @BindView(R.id.tvWorkerDemandWorkTime)
    TextView tvWorkerDemandWorkTime;
    @BindView(R.id.tvWorkerDemandDescription)
    TextView tvWorkerDemandDescription;
    @BindView(R.id.tvWorkerDemandUser)
    TextView tvWorkerDemandUser;
    @BindView(R.id.tvWorkerDemandPhone)
    TextView tvWorkerDemandPhone;
    @BindView(R.id.llWorkerDemandDayPrice)
    LinearLayout llWorkerDemandDayPrice;
    @BindView(R.id.llOrderShow)
    LinearLayout llOrderShow;
    @BindView(R.id.tvGrabSingleWD)
    TextView tvGrabSingleWD;
    @BindView(R.id.tvSeeOrderWD)
    TextView tvSeeOrderWD;
    @BindView(R.id.llWorkerDemandGotoPrice)
    LinearLayout llWorkerDemandGotoPrice;

    private PresenterImpl<Object> presenter;
    private int id;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private Dialog dialog;
    private Intent intent;
    private RequireDetailsResult requireDetailsResult;
    private JpushParam jp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_demand_details);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("需求中心");
        id = getIntent().getIntExtra("id", 0);
        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
            id = Integer.parseInt(jp.getOrder_number());
        }
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        getRequireDetails();
    }

//    /**
//     * 抢单
//     */
//    @OnClick(R.id.tvWorkerGrabSingle)
//    public void workerGrabSingle() {
//        graBorder();
//    }

    /**
     * 获取需求详情
     */
    private void getRequireDetails() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(WorkerDemandDetailsActivity.this);
        presenter.onCreate();
        presenter.getrequiredetails(id);
        presenter.attachView(requireDetailsView);
    }

    private ClientBaseView<Object> requireDetailsView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerDemandDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                requireDetailsResult = (RequireDetailsResult) GsonUtil.parseJson(baseBean.getData(), RequireDetailsResult.class);
                initViewData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "requireDetailsView==" + result);
        }
    };

    /**
     * 加载页面数据
     */
    private void initViewData() {
        if (requireDetailsResult.getIsgrab() == 1) {
            //未抢单
            tvWorkerGrabSingle.setVisibility(View.GONE);
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.VISIBLE);
            tvGrabSingleWD.setText("抢单");
            tvSeeOrderWD.setVisibility(View.GONE);
            tvSeeOrderWD.setText("查看订单");
        } else if (requireDetailsResult.getIsgrab() == 2) {
            //抢单中
            tvWorkerGrabSingle.setVisibility(View.VISIBLE);
            tvWorkerGrabSingle.setText("抢单中");
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvSeeOrderWD.setVisibility(View.VISIBLE);
            tvSeeOrderWD.setText("取消抢单");
        } else if (requireDetailsResult.getIsgrab() == 3) {
            //抢单失败
            tvWorkerGrabSingle.setVisibility(View.VISIBLE);
            tvWorkerGrabSingle.setText("抢单失败");
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvSeeOrderWD.setVisibility(View.GONE);
        } else if (requireDetailsResult.getIsgrab() == 4) {
            //抢单成功
            tvWorkerGrabSingle.setVisibility(View.GONE);
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvGrabSingleWD.setText("抢单成功");
            tvSeeOrderWD.setVisibility(View.VISIBLE);
            tvSeeOrderWD.setText("查看订单");
        } else if (requireDetailsResult.getIsgrab() == 5) {
            //指定工人下单
            tvWorkerGrabSingle.setVisibility(View.GONE);
            llOrderShow.setVisibility(View.VISIBLE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvSeeOrderWD.setVisibility(View.GONE);
        } else if (requireDetailsResult.getIsgrab() == 6) {
            //抢单成功
            tvWorkerGrabSingle.setVisibility(View.VISIBLE);
            tvWorkerGrabSingle.setText("接单成功");
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvSeeOrderWD.setVisibility(View.VISIBLE);
            tvSeeOrderWD.setText("查看订单");
        } else {
            tvWorkerGrabSingle.setVisibility(View.GONE);
            llOrderShow.setVisibility(View.GONE);
            tvGrabSingleWD.setVisibility(View.GONE);
            tvSeeOrderWD.setVisibility(View.GONE);
        }
        tvWorkerDemandTitle.setText(requireDetailsResult.getTitle());
        tvWorkerDemandTime.setText(HelperUtil.StringDateToSingle(requireDetailsResult.getCreate_date() + " "));
        if (requireDetailsResult.getRequire_type().equals("点工")) {
            tvWorkerDemandType.setText("点工");
            llWorkerDemandDayPrice.setVisibility(View.VISIBLE);
            llWorkerDemandGotoPrice.setVisibility(View.GONE);
        } else {
            tvWorkerDemandType.setText("包工");
            llWorkerDemandDayPrice.setVisibility(View.GONE);
            llWorkerDemandGotoPrice.setVisibility(View.VISIBLE);
        }
        tvWorkerDemandNeedType.setText(requireDetailsResult.getWorker_types());
        tvWorkerDemandDayPrice.setText(requireDetailsResult.getPrice());
        tvWorkerDemandGotoPrice.setText(requireDetailsResult.getGoto_price());
        tvWorkerDemandWorkArea.setText(requireDetailsResult.getAddress());
        tvWorkerDemandWorkTime.setText(HelperUtil.StringDateToSingle(requireDetailsResult.getBegin_date() + " ") + "--" + HelperUtil.StringDateToSingle(requireDetailsResult.getEnd_date() + " "));
        tvWorkerDemandDescription.setText(requireDetailsResult.getDescription());
        tvWorkerDemandUser.setText(requireDetailsResult.getContacts());
        tvWorkerDemandPhone.setText(requireDetailsResult.getContact_number());
    }

    /**
     * 拒单
     */
    @OnClick(R.id.tvRejectSingleWD)
    public void rejectSingleWD() {
        intent = new Intent(WorkerDemandDetailsActivity.this, RejectSingleReasonActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 接单
     */
    @OnClick(R.id.tvOrdersWD)
    public void ordersWD() {
        receiveClient();
    }

    /**
     * 工人接单
     */
    private void receiveClient() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(WorkerDemandDetailsActivity.this);
        presenter.onCreate();
        presenter.receive(id);
        presenter.attachView(receiveView);
    }

    private ClientBaseView<Object> receiveView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerDemandDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                //成功显示dialog
                ordersSuccess();
                llOrderShow.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "receiveView--" + result);
        }
    };

    /**
     * 显示接单成功
     */
    private void ordersSuccess() {
        dialog = new Dialog(WorkerDemandDetailsActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_orders_success, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerDemandDetailsActivity.this) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(WorkerDemandDetailsActivity.this) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 抢单new
     */
    @OnClick(R.id.tvGrabSingleWD)
    public void grabSingleWD() {
        graBorder();
    }

    /**
     * 显示已抢单
     */
    private void grabSingle() {
        dialog = new Dialog(WorkerDemandDetailsActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_grab_single, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerDemandDetailsActivity.this) * 0.8);
        p.height = (int) (HelperUtil.getScreenHeight(WorkerDemandDetailsActivity.this) * 0.4);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 查看订单
     */
    @OnClick(R.id.tvSeeOrderWD)
    public void seeOrderWD() {
        if (tvSeeOrderWD.getText().toString().equals("查看订单")) {
            Intent intent = new Intent(WorkerDemandDetailsActivity.this, WorkerOrderInfoActivity.class);
            intent.putExtra("id", requireDetailsResult.getOrder_id());
            startActivity(intent);
        } else {
            //取消抢单
            diamissOrdersClient();
        }
    }

    /**
     * 取消抢单
     */
    private void diamissOrdersClient() {
        presenter = new PresenterImpl<Object>(WorkerDemandDetailsActivity.this);
        presenter.onCreate();
        presenter.cancelorder(String.valueOf(id));
        presenter.attachView(cancelOrderView);
    }

    private ClientBaseView<Object> cancelOrderView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(WorkerDemandDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                dismissOrders();
                getRequireDetails();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "cancelOrderView==" + result);
        }
    };

    /**
     * 取消抢单成功提示框
     */
    private void dismissOrders() {
        dialog = new Dialog(WorkerDemandDetailsActivity.this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_dismiss_orders, null);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(WorkerDemandDetailsActivity.this) * 0.8);
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 抢单
     */
    private void graBorder() {
        mSVProgressHUD.showWithStatus("抢单中...");
        presenter = new PresenterImpl<Object>(WorkerDemandDetailsActivity.this);
        presenter.onCreate();
        presenter.graborder(id);
        presenter.attachView(graBorderView);
    }

    private ClientBaseView<Object> graBorderView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            if (baseBean.getState() == 1) {
                grabSingle();
                tvWorkerGrabSingle.setText("已抢单");
                tvWorkerGrabSingle.setBackgroundResource(R.drawable.circle_grab_yellow);
            } else {
                Toast.makeText(WorkerDemandDetailsActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "graBorderView==" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatWorkerDemandDetails() {
        if (jp != null) {
            intent = new Intent(WorkerDemandDetailsActivity.this, WorkerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            WorkerDemandDetailsActivity.this.finish();
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
                    intent = new Intent(WorkerDemandDetailsActivity.this, WorkerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    WorkerDemandDetailsActivity.this.finish();
                }
                bl = true;
            }
        }
        return bl;
    }
}
