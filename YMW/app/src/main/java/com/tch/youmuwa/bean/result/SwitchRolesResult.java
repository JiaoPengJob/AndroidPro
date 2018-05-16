package com.tch.youmuwa.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/8/14.
 */

public class SwitchRolesResult implements Serializable{


    /**
         * result : 1
         * userInfo : {"type":"1","id":18,"user_id":18,"mobile":"13520518525","name":null,"sex":0,"avator":"http://a.test.ychlink.com/storage/users/default.png","state":1}
         */

        private int result;
        private UserInfoBean userInfo;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * type : 1
             * id : 18
             * user_id : 18
             * mobile : 13520518525
             * name : null
             * sex : 0
             * avator : http://a.test.ychlink.com/storage/users/default.png
             * state : 1
             */

            private String type = "";
            private int id;
            private int user_id;
            private String mobile = "";
            private String name = "";
            private int sex;
            private String avator = "";
            private int state;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getAvator() {
                return avator;
            }

            public void setAvator(String avator) {
                this.avator = avator;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
    }
}
