package com.tch.youmuwa.bean.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/9/7.
 */

public class CheckVersionResult implements Serializable {


    /**
     * checkresult : 1
     * recent_version : 1.0.1
     * update_msg : 有可选择的更新，版本号:1.0.1
     * download_url : https://www.baidu.com/
     * features : ["新增充值功能","帮助中心功能优化","其它功能优化"]
     */

    private int checkresult = 0;
    private String recent_version = "";
    private String update_msg = "";
    private String download_url = "";
    private List<String> features = new ArrayList<String>();

    public int getCheckresult() {
        return checkresult;
    }

    public void setCheckresult(int checkresult) {
        this.checkresult = checkresult;
    }

    public String getRecent_version() {
        return recent_version;
    }

    public void setRecent_version(String recent_version) {
        this.recent_version = recent_version;
    }

    public String getUpdate_msg() {
        return update_msg;
    }

    public void setUpdate_msg(String update_msg) {
        this.update_msg = update_msg;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
