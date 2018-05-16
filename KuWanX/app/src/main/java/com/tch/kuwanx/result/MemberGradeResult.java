package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/23.
 * 我的钱包余额
 */

public class MemberGradeResult {

    private String msg;
    private ResultBean result;
    private String ret;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class ResultBean {

        private String accountnum;
        private String availablenum;
        private String swapCount;
        private String userlevel;

        public String getAccountnum() {
            return accountnum;
        }

        public void setAccountnum(String accountnum) {
            this.accountnum = accountnum;
        }

        public String getAvailablenum() {
            return availablenum;
        }

        public void setAvailablenum(String availablenum) {
            this.availablenum = availablenum;
        }

        public String getSwapCount() {
            return swapCount;
        }

        public void setSwapCount(String swapCount) {
            this.swapCount = swapCount;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }
    }
}
