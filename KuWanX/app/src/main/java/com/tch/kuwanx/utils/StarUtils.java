package com.tch.kuwanx.utils;

import android.widget.ImageView;

import com.tch.kuwanx.R;

import java.util.List;

/**
 * Created by jiaop on 2017/11/23.
 * 星条显示工具类
 */

public class StarUtils {

    /**
     * 显示（替换）星条中星星
     */
    public static void showSomeStars(int number, List<ImageView> imageViews) {
        if (number == 1) {
            imageViews.get(0).setImageResource(R.drawable.pentagram);
            for (int i = 1; i < imageViews.size(); i++) {
                imageViews.get(i).setImageResource(R.drawable.pentagram_unsel);
            }
        } else if (number == 2) {
            for (int i = 0; i < imageViews.size(); i++) {
                if (i < 2) {
                    imageViews.get(i).setImageResource(R.drawable.pentagram);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.pentagram_unsel);
                }
            }
        } else if (number == 3) {
            for (int i = 0; i < imageViews.size(); i++) {
                if (i < 3) {
                    imageViews.get(i).setImageResource(R.drawable.pentagram);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.pentagram_unsel);
                }
            }
        } else if (number == 4) {
            for (int i = 0; i < imageViews.size(); i++) {
                if (i < 4) {
                    imageViews.get(i).setImageResource(R.drawable.pentagram);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.pentagram_unsel);
                }
            }
        } else if (number == 5) {
            for (int i = 0; i < imageViews.size(); i++) {
                imageViews.get(i).setImageResource(R.drawable.pentagram);
            }
        } else {
            for (ImageView img : imageViews) {
                img.setImageResource(R.drawable.pentagram_unsel);
            }
        }
    }
}
