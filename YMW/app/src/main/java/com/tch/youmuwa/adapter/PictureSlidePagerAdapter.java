package com.tch.youmuwa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.tch.youmuwa.ui.fragment.PictureSlideFragment;

import java.util.List;

/**
 * Created by peng on 2017/9/18.
 */

public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> urlList;

    public PictureSlidePagerAdapter(FragmentManager fm, List<String> urlList) {
        super(fm);
        this.urlList = urlList;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
