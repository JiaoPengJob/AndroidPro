package com.tch.zx.fragment.line;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;
import com.tch.zx.adapter.line.ClassAdapter;
import com.tch.zx.bean.SmallListBean;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门直播/推荐
 */

public class ClassForPlayerFragment extends Fragment {

    @BindView(R.id.rv_fgt_purchassed)
    RecyclerView rv_fgt_purchassed;

    /**
     * Fragment父布局
     */
    private View viewRoot;

    /**
     * 列表适配器
     */
    private ClassAdapter classAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_class_for_player, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
//        setRvData();
        return viewRoot;
    }

    /**
     * 加载数据
     */
    private void setRvData(List<SmallListBean.ResponseObject> list) {
        classAdapter = new ClassAdapter(getContext(), list);
        rv_fgt_purchassed.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fgt_purchassed.addItemDecoration(new RecyclerViewDecoration(getContext(), "#EAEAEA", 30, false));
        rv_fgt_purchassed.setAdapter(classAdapter);
    }

}
