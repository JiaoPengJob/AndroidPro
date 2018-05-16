package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/30.
 */

public class MarkPhotoUrl {


    /**
     * big : http://e.hiphotos.bdimg.com/lbsapi/pic/item/b812c8fcc3cec3fd6098b7bcdc88d43f86942750.jpg
     * imgid : 47129664412
     * sml : http://e.hiphotos.bdimg.com/lbsapi/wh%3D16%2C16/sign=a961c33471899e5178db3215748be805/b812c8fcc3cec3fd6098b7bcdc88d43f86942750.jpg
     * mid : http://e.hiphotos.bdimg.com/lbsapi/wh%3D160%2C160/sign=1b4d0286942f07085f502201df1494a8/b812c8fcc3cec3fd6098b7bcdc88d43f86942750.jpg
     */

    private String big = "";
    private String imgid = "";
    private String sml = "";
    private String mid = "";

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getSml() {
        return sml;
    }

    public void setSml(String sml) {
        this.sml = sml;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
