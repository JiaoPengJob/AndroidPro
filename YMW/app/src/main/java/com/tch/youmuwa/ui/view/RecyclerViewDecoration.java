package com.tch.youmuwa.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 绘制RecyclerView的分割线
 */
public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    //分割线高度
    private float dividerHeight;
    //绘制图形的画笔
    private Paint dividerPaint;
    //判断是否存在自定义的特殊情况
    private boolean special;

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param color   分割线颜色
     * @param height  分割线高度
     * @param special 是否存在自定义特殊情况
     */
    public RecyclerViewDecoration(Context context, String color, float height, boolean special) {
        dividerPaint = new Paint();
        dividerPaint.setColor(Color.parseColor(color));
        dividerHeight = height;
        this.special = special;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = 0;
        if (special) {
            left = parent.getWidth() / 2 - 160;
        } else {
            left = parent.getPaddingLeft();
        }
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
