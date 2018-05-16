package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/23.
 * 置换记录
 */

public class SwapListResult {

    private String msg;
    private String ret;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {

        private String swap_good;
        private String swap_time;

        public String getSwap_good() {
            return swap_good;
        }

        public void setSwap_good(String swap_good) {
            this.swap_good = swap_good;
        }

        public String getSwap_time() {
            return swap_time;
        }

        public void setSwap_time(String swap_time) {
            this.swap_time = swap_time;
        }
    }
}
