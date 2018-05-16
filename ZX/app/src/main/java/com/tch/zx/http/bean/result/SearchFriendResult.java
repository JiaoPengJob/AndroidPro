package com.tch.zx.http.bean.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by peng on 2017/9/28.
 */

public class SearchFriendResult {

    private List<ResponseObjectBean> responseObject;

    public List<ResponseObjectBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ResponseObjectBean> responseObject) {
        this.responseObject = responseObject;
    }

    public static class ResponseObjectBean implements Serializable{
        /**
         * adr_city :
         * adr_county :
         * adr_province :
         * app_user_id : d8f08b34f2b7490481097328bf3a66fb
         * app_user_phone :
         * apply_status :
         * appyId :
         * company_introduce :
         * company_logo :
         * company_name : 葡萄牙语
         * company_position : 市场运营部经理
         * content_picture :
         * dynamic_content :
         * is_column :
         * is_friend : false
         * name : Dads
         * need_industry :
         * offer_industry :
         * sex :
         * user_introduce :
         * user_picture : http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png
         * verification_message :
         */

        private String adr_city;
        private String adr_county;
        private String adr_province;
        private String app_user_id;
        private String app_user_phone;
        private String apply_status;
        private String appyId;
        private String company_introduce;
        private String company_logo;
        private String company_name;
        private String company_position;
        private String content_picture;
        private String dynamic_content;
        private String is_column;
        private boolean is_friend;
        private String name;
        private String need_industry;
        private String offer_industry;
        private String sex;
        private String user_introduce;
        private String user_picture;
        private String verification_message;

        public String getAdr_city() {
            return adr_city;
        }

        public void setAdr_city(String adr_city) {
            this.adr_city = adr_city;
        }

        public String getAdr_county() {
            return adr_county;
        }

        public void setAdr_county(String adr_county) {
            this.adr_county = adr_county;
        }

        public String getAdr_province() {
            return adr_province;
        }

        public void setAdr_province(String adr_province) {
            this.adr_province = adr_province;
        }

        public String getApp_user_id() {
            return app_user_id;
        }

        public void setApp_user_id(String app_user_id) {
            this.app_user_id = app_user_id;
        }

        public String getApp_user_phone() {
            return app_user_phone;
        }

        public void setApp_user_phone(String app_user_phone) {
            this.app_user_phone = app_user_phone;
        }

        public String getApply_status() {
            return apply_status;
        }

        public void setApply_status(String apply_status) {
            this.apply_status = apply_status;
        }

        public String getAppyId() {
            return appyId;
        }

        public void setAppyId(String appyId) {
            this.appyId = appyId;
        }

        public String getCompany_introduce() {
            return company_introduce;
        }

        public void setCompany_introduce(String company_introduce) {
            this.company_introduce = company_introduce;
        }

        public String getCompany_logo() {
            return company_logo;
        }

        public void setCompany_logo(String company_logo) {
            this.company_logo = company_logo;
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

        public String getDynamic_content() {
            return dynamic_content;
        }

        public void setDynamic_content(String dynamic_content) {
            this.dynamic_content = dynamic_content;
        }

        public String getIs_column() {
            return is_column;
        }

        public void setIs_column(String is_column) {
            this.is_column = is_column;
        }

        public boolean isIs_friend() {
            return is_friend;
        }

        public void setIs_friend(boolean is_friend) {
            this.is_friend = is_friend;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNeed_industry() {
            return need_industry;
        }

        public void setNeed_industry(String need_industry) {
            this.need_industry = need_industry;
        }

        public String getOffer_industry() {
            return offer_industry;
        }

        public void setOffer_industry(String offer_industry) {
            this.offer_industry = offer_industry;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_introduce() {
            return user_introduce;
        }

        public void setUser_introduce(String user_introduce) {
            this.user_introduce = user_introduce;
        }

        public String getUser_picture() {
            return user_picture;
        }

        public void setUser_picture(String user_picture) {
            this.user_picture = user_picture;
        }

        public String getVerification_message() {
            return verification_message;
        }

        public void setVerification_message(String verification_message) {
            this.verification_message = verification_message;
        }
    }
}
