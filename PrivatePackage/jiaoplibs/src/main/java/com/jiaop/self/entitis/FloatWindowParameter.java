package com.jiaop.self.entitis;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

/**
 * Created by jiaop
 * 任意界面悬浮窗参数类
 */

public class FloatWindowParameter {
    /**
     * 文本对象
     * 必须设置
     */
    private Context content;
    /**
     * 显示的View
     * 和viewId选一
     */
    private View view;
    /**
     * 唯一标识
     * 必须设置
     */
    private String tag;
    /**
     * 显示的布局
     * 和view选一
     */
    private int viewId = 0;
    /**
     * 自定义宽度
     */
    private int width = Screen.width;
    /**
     * 自定义宽度百分比
     */
    private float widthRatio = 0f;
    /**
     * 自定义高度
     */
    private int height = Screen.height;
    /**
     * 自定义高度百分比
     */
    private float heightRatio = 0f;
    /**
     * x偏移量
     */
    private int x = 0;
    /**
     * x偏移量百分比
     */
    private float xRatio = 0f;
    /**
     * y偏移量
     */
    private int y = 0;
    /**
     * y偏移量百分比
     */
    private float yRatio = 0f;
    /**
     * 是否显示
     * true时为：指定页面显示
     * false时为：指定页面不显示，其他页面显示
     */
    private boolean isShow = true;
    /**
     * 指定的页面
     */
    private Class[] activities;
    /**
     * 是否在桌面显示
     */
    private boolean isShowDesktop = false;
    /**
     * 拖动的样式
     * MoveType.slide : 可拖动，释放后自动贴边 （默认）
     * MoveType.back : 可拖动，释放后自动回到原位置
     * MoveType.active : 可拖动
     * MoveType.inactive : 不可拖动
     */
    private int moveType = MoveType.slide;
    /**
     * 动画时长
     */
    private long duration = 0l;
    /**
     * 减速插值器
     */
    private TimeInterpolator interpolator = new AccelerateInterpolator();

    public Context getContent() {
        return content;
    }

    public void setContent(Context content) {
        this.content = content;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(float widthRatio) {
        this.widthRatio = widthRatio;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(float heightRatio) {
        this.heightRatio = heightRatio;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getxRatio() {
        return xRatio;
    }

    public void setxRatio(float xRatio) {
        this.xRatio = xRatio;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getyRatio() {
        return yRatio;
    }

    public void setyRatio(float yRatio) {
        this.yRatio = yRatio;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public Class[] getActivities() {
        return activities;
    }

    public void setActivities(Class... activities) {
        this.activities = activities;
    }

    public boolean isShowDesktop() {
        return isShowDesktop;
    }

    public void setShowDesktop(boolean showDesktop) {
        isShowDesktop = showDesktop;
    }

    public int getMoveType() {
        return moveType;
    }

    public void setMoveType(int moveType) {
        this.moveType = moveType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }
}
