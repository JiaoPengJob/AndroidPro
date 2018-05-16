package com.tch.kuwanx.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.SwapListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.DaoUtils;
import com.tch.kuwanx.utils.GsonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 置换记录
 */
public class ReplaceRecordsActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvReplaceRecords)
    RecyclerView rvReplaceRecords;
    @BindView(R.id.refreshReplaceRecords)
    SmartRefreshLayout refreshReplaceRecords;

    private CommonAdapter replaceRecordsAdapter;
    private boolean isMore = false;
    private int size = 10, index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_records);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleContent.setText("置换记录");
        initReplaceRecords();
        refreshReplaceRecords.setOnRefreshLoadmoreListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSwapListHttp();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        index += 1;
        getSwapListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        index = 1;
        getSwapListHttp();
    }

    /**
     * 加载置换列表
     */
    private void initReplaceRecords() {
        rvReplaceRecords.setLayoutManager(new LinearLayoutManager(ReplaceRecordsActivity.this));
        rvReplaceRecords.setAdapter(replaceRecordsAdapter = new CommonAdapter<SwapListResult.ResultBean>(ReplaceRecordsActivity.this,
                R.layout.item_mine_evaluate, new ArrayList<SwapListResult.ResultBean>()) {
            @Override
            protected void convert(ViewHolder holder, SwapListResult.ResultBean item, int position) {
                holder.setVisible(R.id.tvEvaluateContent, false);
                Glide.with(ReplaceRecordsActivity.this)
                        .load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3602249452,3475976671&fm=27&gp=0.jpg")
                        .apply(bitmapTransform(new CropCircleTransformation()))
                        .into((ImageView) holder.getView(R.id.ivReplaceAvatar));
                holder.setText(R.id.tvReplaceName, "名字");
                holder.setText(R.id.tvReplaceResName, item.getSwap_good());
                holder.setText(R.id.tvReplaceTime, item.getSwap_time());

                Glide.with(ReplaceRecordsActivity.this)
                        .load("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=500698217,2710650434&fm=27&gp=0.jpg")
                        .into((ImageView) holder.getView(R.id.ivReplaceResBottomPhoto));
                holder.setText(R.id.tvReplaceResBottomName, item.getSwap_good());
            }
        });
        replaceRecordsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void replaceRecordsBack() {
        ReplaceRecordsActivity.this.finish();
    }

    /**
     * 置换记录
     */
    private void getSwapListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("appuser_id", DaoUtils.getUserId(ReplaceRecordsActivity.this));
        map.put("pageSize", String.valueOf(size));
        map.put("pageIndex", String.valueOf(index));
        String params = EncryptionUtil.getParameter(ReplaceRecordsActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "user/getSwapList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSwapList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        ReplaceRecordsActivity.this, "查询中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshReplaceRecords != null) {
                            refreshReplaceRecords.finishLoadmore();
                            refreshReplaceRecords.finishRefresh();
                        }

                        Toasty.warning(ReplaceRecordsActivity.this, "查询失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshReplaceRecords != null) {
                            refreshReplaceRecords.finishLoadmore();
                            refreshReplaceRecords.finishRefresh();
                        }

                        SwapListResult swapListResult =
                                (SwapListResult) GsonUtil.json2Object(response, SwapListResult.class);
                        if (swapListResult != null
                                && swapListResult.getRet().equals("1")) {
                            if (isMore) {
                                replaceRecordsAdapter.getDatas().addAll(swapListResult.getResult());
                                replaceRecordsAdapter.notifyDataSetChanged();
                            } else {
                                replaceRecordsAdapter.getDatas().clear();
                                replaceRecordsAdapter.getDatas().addAll(swapListResult.getResult());
                                replaceRecordsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
