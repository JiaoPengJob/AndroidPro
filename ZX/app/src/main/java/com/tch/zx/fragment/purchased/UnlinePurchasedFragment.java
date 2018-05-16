package com.tch.zx.fragment.purchased;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.adapter.purchased.PurchasedAdapter;
import com.tch.zx.adapter.purchased.UnlinePurchasedAdapter;
import com.tch.zx.view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已购/线下
 */

public class UnlinePurchasedFragment extends Fragment{
    @BindView(R.id.rv_fgt_purchassed)
    RecyclerView rv_fgt_purchassed;

    public View viewRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.fragment_purchased, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        setRvData();
        return viewRoot;
    }

    public void setRvData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add("");
        }
        //已购选项卡显示适配器
        UnlinePurchasedAdapter purchasedAdapter = new UnlinePurchasedAdapter(getContext(), list);
        rv_fgt_purchassed.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置分割线
        rv_fgt_purchassed.addItemDecoration(new RecyclerViewDecoration(getContext(), "#949494", 1, true));
        rv_fgt_purchassed.setAdapter(purchasedAdapter);
        purchasedAdapter.setOnItemClickListener(new UnlinePurchasedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), OnLinePlayerItemMainActivity.class);
                startActivity(intent);
            }
        });
    }
}