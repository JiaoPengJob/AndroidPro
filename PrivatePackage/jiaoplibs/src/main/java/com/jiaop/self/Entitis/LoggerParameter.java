package com.jiaop.self.Entitis;

/**
 * Created by JiaoP
 * Logger的设置参数
 */

public class LoggerParameter {

    private boolean showThreadInfo = true;

    private int methodCount = 0;

    private int methodOffset = 4;

    private String tag = "LOGGER_TAG：";

    public LoggerParameter() {
    }

    public LoggerParameter(boolean showThreadInfo, int methodCount, int methodOffset, String tag) {
        this.showThreadInfo = showThreadInfo;
        this.methodCount = methodCount;
        this.methodOffset = methodOffset;
        this.tag = tag;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public void setShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public void setMethodCount(int methodCount) {
        this.methodCount = methodCount;
    }

    public int getMethodOffset() {
        return methodOffset;
    }

    public void setMethodOffset(int methodOffset) {
        this.methodOffset = methodOffset;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
