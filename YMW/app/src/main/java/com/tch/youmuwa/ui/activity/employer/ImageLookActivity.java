package com.tch.youmuwa.ui.activity.employer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.adapter.PictureSlidePagerAdapter;
import com.tch.youmuwa.ui.activity.BaseActivtiy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageLookActivity extends BaseActivtiy {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_indicator)
    TextView tv_indicator;

    private List<String> urlList;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_look);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        urlList = new ArrayList<String>();
        if (getIntent().getStringArrayListExtra("urls") != null) {
            index = getIntent().getIntExtra("index", 0);
            urlList = getIntent().getStringArrayListExtra("urls");
            viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager(), urlList));
            viewPager.setCurrentItem(index);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    tv_indicator.setText(String.valueOf(position + 1) + "/" + urlList.size());
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
}
