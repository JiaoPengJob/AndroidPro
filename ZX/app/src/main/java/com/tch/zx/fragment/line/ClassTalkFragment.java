package com.tch.zx.fragment.line;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tch.zx.R;
import com.tch.zx.adapter.contacts.CommentDynamicAdapter;
import com.tch.zx.http.bean.result.GetDynamicCommentListResult;
import com.tch.zx.util.HelperUtil;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 精品小课/讨论
 */

public class ClassTalkFragment extends Fragment {
    /**
     * 数据列表
     */
    @BindView(R.id.rv_sub_talks)
    RecyclerView rv_sub_talks;

    /**
     * 评论列表适配器
     */
    private CommentDynamicAdapter commentDynamicAdapter;
    private List<GetDynamicCommentListResult.ResponseObjectBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_module_player_talk_info, container, false);
        ButterKnife.bind(this, view);
        //设置输入框在输入法上面
        View decorView = getActivity().getWindow().getDecorView();
        View contentView = getActivity().findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(HelperUtil.getGlobalLayoutListener(decorView, contentView));
        list = new ArrayList<GetDynamicCommentListResult.ResponseObjectBean>();
        setRecyclerViewData();
        return view;
    }

    /**
     * 加载评论信息列表数据
     */
    private void setRecyclerViewData() {
        commentDynamicAdapter = new CommentDynamicAdapter(getContext(), list);
        rv_sub_talks.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_sub_talks.addItemDecoration(new RecyclerViewDecoration(getContext(), "#949494", 1, true));
        rv_sub_talks.setAdapter(commentDynamicAdapter);
        rv_sub_talks.setFocusable(false);
    }
}