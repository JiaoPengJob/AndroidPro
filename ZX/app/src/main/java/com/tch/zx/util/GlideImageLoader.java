package com.tch.zx.util;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";

    @Override
    public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .into(galleryImageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}