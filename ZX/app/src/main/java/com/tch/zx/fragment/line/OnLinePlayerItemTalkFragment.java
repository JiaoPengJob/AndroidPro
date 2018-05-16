package com.tch.zx.fragment.line;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tch.zx.R;
import com.tch.zx.adapter.line.OnLinePlayerItemTalkAdapter;
import com.tch.zx.enjoy.fragment.EmotionMainFragment;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 热门直播/直播视频展示页
 */

public class OnLinePlayerItemTalkFragment extends Fragment {

    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 讨论信息列表
     */
    @BindView(R.id.rv_talk_content)
    RecyclerView rv_talk_content;
    /**
     * 输入框
     */
    @BindView(R.id.et_input_enjoy_player_talk)
    EditText et_input_enjoy_player_talk;
    @BindView(R.id.rlOnLinePlayerItemTalk)
    RelativeLayout rlOnLinePlayerItemTalk;
    /**
     * 表情按钮
     */
    @BindView(R.id.iv_enjoy_talk)
    ImageView iv_enjoy_talk;
    /**
     * 展示表情符号的布局
     */
    @BindView(R.id.fl_emotionview_main)
    FrameLayout fl_emotionview_main;
    private EmotionMainFragment emotionMainFragment;

    /**
     * 直播讨论的适配器
     */
    OnLinePlayerItemTalkAdapter onLinePlayerItemTalkAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_online_player_talk, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);

        //设置输入框在输入法上面
        View decorView = getActivity().getWindow().getDecorView();
        View contentView = getActivity().findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(HelperUtil.getGlobalLayoutListener(decorView, contentView));
        et_input_enjoy_player_talk.setFocusable(true);
        et_input_enjoy_player_talk.setFocusableInTouchMode(true);
        et_input_enjoy_player_talk.requestFocus();
        InputMethodManager im = ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(rlOnLinePlayerItemTalk, 0);
        initEmotionMainFragment();

        setListData();

        return viewRoot;
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(et_input_enjoy_player_talk);
        emotionMainFragment.bindToEmotionButton(iv_enjoy_talk);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    /**
     * 初始化监听器
     */
    public void initListentener() {

    }

//    @Override
//    public void onBackPressed() {
//        /**
//         * 判断是否拦截返回键操作
//         */
//        if (!emotionMainFragment.isInterceptBackPress()) {
//            super.onBackPressed();
//        }
//    }

    /**
     * 加载列表内容
     */
    private void setListData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        onLinePlayerItemTalkAdapter = new OnLinePlayerItemTalkAdapter(getContext(), list);
        rv_talk_content.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_talk_content.addItemDecoration(new RecyclerViewDecoration(getContext(), "#FFFFFF", 1, false));
        rv_talk_content.setAdapter(onLinePlayerItemTalkAdapter);
    }

}
