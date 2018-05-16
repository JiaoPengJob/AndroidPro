package com.tch.youmuwa.ui.activity.employer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.parameters.JpushParam;
import com.tch.youmuwa.bean.parameters.MsgParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.MsgResult;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.BaseActivtiy;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
import com.tch.youmuwa.ui.view.RecyclerViewDecoration;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivtiy {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refreshMsgCenter)
    SmartRefreshLayout refreshMsgCenter;
    @BindView(R.id.rvMsgCenter)
    RecyclerView rvMsgCenter;

    private PresenterImpl<Object> presenter;
    private SVProgressHUD mSVProgressHUD;//加载显示
    private TextView tvMsgCancel, tvMsgDetermine;
    private CommonAdapter adapter;
    private JpushParam jp;
    private List<MsgResult.MsgListBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMsgs();
    }

    /**
     * 初始化
     */
    private void initView() {
        title.setText("消息中心");
        list = new ArrayList<MsgResult.MsgListBean>();
        //初始化加载显示
        mSVProgressHUD = new SVProgressHUD(this);
        if (getIntent().getSerializableExtra("jp") != null) {
            jp = (JpushParam) getIntent().getSerializableExtra("jp");
        }
        refreshMsgCenter.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMsgs();
            }
        });
        getMsgs();
    }

    /**
     * 加载列表数据
     */
    private void initListData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMsgCenter.setLayoutManager(layoutManager);
        rvMsgCenter.addItemDecoration(new RecyclerViewDecoration(this, "#F0F0F0", 1, false));
        adapter = new CommonAdapter<MsgResult.MsgListBean>(this, R.layout.item_msg_center, list) {
            @Override
            protected void convert(ViewHolder holder, MsgResult.MsgListBean item, int position) {
                holder.setText(R.id.tvMsgInfo, item.getContent());
                if (item.getIs_read() == 0) {
                    holder.setVisible(R.id.viewIsRead, true);
                } else {
                    holder.setVisible(R.id.viewIsRead, false);
                }
            }
        };
        if (adapter != null) {
            rvMsgCenter.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                isRead(String.valueOf(list.get(position).getId()));
                Intent intent = new Intent(MessageCenterActivity.this, MsgInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("msg", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置为已读
     */
    private void isRead(String id) {
        MsgParam mp = new MsgParam(id);
        presenter = new PresenterImpl<Object>(MessageCenterActivity.this);
        presenter.onCreate();
        presenter.markmessageisread(mp);
        presenter.attachView(mpView);
    }

    private ClientBaseView<Object> mpView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            Log.e("TAG", "mpView--" + baseBean.getMsg().toString());
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "mpView--" + result);
        }
    };

    /**
     * 清空
     */
    @OnClick(R.id.ibMsgClear)
    public void msgClear() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //获取自定义布局
        View layout = getLayoutInflater().inflate(R.layout.dialog_msg_clear, null);
        tvMsgCancel = (TextView) layout.findViewById(R.id.tvMsgCancel);
        tvMsgDetermine = (TextView) layout.findViewById(R.id.tvMsgDetermine);
        dialog.setContentView(layout);
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (HelperUtil.getScreenWidth(this) * 0.8);
        p.height = HelperUtil.getScreenHeight(this) / 3;
        dialog.getWindow().setAttributes(p);
        tvMsgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvMsgDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                clearHasRead();
            }
        });
    }

    /**
     * 清空已读消息
     */
    private void clearHasRead() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(MessageCenterActivity.this);
        presenter.onCreate();
        presenter.clearmessages();
        presenter.attachView(clearMsgView);
    }

    private ClientBaseView<Object> clearMsgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(MessageCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                list.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                getMsgs();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            Log.e("Error", "clearMsgView--" + result);
        }
    };

    /**
     * 获取消息列表
     */
    private void getMsgs() {
        mSVProgressHUD.showWithStatus("加载中...");
        presenter = new PresenterImpl<Object>(MessageCenterActivity.this);
        presenter.onCreate();
        presenter.getmessagelist();
        presenter.attachView(msgView);
    }

    private List<Integer> msgSize = new ArrayList<Integer>();

    private ClientBaseView<Object> msgView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }
            if (refreshMsgCenter.isRefreshing()) {
                refreshMsgCenter.finishRefresh();
            }
            if (baseBean.getState() != 1) {
                Toast.makeText(MessageCenterActivity.this, baseBean.getMsg().toString(), Toast.LENGTH_SHORT).show();
            } else {
                MsgResult msg = (MsgResult) GsonUtil.parseJson(baseBean.getData(), MsgResult.class);
                list = msg.getMsg_list();
                for (MsgResult.MsgListBean mb : list) {
                    if (mb.getIs_read() == 0) {
                        msgSize.add(1);
                    }
                }
                ShortcutBadger.applyCount(MessageCenterActivity.this, msgSize.size());
                SharedPrefsUtil.putValue(MessageCenterActivity.this, "msgSize", msgSize.size());
                initListData();
            }
        }

        @Override
        public void onError(String result) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
            }

            Log.e("Error", "msgView--" + result);
        }
    };

    /**
     * 后退
     */
    @OnClick(R.id.ibRetreat)
    public void retreatMessageCenter() {
        if (jp != null) {
            if (jp.getUser_type().equals("1")) {
                Intent intent = new Intent(MessageCenterActivity.this, EmployerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MessageCenterActivity.this, WorkerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            MessageCenterActivity.this.finish();
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
                MessageCenterActivity.this.finish();
                if (jp != null) {
                    if (jp != null && jp.getUser_type().equals("1")) {
                        Intent intent = new Intent(MessageCenterActivity.this, EmployerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MessageCenterActivity.this, WorkerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                bl = true;
            }
        }
        return bl;
    }
}
