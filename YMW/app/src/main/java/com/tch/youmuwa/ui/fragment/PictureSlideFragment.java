package com.tch.youmuwa.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tch.youmuwa.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peng on 2017/9/18.
 */

public class PictureSlideFragment extends Fragment {

    @BindView(R.id.iv_main_pic)
    ImageView imageView;

    private String url;

    public static PictureSlideFragment newInstance(String url) {
        PictureSlideFragment f = new PictureSlideFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments() != null ? getArguments().getString("url") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_slide, container, false);
        ButterKnife.bind(this, v);
        Glide.with(getContext()).load(url).into(imageView);
        return v;
    }
}
