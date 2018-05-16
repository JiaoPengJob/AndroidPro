package com.tch.kuwanx.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gavin.com.library.PowerfulStickyDecoration;
import com.gavin.com.library.listener.PowerGroupListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.Points;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.AppuserTransactionResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 交易明细
 */
public class TransactionDetailsActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvTransactionDetails)
    RecyclerView rvTransactionDetails;
    @BindView(R.id.refreshTransactionDetails)
    SmartRefreshLayout refreshTransactionDetails;

    private CommonAdapter tranDetailsAdapter;
    private List<Points> list = new ArrayList<>();
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("交易明细");
        initTransactionDetails();
        refreshTransactionDetails.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAppuserTransactionListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getAppuserTransactionListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getAppuserTransactionListHttp();
    }

    /**
     * 加载列表
     */
    private void initTransactionDetails() {
        rvTransactionDetails.setLayoutManager(new LinearLayoutManager(this));
        rvTransactionDetails.setAdapter(tranDetailsAdapter = new CommonAdapter<Points>(this,
                R.layout.item_tran_record, list) {
            @Override
            protected void convert(ViewHolder holder, Points item, int position) {
                AppuserTransactionResult.ResultBean at = (AppuserTransactionResult.ResultBean) item.getItem();
                if (at.getTransaction_source().equals("1")) {
                    //微信
                    holder.setImageResource(R.id.ivTranRecordType, R.drawable.weixin_dd);
                } else if (at.getTransaction_source().equals("2")) {
                    //支付宝
                    holder.setImageResource(R.id.ivTranRecordType, R.drawable.recharge_apay);
                } else if (at.getTransaction_source().equals("3")) {
                    //其他
                    holder.setImageResource(R.id.ivTranRecordType, R.drawable.umeng_socialize_delete);
                }

                if (at.getTransaction_mode().equals("1")) {
                    holder.setText(R.id.tvPointResName, "交押金");
                    holder.setText(R.id.tvPointResNum, "-" + at.getTransaction_amt());
                    holder.setTextColor(R.id.tvPointResNum, Color.parseColor("#333333"));
                } else if (at.getTransaction_mode().equals("2")) {
                    holder.setText(R.id.tvPointResName, "退押金");
                    holder.setText(R.id.tvPointResNum, "+" + at.getTransaction_amt());
                    holder.setTextColor(R.id.tvPointResNum, Color.parseColor("#FFDA44"));
                } else if (at.getTransaction_mode().equals("3")) {
                    holder.setText(R.id.tvPointResName, "充值");
                    holder.setText(R.id.tvPointResNum, "+" + at.getTransaction_amt());
                    holder.setTextColor(R.id.tvPointResNum, Color.parseColor("#FFDA44"));
                } else if (at.getTransaction_mode().equals("4")) {
                    holder.setText(R.id.tvPointResName, "购买");
                    holder.setText(R.id.tvPointResNum, "-" + at.getTransaction_amt());
                    holder.setTextColor(R.id.tvPointResNum, Color.parseColor("#333333"));
                }

                holder.setText(R.id.tvPointResTime, Utils.times(at.getTransaction_time(), "MM月dd日 HH:ss:mm"));
            }
        });

        PowerGroupListener listener = new PowerGroupListener() {
            @Override
            public String getGroupName(int position) {
                return list.get(position).getDate();
            }

            @Override
            public View getGroupView(int position) {
                //获取自定定义的组View
                View view = getLayoutInflater().inflate(R.layout.item_group_tran, null, false);
                TextView tvTranDate = (TextView) view.findViewById(R.id.tvTranDate);
                tvTranDate.setText(list.get(position).getDate());
                return view;
            }
        };
        PowerfulStickyDecoration decoration = PowerfulStickyDecoration.Builder
                .init(listener)
                .setGroupHeight(120)       //设置高度
                .isAlignLeft(true)                                 //靠右边显示   默认左边
                .setGroupBackground(Color.parseColor("#F2F2F2"))    //设置背景   默认透明
                .setDivideColor(Color.parseColor("#F2F2F2"))        //分割线颜色
                .setDivideHeight(1)       //分割线高度
                .build();
        rvTransactionDetails.addItemDecoration(decoration);
    }

    /**
     * 用户交易明细
     */
    private void getAppuserTransactionListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(TransactionDetailsActivity.this));
        String params = EncryptionUtil.getParameter(TransactionDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "message/getAppuserTransactionList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getAppuserTransactionList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        TransactionDetailsActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshTransactionDetails != null) {
                            refreshTransactionDetails.finishLoadmore();
                            refreshTransactionDetails.finishRefresh();
                        }

                        Toasty.warning(TransactionDetailsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshTransactionDetails != null) {
                            refreshTransactionDetails.finishLoadmore();
                            refreshTransactionDetails.finishRefresh();
                        }

                        AppuserTransactionResult appuserTransactionResult =
                                (AppuserTransactionResult) GsonUtil.json2Object(response, AppuserTransactionResult.class);
                        if (appuserTransactionResult != null
                                && appuserTransactionResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                list.clear();
                                for (AppuserTransactionResult.ResultBean item : appuserTransactionResult.getResult()) {
                                    try {
                                        list.add(new Points(
                                                Utils.longToString(Long.parseLong(
                                                        item.getTransaction_time()
                                                ), "yyyy-MM"), item
                                        ));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                tranDetailsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(TransactionDetailsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void TranDetailBack() {
        TransactionDetailsActivity.this.finish();
    }
}
