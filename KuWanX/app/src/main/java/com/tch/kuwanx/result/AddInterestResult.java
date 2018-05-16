package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/1/29.
 * 添加关注
 */

public class AddInterestResult {

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

        private String interest_user_id;
        private String user_id;
        private String id;
        private String create_time;

        public String getInterest_user_id() {
            return interest_user_id;
        }

        public void setInterest_user_id(String interest_user_id) {
            this.interest_user_id = interest_user_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
