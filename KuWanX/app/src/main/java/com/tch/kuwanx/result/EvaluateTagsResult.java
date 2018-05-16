package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/30.
 * 评价标签
 */

public class EvaluateTagsResult {

    private String msg;
    private String ret;
    private List<String> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
