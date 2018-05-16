package com.tch.kuwanx.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by peng on 2017/8/29.
 */

public class WeChatPayResult {


    /**
     * partnerid : 1486490072
     * prepayid : wx201708281808034f50a342a40345894351
     * timestamp : 1503914546
     * appid : wxef807a23ff724c9d
     * noncestr : test
     * package : Sign=WXPay
     * sign : 936B93B53CBD90781EBC52A24569A2F6
     */

    private String partnerid = "";
    private String prepayid = "";
    private int timestamp;
    private String appid = "";
    private String noncestr = "";
    @SerializedName("package")
    private String packageX = "";
    private String sign = "";

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
