package com.tch.kuwanx.result;

/**
 * Created by jiaop on 2018/2/1.
 * 添加好友备注
 */

public class AddFriendNoteResult {

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

        private String app_user_id;
        private String friend_note;
        private String friends_app_user_id;

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getFriend_note() {
            return friend_note;
        }

        public void setFriend_note(String friend_note) {
            this.friend_note = friend_note;
        }

        public String getFriends_app_user_id() {
            return friends_app_user_id;
        }

        public void setFriends_app_user_id(String friends_app_user_id) {
            this.friends_app_user_id = friends_app_user_id;
        }
    }
}
