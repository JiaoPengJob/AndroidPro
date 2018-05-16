package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/13.
 * 置换物品的留言列表
 */

public class SwapCdCommentsResult {

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
        private String comm_content;
        private String comm_createtime;
        private String comm_id;
        private String comm_status;
        private String comm_time;
        private String comments_count = "0";
        private String headpic;
        private String nickname;
        private String post_id;
        private String comm_thumbdown = "0";
        private String comm_thumbup = "0";

        public String getAppuser_id() {
            return appuser_id;
        }

        public void setAppuser_id(String appuser_id) {
            this.appuser_id = appuser_id;
        }

        public String getComm_content() {
            return comm_content;
        }

        public void setComm_content(String comm_content) {
            this.comm_content = comm_content;
        }

        public String getComm_createtime() {
            return comm_createtime;
        }

        public void setComm_createtime(String comm_createtime) {
            this.comm_createtime = comm_createtime;
        }

        public String getComm_id() {
            return comm_id;
        }

        public void setComm_id(String comm_id) {
            this.comm_id = comm_id;
        }

        public String getComm_status() {
            return comm_status;
        }

        public void setComm_status(String comm_status) {
            this.comm_status = comm_status;
        }

        public String getComm_time() {
            return comm_time;
        }

        public void setComm_time(String comm_time) {
            this.comm_time = comm_time;
        }

        public String getComments_count() {
            return comments_count;
        }

        public void setComments_count(String comments_count) {
            this.comments_count = comments_count;
        }

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
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

        public String getComm_thumbdown() {
            return comm_thumbdown;
        }

        public void setComm_thumbdown(String comm_thumbdown) {
            this.comm_thumbdown = comm_thumbdown;
        }

        public String getComm_thumbup() {
            return comm_thumbup;
        }

        public void setComm_thumbup(String comm_thumbup) {
            this.comm_thumbup = comm_thumbup;
        }
    }
}
