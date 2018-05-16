package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/2/2.
 * 用户交易明细
 */

public class AppuserTransactionResult {

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

        private String transaction_amt;
        private String transaction_mode;
        private String transaction_source;
        private String transaction_time;

        public String getTransaction_amt() {
            return transaction_amt;
        }

        public void setTransaction_amt(String transaction_amt) {
            this.transaction_amt = transaction_amt;
        }

        public String getTransaction_mode() {
            return transaction_mode;
        }

        public void setTransaction_mode(String transaction_mode) {
            this.transaction_mode = transaction_mode;
        }

        public String getTransaction_source() {
            return transaction_source;
        }

        public void setTransaction_source(String transaction_source) {
            this.transaction_source = transaction_source;
        }

        public String getTransaction_time() {
            return transaction_time;
        }

        public void setTransaction_time(String transaction_time) {
            this.transaction_time = transaction_time;
        }
    }
}
