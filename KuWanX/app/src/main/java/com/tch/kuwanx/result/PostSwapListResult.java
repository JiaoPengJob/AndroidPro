package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/13.
 * 置换物品置换的列表
 */

public class PostSwapListResult {

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

        private String appuser_id;
        private String headpic;
        private String id;
        private String nickname;
        private String post_id;
        private String publish_time;
        private String swap_deposit;
        private String swap_good;
        private String swap_mode;

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
    }
}
