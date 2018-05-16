package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/3/8.
 * 提出置换
 */

public class AddSwapDetailResult {

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

        private String appuser_id;
        private String headpic;
        private String id;
        private String name;
        private String nickname;
        private String post_id;
        private String post_user_id;
        private String publish_time;
        private String swap_deposit;
        private String swap_good;
        private String swap_mode;
        private String swap_status;
        private String detail;
        private String post_headpic;

        public String getPost_headpic() {
            return post_headpic;
        }

        public void setPost_headpic(String post_headpic) {
            this.post_headpic = post_headpic;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getPost_user_id() {
            return post_user_id;
        }

        public void setPost_user_id(String post_user_id) {
            this.post_user_id = post_user_id;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }

        public String getSwap_deposit() {
            return swap_deposit;
        }

        public void setSwap_deposit(String swap_deposit) {
            this.swap_deposit = swap_deposit;
        }

        public String getSwap_good() {
            return swap_good;
        }

        public void setSwap_good(String swap_good) {
            this.swap_good = swap_good;
        }

        public String getSwap_mode() {
            return swap_mode;
        }

        public void setSwap_mode(String swap_mode) {
            this.swap_mode = swap_mode;
        }

        public String getSwap_status() {
            return swap_status;
        }

        public void setSwap_status(String swap_status) {
            this.swap_status = swap_status;
        }
    }
}
