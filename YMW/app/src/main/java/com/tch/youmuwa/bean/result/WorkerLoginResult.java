package com.tch.youmuwa.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/8/11.
 */

public class WorkerLoginResult implements Serializable{

    /**
     * result : 0
     */

    private int result;
    private String mobile = "";

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
