package com.tch.zx.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页推荐页中顶部图片显示的加载
 */

public class RecommentTitleAdapter extends PagerAdapter {

    private Context context;

    /**
     * 数据集合
     */
    private List<String> photos = new ArrayList<String>();

    /**
     * 构造函数
     *
     * @param recommentTitlePhotos 需要加载的图片数据集合
     */
    public RecommentTitleAdapter(Context context, List<String> recommentTitlePhotos) {
        this.context = context;
        this.photos = recommentTitlePhotos;
    }

    /**
     * 返回当前有效视图的个数
     *
     * @return
     */
    @Override
    public int getCount() {
        return photos.size();
    }

    /**
     * 判断instantiateItem(ViewGroup, int)函数所返回来的Key与一个页面视图是否是代表的同一个视图
     * (即它俩是否是对应的，对应的表示同一个View)
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 移除一个给定位置的页面
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 创建指定位置的页面视图
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //代码定义ImageView，用来加载图片流
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        Glide.with(context).load(photos.get(position)).into(imageView);
        return imageView;
    }
}
