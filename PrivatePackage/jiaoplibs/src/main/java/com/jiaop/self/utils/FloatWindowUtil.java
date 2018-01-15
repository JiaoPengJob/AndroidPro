package com.jiaop.self.utils;

import com.jiaop.self.entitis.FloatWindowParameter;
import com.yhao.floatwindow.FloatWindow;

/**
 * Created by jiaop
 * 任意界面悬浮窗帮助类
 */

public class FloatWindowUtil {

    /**
     * 显示悬浮窗
     */
    public static void showFloatWindow(FloatWindowParameter floatWindowParameter) {
        if (floatWindowParameter.getViewId() == 0) {
            FloatWindow
                    .with(floatWindowParameter.getContent())
                    .setView(floatWindowParameter.getView())
                    .setWidth(floatWindowParameter.getWidth(), floatWindowParameter.getWidthRatio())
                    .setHeight(floatWindowParameter.getHeight(), floatWindowParameter.getHeightRatio())
                    .setX(floatWindowParameter.getX(), floatWindowParameter.getxRatio())
                    .setY(floatWindowParameter.getY(), floatWindowParameter.getyRatio())
                    .setFilter(floatWindowParameter.isShow(), floatWindowParameter.getActivities())
                    .setDesktopShow(floatWindowParameter.isShowDesktop())
                    .setMoveType(floatWindowParameter.getMoveType())
                    .setMoveStyle(floatWindowParameter.getDuration(), floatWindowParameter.getInterpolator())
                    .build();
        } else {
            FloatWindow
                    .with(floatWindowParameter.getContent())
                    .setView(floatWindowParameter.getViewId())
                    .setWidth(floatWindowParameter.getWidth(), floatWindowParameter.getWidthRatio())
                    .setHeight(floatWindowParameter.getHeight(), floatWindowParameter.getHeightRatio())
                    .setX(floatWindowParameter.getX(), floatWindowParameter.getxRatio())
                    .setY(floatWindowParameter.getY(), floatWindowParameter.getyRatio())
                    .setFilter(floatWindowParameter.isShow(), floatWindowParameter.getActivities())
                    .setDesktopShow(floatWindowParameter.isShowDesktop())
                    .setMoveType(floatWindowParameter.getMoveType())
                    .setMoveStyle(floatWindowParameter.getDuration(), floatWindowParameter.getInterpolator())
                    .build();
        }
    }

    /**
     * 手动指定页面展示
     *
     * @param tag
     */
    private static void manualFloatWindowShow(String tag) {
        FloatWindow.get(tag).show();
    }

    /**
     * 手动指定页面隐藏
     *
     * @param tag
     */
    private static void manualFloatWindowHide(String tag) {
        FloatWindow.get(tag).hide();
    }

    /**
     * 手动销毁指定页面
     * tag=""时，销毁全部
     *
     * @param tag
     */
    public static void destroyFloatWindow(String tag) {
        if (tag.equals("")) {
            FloatWindow.destroy();
        } else {
            FloatWindow.destroy(tag);
        }

    }


}
