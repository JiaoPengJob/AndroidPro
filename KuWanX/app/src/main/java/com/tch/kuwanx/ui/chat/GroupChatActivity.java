package com.tch.kuwanx.ui.chat;

import android.os.Bundle;

import com.tch.kuwanx.R;
import com.tch.kuwanx.ui.BaseActivity;

import butterknife.ButterKnife;

/**
 * 群组聊天的页面
 * 弃用
 */
public class GroupChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }
}
