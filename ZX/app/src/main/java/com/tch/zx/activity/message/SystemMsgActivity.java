package com.tch.zx.activity.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tch.zx.R;
import com.tch.zx.adapter.SystemMsgAdapter;
import com.tch.zx.util.ActionItem;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;
import com.tch.zx.view.TitlePopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统消息展示页
 */
public class SystemMsgActivity extends AppCompatActivity {

    @BindView(R.id.ivSystemAddMenu)
    ImageView ivSystemAddMenu;
    @BindView(R.id.rvSystemMsgContent)
    RecyclerView rvSystemMsgContent;
    @BindView(R.id.refreshSystemMsgContent)
    SmartRefreshLayout refreshSystemMsgContent;

    private TitlePopup titlePopup;
    private SystemMsgAdapter systemMsgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_system_msg);
        //集成使用Butterknife
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //实例化标题栏弹窗
        titlePopup = new TitlePopup(SystemMsgActivity.this, (int) Math.floor(HelperUtil.getScreenWidth(SystemMsgActivity.this) / 2), 400);
        initMenuData();

        setListData();
    }

    /**
     * 加载列表数据
     */
    private void setListData() {

        List<String> list = new ArrayList<String>();
        list.add("0");
        list.add("1");
        list.add("2");

        systemMsgAdapter = new SystemMsgAdapter(SystemMsgActivity.this, list);
        rvSystemMsgContent.setLayoutManager(new LinearLayoutManager(SystemMsgActivity.this));
        //设置分割线
        rvSystemMsgContent.addItemDecoration(new RecyclerViewDecoration(SystemMsgActivity.this, "#949494", 0, false));
        rvSystemMsgContent.setAdapter(systemMsgAdapter);
        systemMsgAdapter.setOnItemClickListener(new SystemMsgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    /**
     * 加号点击
     */
    @OnClick(R.id.ivSystemAddMenu)
    public void systemAddMenu() {
        titlePopup.show(ivSystemAddMenu);
    }

    /**
     * 初始化窗口数据
     */
    private void initMenuData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(SystemMsgActivity.this, "添加好友", R.mipmap.add_friend_menu));
        titlePopup.addAction(new ActionItem(SystemMsgActivity.this, "发起群聊", R.mipmap.togeter_chat_menu));
        titlePopup.addAction(new ActionItem(SystemMsgActivity.this, "邀请好友", R.mipmap.send_friends_menu));
    }

    /**
     * 返回
     */
    @OnClick(R.id.llSystemBack)
    public void systemBack() {
        SystemMsgActivity.this.finish();
    }

}
