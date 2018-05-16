package com.tch.kuwanx.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.MemberGradeResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.btTitleFeatures)
    Button btTitleFeatures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMemberGradeHttp();
    }

    private void initView() {
        tvTitleContent.setText("余额");
        btTitleFeatures.setText("交易记录");
        btTitleFeatures.setVisibility(View.VISIBLE);
    }

    /**
     * 交易记录
     */
    @OnClick(R.id.btTitleFeatures)
    public void orderReconds() {
        Intent intent = new Intent(WalletActivity.this, TransactionDetailsActivity.class);
        startActivity(intent);
    }

    /**
     * 充值
     */
    @OnClick(R.id.btRecharge)
    public void recharge() {
        Intent intent = new Intent(WalletActivity.this, RechargeActivity.class);
        startActivity(intent);
    }

    /**
     * 退押金
     */
    @OnClick(R.id.btRefundDeposit)
    public void btRefundDeposit() {
        Intent intent = new Intent(WalletActivity.this, RefundDepositActivity.class);
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void walletBack() {
        WalletActivity.this.finish();
    }

    /**
     * 我的钱包余额
     */
    private void getMemberGradeHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DaoUtils.getUserId(WalletActivity.this));
        String params = EncryptionUtil.getParameter(WalletActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getMemberGrade.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getMemberGrade")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        WalletActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toasty.warning(WalletActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        MemberGradeResult memberGradeResult =
                                (MemberGradeResult) GsonUtil.json2Object(response, MemberGradeResult.class);
                        if (memberGradeResult != null
                                && memberGradeResult.getRet().equals("1")) {
                            initHttpData(memberGradeResult.getResult());
                        } else {
                            Toasty.warning(WalletActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }

    @BindView(R.id.tvWalletAvailable)
    TextView tvWalletAvailable;
    @BindView(R.id.tvWalletAll)
    TextView tvWalletAll;

    /**
     * 加载页面数据
     */
    private void initHttpData(MemberGradeResult.ResultBean item) {
        tvWalletAvailable.setText(item.getAvailablenum());
        tvWalletAll.setText(item.getAccountnum());
    }
}
