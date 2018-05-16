package com.tch.kuwanx.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Jiaop on 2017/10/26.
 * 圆角图片加载器
 */

public class GlideRoundedCornersImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(bitmapTransform(new RoundedCornersTransformation(20, 5,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

}
