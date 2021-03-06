package com.tch.zx.activity.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.tch.zx.R;
import com.tch.zx.activity.BaseActivity;
import com.tch.zx.adapter.message.GroupChatAdapter;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.util.ActionItem;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;
import com.tch.zx.view.TitlePopup;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * 群聊消息提醒页面
 */
public class TogetherChatActivity extends BaseActivity {

    /**
     * 数据列表
     */
    @BindView(R.id.irrv_together_msg_receive)
    SwipeMenuRecyclerView irrv_together_msg_receive;

    /**
     * 适配器
     */
    private GroupChatAdapter groupChatAdapter;
    private GetGroupListResult getGroupListResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_together_chat);
        ButterKnife.bind(this);

        setData();
    }

    /**
     * 加载列表数据
     */
    private void setData() {
        groupChatAdapter = new GroupChatAdapter(getContext(), getGroupListResult.getResponseObject(),"");
        irrv_together_msg_receive.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        irrv_together_msg_receive.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 1, false));
        irrv_together_msg_receive.setAdapter(groupChatAdapter);
    }


    /**
     * 标题栏后退
     */
    @OnClick(R.id.ll_together_msg_title_return)
    public void returnBack() {
        TogetherChatActivity.this.finish();
    }
}
