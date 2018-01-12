package com.jiaop.self.entitis;

import android.graphics.Color;

import com.jiaop.self.R;
import com.jiaop.self.views.TitleBar;

/**
 * Created by JiaoP
 * 标题栏样式参数
 */

public class TitleBarParameter {

    /**
     * 左边图标资源
     */
    private int leftImageResource = R.mipmap.title_back;
    /**
     * 左边文字
     */
    private String leftText = "";
    /**
     * 左边文字颜色
     */
    private int leftTextColor = Color.WHITE;
    /**
     * 中间文字标题
     */
    private String title = "";
    /**
     * 中间副标题文字
     */
    private String subTitle = "";
    /**
     * 是否竖向展示副标题
     */
    private boolean isSubVertical = true;
    /**
     * 中间文字标题颜色
     */
    private int titleColor = Color.WHITE;
    /**
     * 添加右边图标
     */
    private TitleBar.ImageAction[] imageAction;
    /**
     * 添加右边文字
     */
    private TitleBar.TextAction[] textAction;
    /**
     * 下滑分割线颜色
     */
    private int dividerColor = Color.WHITE;
    /**
     * 沉浸式下，自动填充状态栏
     */
    private boolean immersive = false;
    /**
     * 高度/px
     */
    private int height = 100;
    /**
     * 右边文字颜色
     */
    private int actionTextColor = Color.WHITE;

    public int getLeftImageResource() {
        return leftImageResource;
    }

    public void setLeftImageResource(int leftImageResource) {
        this.leftImageResource = leftImageResource;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public int getLeftTextColor() {
        return leftTextColor;
    }

    public void setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isSubVertical() {
        return isSubVertical;
    }

    public void setSubVertical(boolean subVertical) {
        isSubVertical = subVertical;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public TitleBar.ImageAction[] getImageAction() {
        return imageAction;
    }

    public void setImageAction(TitleBar.ImageAction[] imageAction) {
        this.imageAction = imageAction;
    }

    public TitleBar.TextAction[] getTextAction() {
        return textAction;
    }

    public void setTextAction(TitleBar.TextAction[] textAction) {
        this.textAction = textAction;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public boolean isImmersive() {
        return immersive;
    }

    public void setImmersive(boolean immersive) {
        this.immersive = immersive;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getActionTextColor() {
        return actionTextColor;
    }

    public void setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
    }
}
