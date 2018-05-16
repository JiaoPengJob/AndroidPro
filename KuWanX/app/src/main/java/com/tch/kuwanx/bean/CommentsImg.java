package com.tch.kuwanx.bean;

/**
 * Created by jiaop on 2018/3/5.
 * 评价的图片显示
 */

public class CommentsImg {

    private boolean isSkip;

    private String path;

    public CommentsImg() {
    }

    public CommentsImg(boolean isSkip, String path) {
        this.isSkip = isSkip;
        this.path = path;
    }

    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        isSkip = skip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
