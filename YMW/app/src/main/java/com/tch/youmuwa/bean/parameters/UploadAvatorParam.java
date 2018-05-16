package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class UploadAvatorParam {

    private String imgpath;

    public UploadAvatorParam() {
    }

    public UploadAvatorParam(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
