package com.tch.kuwanx.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.DepositDetailResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 押金明细
 */
public class DepositDetailsActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;
    @BindView(R.id.rvDepositDetails)
    RecyclerView rvDepositDetails;
    @BindView(R.id.refreshDepositDetails)
    SmartRefreshLayout refreshDepositDetails;

    private CommonAdapter depositDetailsAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("押金明细");
        btTitleFeatures.setText("退款说明");
        btTitleFeatures.setVisibility(View.VISIBLE);
        initDepositDetails();
        refreshDepositDetails.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDepositDetailHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getDepositDetailHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getDepositDetailHttp();
    }

    private void initDepositDetails() {
        rvDepositDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDepositDetails.setAdapter(depositDetailsAdapter = new CommonAdapter<DepositDetailResult.ResultBean>(this,
                R.layout.item_deposit_details, new ArrayList<DepositDetailResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, DepositDetailResult.ResultBean item, int position) {
                if (item.getTransaction_mode().equals("1")) {
                    holder.setImageResource(R.id.ivDepDetType, R.drawable.weixin_dd);
                } else if (item.getTransaction_mode().equals("2")) {
                    holder.setImageResource(R.id.ivDepDetType, R.drawable.recharge_apay);
                }
                holder.setText(R.id.tvDepDetTime, item.getTransaction_time());
                if (item.getTransaction_source().equals("1")) {
                    holder.setText(R.id.tvDepDetPlus, "-" + item.getTransaction_amt());
                } else if (item.getTransaction_source().equals("2")) {
                    holder.setText(R.id.tvDepDetPlus, "+" + item.getTransaction_amt());
                }

                if (item.getTransaction_mode().equals("1")) {
                    if (item.getTransaction_source().equals("1")) {
                        holder.setText(R.id.tvPointResName, "微信支付押金");
                    } else if (item.getTransaction_source().equals("2")) {
                        holder.setText(R.id.tvPointResName, "微信退还押金");
                    }
                } else if (item.getTransaction_mode().equals("2")) {
                    if (item.getTransaction_source().equals("1")) {
                        holder.setText(R.id.tvPointResName, "支付宝支付押金");
                    } else if (item.getTransaction_source().equals("2")) {
                        holder.setText(R.id.tvPointResName, "支付宝退还押金");
                    }
                }
            }
        });
    }

    /**
     * 退款说明
     */
    @OnClick(R.id.btTitleFeatures)
    public void RefundInstructions() {

    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void depositDetailsBack() {
        DepositDetailsActivity.this.finish();
    }

    /**
     * 押金明细
     */
    private void getDepositDetailHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(DepositDetailsActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(DepositDetailsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getDepositDetail.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getDepositDetail")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        DepositDetailsActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshDepositDetails != null) {
                            refreshDepositDetails.finishLoadmore();
                            refreshDepositDetails.finishRefresh();
                        }
                        Toasty.warning(DepositDetailsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshDepositDetails != null) {
                            refreshDepositDetails.finishLoadmore();
                            refreshDepositDetails.finishRefresh();
                        }

                        DepositDetailResult depositDetailResult =
                                (DepositDetailResult) GsonUtil.json2Object(response, DepositDetailResult.class);
                        if (depositDetailResult != null
                                && depositDetailResult.getRet().equals("1")) {
                            if (isMore) {
                                depositDetailsAdapter.getDatas().addAll(depositDetailResult.getResult());
                                depositDetailsAdapter.notifyDataSetChanged();
                            } else {
                                depositDetailsAdapter.getDatas().clear();
                                depositDetailsAdapter.getDatas().addAll(depositDetailResult.getResult());
                                depositDetailsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.warning(DepositDetailsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

}
