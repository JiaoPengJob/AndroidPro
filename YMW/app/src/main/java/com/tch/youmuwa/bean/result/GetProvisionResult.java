package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/8.
 */

public class GetProvisionResult {

    private String content = "";

    public GetProvisionResult() {
    }

    public GetProvisionResult(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
