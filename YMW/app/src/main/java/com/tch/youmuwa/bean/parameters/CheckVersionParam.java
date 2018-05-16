package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/9/7.
 */

public class CheckVersionParam {

    private String platform = "android";
    private String client_version;

    public CheckVersionParam() {
    }

    public CheckVersionParam(String client_version) {
        this.client_version = client_version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getClient_version() {
        return client_version;
    }

    public void setClient_version(String client_version) {
        this.client_version = client_version;
    }
}
