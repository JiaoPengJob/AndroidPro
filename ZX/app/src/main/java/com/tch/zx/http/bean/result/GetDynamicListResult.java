package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/10/18.
 */

public class GetDynamicListResult {

    /**
     * pageNow : 11
     * responseObject : [{"app_user_id":"e725814fc0ae4102a5f2fb81ca3b9963","company_name":"","company_position":"","content_picture":"","count":"","dynamic_content":"Jjjjjjjjjjjjjjjjjjjjjjjjjj","dynamic_create_date":"2017-10-16 22:12:51","dynamic_id":"10","fabulous_num":"","name":"测试账号","su_ccomment_content":"","su_ccomment_create_time":"","su_ccomment_id":"","user_picture":""}]
     */

    private int pageNow;
    private List<ResponseObjectBean> responseObject;

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public List<ResponseObjectBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ResponseObjectBean> responseObject) {
        this.responseObject = responseObject;
    }

    public static class ResponseObjectBean {
        /**
         * app_user_id : e725814fc0ae4102a5f2fb81ca3b9963
         * company_name :
         * company_position :
         * content_picture :
         * count :
         * dynamic_content : Jjjjjjjjjjjjjjjjjjjjjjjjjj
         * dynamic_create_date : 2017-10-16 22:12:51
         * dynamic_id : 10
         * fabulous_num :
         * name : 测试账号
         * su_ccomment_content :
         * su_ccomment_create_time :
         * su_ccomment_id :
         * user_picture :
         */

        private String app_user_id;
        private String company_name;
        private String company_position;
        private String content_picture;
        private String count = "0";
        private String dynamic_content;
        private String dynamic_create_date;
        private String dynamic_id;
        private String fabulous_num = "0";
        private String name;
        private String su_ccomment_content;
        private String su_ccomment_create_time;
        private String su_ccomment_id;
        private String user_picture;

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_position() {
            return company_position;
        }

        public void setCompany_position(String company_position) {
            this.company_position = company_position;
        }

        public String getContent_picture() {
            return content_picture;
        }

        public void setContent_picture(String content_picture) {
            this.content_picture = content_picture;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDynamic_content() {
            return dynamic_content;
        }

        public void setDynamic_content(String dynamic_content) {
            this.dynamic_content = dynamic_content;
        }

        public String getDynamic_create_date() {
            return dynamic_create_date;
        }

        public void setDynamic_create_date(String dynamic_create_date) {
            this.dynamic_create_date = dynamic_create_date;
        }

        public String getDynamic_id() {
            return dynamic_id;
        }

        public void setDynamic_id(String dynamic_id) {
            this.dynamic_id = dynamic_id;
        }

        public String getFabulous_num() {
            return fabulous_num;
        }

        public void setFabulous_num(String fabulous_num) {
            this.fabulous_num = fabulous_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSu_ccomment_content() {
            return su_ccomment_content;
        }

        public void setSu_ccomment_content(String su_ccomment_content) {
            this.su_ccomment_content = su_ccomment_content;
        }

        public String getSu_ccomment_create_time() {
            return su_ccomment_create_time;
        }

        public void setSu_ccomment_create_time(String su_ccomment_create_time) {
            this.su_ccomment_create_time = su_ccomment_create_time;
        }

        public String getSu_ccomment_id() {
            return su_ccomment_id;
        }

        public void setSu_ccomment_id(String su_ccomment_id) {
            this.su_ccomment_id = su_ccomment_id;
        }

        public String getUser_picture() {
            return user_picture;
        }

        public void setUser_picture(String user_picture) {
            this.user_picture = user_picture;
        }
    }
}
