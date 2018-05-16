package com.tch.zx.http.bean.result;

import java.io.Serializable;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * Created by peng on 2017/10/11.
 */

public class GetGroupListResult {

    private List<ResponseObjectBean> responseObject;

    public List<ResponseObjectBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ResponseObjectBean> responseObject) {
        this.responseObject = responseObject;
    }

    public static class ResponseObjectBean implements Serializable {
        /**
         * app_user_id : e725814fc0ae4102a5f2fb81ca3b9963
         * group_nickname : 群聊
         * id : 2
         * member_nickname : 测试账号
         * menberList : [{"isFriend":"","member_app_user_id":"d8f08b34f2b7490481097328bf3a66fb","member_nickname":"Dads","name":"","user_picture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png"},{"isFriend":"","member_app_user_id":"e725814fc0ae4102a5f2fb81ca3b9963","member_nickname":"测试账号","name":"","user_picture":""}]
         */

        private String app_user_id;
        private String group_nickname;
        private String id;
        private String member_nickname;
        private String lastMessage;
        private String lastTime;
        private String groupType;
        private List<MenberListBean> menberList;

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getGroup_nickname() {
            return group_nickname;
        }

        public void setGroup_nickname(String group_nickname) {
            this.group_nickname = group_nickname;
        }

        public String getId() {
            float f = Float.parseFloat(id);
            return String.valueOf(Math.round(f));
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMember_nickname() {
            return member_nickname;
        }

        public void setMember_nickname(String member_nickname) {
            this.member_nickname = member_nickname;
        }

        public List<MenberListBean> getMenberList() {
            return menberList;
        }

        public void setMenberList(List<MenberListBean> menberList) {
            this.menberList = menberList;
        }

        public static class MenberListBean implements Serializable {
            /**
             * isFriend :
             * member_app_user_id : d8f08b34f2b7490481097328bf3a66fb
             * member_nickname : Dads
             * name :
             * user_picture : http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png
             */

            private String isFriend;
            private String member_app_user_id;
            private String member_nickname;
            private String name;
            private String user_picture;

            public MenberListBean() {
            }

            public MenberListBean(String isFriend, String member_app_user_id, String member_nickname, String name, String user_picture) {
                this.isFriend = isFriend;
                this.member_app_user_id = member_app_user_id;
                this.member_nickname = member_nickname;
                this.name = name;
                this.user_picture = user_picture;
            }

            public String getIsFriend() {
                return isFriend;
            }

            public void setIsFriend(String isFriend) {
                this.isFriend = isFriend;
            }

            public String getMember_app_user_id() {
                return member_app_user_id;
            }

            public void setMember_app_user_id(String member_app_user_id) {
                this.member_app_user_id = member_app_user_id;
            }

            public String getMember_nickname() {
                return member_nickname;
            }

            public void setMember_nickname(String member_nickname) {
                this.member_nickname = member_nickname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUser_picture() {
                return user_picture;
            }

            public void setUser_picture(String user_picture) {
                this.user_picture = user_picture;
            }
        }
    }
}
