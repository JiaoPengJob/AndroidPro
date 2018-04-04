package com.jiaop.kotlin;

import android.content.Context;
import android.widget.ImageView;

import com.jiaop.libs.utils.JPGlideUtil;
import com.youth.banner.loader.ImageLoader;

/**
 * 重写图片加载器
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        JPGlideUtil.loadImg(context, (String) path, imageView);
    }

}
