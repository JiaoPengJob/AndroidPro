package com.tch.kuwanx.ui.release;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.zhy.adapter.recyclerview.CommonAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jiaop on 2017/10/24.
 * 选择物品的弹出框
 */
public class ResFragment extends Fragment {

    @BindView(R.id.rvReleasePer)
    RecyclerView rvReleasePer;

    private View viewRoot;
    private CommonAdapter perAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_res, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        Logger.wtf("数值==" + getArguments().getInt("pageIndex"));
    }


}
