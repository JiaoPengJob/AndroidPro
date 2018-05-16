package com.tch.kuwanx.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.tch.kuwanx.utils.PixelsUtil;

/**
 * Created by Jiaop on 2017/10/26.
 */

public class TranslucentScrollView extends ScrollView {

    static final String TAG = "TranslucentScrollView";

    //伸缩视图
    private View zoomView;
    //伸缩视图初始高度
    private int zoomViewInitHeight = 0;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    //渐变的视图
    private View transView;
    //渐变颜色
    private int transColor = Color.WHITE;
    //渐变开始位置
    private int transStartY = 50;
    //渐变结束位置
    private int transEndY = 300;

    //渐变开始默认位置，Y轴，50dp
    private final int DFT_TRANSSTARTY = 50;
    //渐变结束默认位置，Y轴，300dp
    private final int DFT_TRANSENDY = 300;

    private TranslucentChangedListener translucentChangedListener;

    public interface TranslucentChangedListener {
        /**
         * 透明度变化，取值范围0-255
         *
         * @param transAlpha
         */
        void onTranslucentChanged(int transAlpha);
    }

    public TranslucentScrollView(Context context) {
        super(context);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslucentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTranslucentChangedListener(TranslucentChangedListener translucentChangedListener) {
        this.translucentChangedListener = translucentChangedListener;
    }

    /**
     * 设置伸缩视图
     *
     * @param zoomView
     */
    public void setPullZoomView(View zoomView) {
        this.zoomView = zoomView;
        zoomViewInitHeight = zoomView.getLayoutParams().height;
        if (zoomViewInitHeight == LayoutParams.MATCH_PARENT || zoomViewInitHeight == WindowManager.LayoutParams.WRAP_CONTENT) {
            zoomView.post(new Runnable() {
                @Override
                public void run() {
                    zoomViewInitHeight = TranslucentScrollView.this.zoomView.getHeight();
                }
            });
        }
    }

    /**
     * 设置渐变视图
     *
     * @param transView 渐变的视图
     */
    public void setTransView(View transView) {
        setTransView(transView, Color.parseColor("#FFDA44"), PixelsUtil.dip2px(getContext(), DFT_TRANSSTARTY), PixelsUtil.dip2px(getContext(), DFT_TRANSENDY));
    }

    /**
     * 设置渐变视图
     *
     * @param transView  渐变的视图
     * @param transColor 渐变颜色
     * @param transEndY  渐变结束位置
     */
    public void setTransView(View transView, @ColorInt int transColor, int transStartY, int transEndY) {
        this.transView = transView;
        //初始视图-透明
        this.transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, 0));
        this.transStartY = transStartY;
        this.transEndY = transEndY;
        this.transColor = transColor;
        if (transStartY > transEndY) {
            throw new IllegalArgumentException("transStartY 不得大于 transEndY .. ");
        }
    }

    /**
     * 获取透明度
     *
     * @return
     */
    private int getTransAlpha() {
        float scrollY = getScrollY();
        if (transStartY != 0) {
            if (scrollY <= transStartY) {
                return 0;
            } else if (scrollY >= transEndY) {
                return 255;
            } else {
                return (int) ((scrollY - transStartY) / (transEndY - transStartY) * 255);
            }
        } else {
            if (scrollY >= transEndY) {
                return 255;
            }
            return (int) ((transEndY - scrollY) / transEndY * 255);
        }
    }

    /**
     * 重置ZoomView
     */
    private void resetZoomView() {
        final ViewGroup.LayoutParams lp = zoomView.getLayoutParams();
        final float h = zoomView.getLayoutParams().height;// ZoomView当前高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.height = (int) (h - (h - zoomViewInitHeight) * cVal);
                zoomView.setLayoutParams(lp);
            }
        });
        anim.start();
    }

    private OnScrollListener onScrollListener;

    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int transAlpha = getTransAlpha();

        if (transView != null) {
            transView.setBackgroundColor(ColorUtils.setAlphaComponent(transColor, transAlpha));
        }
        if (translucentChangedListener != null) {
            translucentChangedListener.onTranslucentChanged(transAlpha);
        }
    }

    /**
     * 滚动的回调接口
     *
     * @author xiaanming
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回ScrollView滑动的Y方向距离
         *
         * @param scrollY 、
         */
        public void onScroll(int scrollY);
    }

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        if (zoomView != null) {
            ViewGroup.LayoutParams params = zoomView.getLayoutParams();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    //手指离开后恢复图片
                    mScaling = false;
                    resetZoomView();
                    handler.sendMessageDelayed(handler.obtainMessage(), 5);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mScaling) {
                        if (getScrollY() == 0) {
                            mFirstPosition = event.getY();
                        } else {
                            break;
                        }
                    }

                    int distance = (int) ((event.getY() - mFirstPosition) * 0.6);
                    if (distance < 0) {
                        break;
                    }
                    mScaling = true;
                    params.height = zoomViewInitHeight + distance;

                    zoomView.setLayoutParams(params);
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int scrollY = TranslucentScrollView.this.getScrollY();
            //此时的距离和记录下的距离不相等，在隔2毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 2);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }
        }
    };

}
