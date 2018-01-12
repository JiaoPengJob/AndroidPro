package com.jiaop.self.utils;

import com.jiaop.self.entitis.TitleBarParameter;
import com.jiaop.self.views.TitleBar;

/**
 * Created by JiaoP
 * 标题栏设置帮助类
 */

public class TitleBarUtil {

    /**
     * 加载标题栏样式
     *
     * @param titleBar
     * @param parameter
     */
    public static void initTitle(TitleBar titleBar, TitleBarParameter parameter) {
        titleBar.setLeftImageResource(parameter.getLeftImageResource());
        if (parameter.getLeftText() != null
                && !parameter.getLeftText().equals("")) {
            titleBar.setLeftText(parameter.getLeftText());
            titleBar.setLeftTextColor(parameter.getLeftTextColor());
        }

        if (parameter.getImageAction() != null
                && parameter.getImageAction().length > 0) {
            for (int i = 0; i < parameter.getImageAction().length; i++) {
                titleBar.addAction(parameter.getImageAction()[i]);
            }
        }

        if (parameter.getTextAction() != null
                && parameter.getTextAction().length > 0) {
            for (int i = 0; i < parameter.getTextAction().length; i++) {
                titleBar.addAction(parameter.getTextAction()[i]);
            }
        }

        titleBar.setActionTextColor(parameter.getActionTextColor());
        titleBar.setHeight(parameter.getHeight());
        titleBar.setDividerColor(parameter.getDividerColor());
        titleBar.setImmersive(parameter.isImmersive());
        titleBar.setTitle(parameter.getTitle());
        if (parameter.getSubTitle() != null
                && !parameter.getSubTitle().equals("")) {
            if (parameter.isSubVertical()) {
                titleBar.setTitle(parameter.getTitle() + "\n" + parameter.getSubTitle());
            } else {
                titleBar.setTitle(parameter.getTitle() + "\t" + parameter.getSubTitle());
            }
        }
        titleBar.setTitleColor(parameter.getTitleColor());
    }

}
