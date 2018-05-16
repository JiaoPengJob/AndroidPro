package com.tch.zx.fragment.line;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;

/**
 * 精品小课/选集
 */

public class ClassDramaSeriesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_module_player_types_info, container, false);
        return view;
    }
}
