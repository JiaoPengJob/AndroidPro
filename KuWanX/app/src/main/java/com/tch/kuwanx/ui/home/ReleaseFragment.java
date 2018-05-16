package com.tch.kuwanx.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.kuwanx.R;

/**
 * Created by Jiaop on 2017/10/24.
 * 发布
 */
public class ReleaseFragment extends Fragment {

    private View viewRoot;

    public static ReleaseFragment getInstance() {
        ReleaseFragment sf = new ReleaseFragment();
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_release, null);
        return viewRoot;
    }
}
