package com.tch.kuwanx.ui.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.kuwanx.R;

import butterknife.ButterKnife;

/**
 * Created by Jiaop on 2017/10/24.
 * 详情
 */
public class DetailsFragment extends Fragment {

    private View viewRoot;
    private String goodId;

    public static DetailsFragment getInstance(String goodId) {
        DetailsFragment sf = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goodId", goodId);
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_details, null);
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    private void initView() {
        if (getArguments().getString("goodId") != null) {
            goodId = getArguments().getString("goodId");
        }
    }
}
