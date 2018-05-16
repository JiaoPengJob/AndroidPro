package com.tch.kuwanx.result;

import java.util.List;

/**
 * Created by jiaop on 2018/1/27.
 * 好友列表
 */

public class FriendsListResult {

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

        private String friends_app_user_id;
        private String headpic;
        private String id;
        private String nickname;

        public String getFriends_app_user_id() {
            return friends_app_user_id;
        }

        public void setFriends_app_user_id(String friends_app_user_id) {
            this.friends_app_user_id = friends_app_user_id;
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
    }
}
