package com.tch.kuwanx.ui.mine.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tch.kuwanx.R;
import com.tch.kuwanx.https.EncryptionUtil;
import com.tch.kuwanx.https.HttpUtils;
import com.tch.kuwanx.result.SysMsgListResult;
import com.tch.kuwanx.ui.BaseActivity;
import com.tch.kuwanx.utils.GsonUtil;
import com.tch.kuwanx.view.TimeLineAdapter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * 系统消息
 */
public class SystemMsgActivity extends BaseActivity implements OnRefreshLoadmoreListener {

    @BindView(R.id.tvTitleContent)
    TextView tvTitleContent;
    @BindView(R.id.rvSystemMsg)
    RecyclerView rvSystemMsg;
    @BindView(R.id.refreshSystemMsg)
    SmartRefreshLayout refreshSystemMsg;

    private TimeLineAdapter mTimeLineAdapter;
    private boolean isMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSysMsgListHttp();
    }

    private void initView() {
        tvTitleContent.setText("系统消息");
        initSystemMsgs(new ArrayList<SysMsgListResult.ResultBean>());
        refreshSystemMsg.setOnRefreshLoadmoreListener(this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        isMore = true;
        getSysMsgListHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        isMore = false;
        getSysMsgListHttp();
    }

    private void initSystemMsgs(List<SysMsgListResult.ResultBean> list) {
        rvSystemMsg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSystemMsg.setHasFixedSize(true);
        mTimeLineAdapter = new TimeLineAdapter(list);
        rvSystemMsg.setAdapter(mTimeLineAdapter);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ibTitleBack)
    public void systemMsgBack() {
        SystemMsgActivity.this.finish();
    }

    /**
     * 系统消息
     */
    private void getSysMsgListHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        String params = EncryptionUtil.getParameter(SystemMsgActivity.this, map);
        EasyHttp.post(HttpUtils.URI_CENTER + "message/getSysMsgList.jhtml")
                .params("data", params)
                .accessToken(false)
                .timeStamp(false)
                .sign(false)
                .syncRequest(false)
                .cacheKey(this.getClass().getSimpleName() + "_getSysMsgList")
                .execute(new ProgressDialogCallBack<String>(HttpUtils.getIProgressDialog(
                        SystemMsgActivity.this, "获取中..."), true, true) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (refreshSystemMsg != null) {
                            refreshSystemMsg.finishLoadmore();
                            refreshSystemMsg.finishRefresh();
                        }
                        Toasty.warning(SystemMsgActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (refreshSystemMsg != null) {
                            refreshSystemMsg.finishLoadmore();
                            refreshSystemMsg.finishRefresh();
                        }

                        SysMsgListResult sysMsgListResult =
                                (SysMsgListResult) GsonUtil.json2Object(response, SysMsgListResult.class);
                        if (sysMsgListResult != null
                                && sysMsgListResult.getRet().equals("1")) {
                            if (isMore) {

                            } else {
                                initSystemMsgs(sysMsgListResult.getResult());
                            }
                        } else {
                            Toasty.warning(SystemMsgActivity.this, "获取失败！", Toast.LENGTH_SHORT, false).show();
                        }
                    }
                });
    }
}
