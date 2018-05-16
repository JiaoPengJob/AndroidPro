package com.tch.kuwanx.result;

/**
 * Created by JiaoP on 2018/1/11.
 * 注册
 */

public class RegisteredResult {

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

        private String account;
        private String accountnum;
        private String authedstatus;
        private String availablenum;
        private String createdate;
        private String id;
        private String nickname;
        private String password;
        private String thirdpartyno;
        private String thirdpartytype;
        private String user_random;
        private String userlevel;
        private String userstatus;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAccountnum() {
            return accountnum;
        }

        public void setAccountnum(String accountnum) {
            this.accountnum = accountnum;
        }

        public String getAuthedstatus() {
            return authedstatus;
        }

        public void setAuthedstatus(String authedstatus) {
            this.authedstatus = authedstatus;
        }

        public String getAvailablenum() {
            return availablenum;
        }

        public void setAvailablenum(String availablenum) {
            this.availablenum = availablenum;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getThirdpartyno() {
            return thirdpartyno;
        }

        public void setThirdpartyno(String thirdpartyno) {
            this.thirdpartyno = thirdpartyno;
        }

        public String getThirdpartytype() {
            return thirdpartytype;
        }

        public void setThirdpartytype(String thirdpartytype) {
            this.thirdpartytype = thirdpartytype;
        }

        public String getUser_random() {
            return user_random;
        }

        public void setUser_random(String user_random) {
            this.user_random = user_random;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
        }
    }
}
