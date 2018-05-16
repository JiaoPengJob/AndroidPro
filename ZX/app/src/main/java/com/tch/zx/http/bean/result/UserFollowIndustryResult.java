package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/10/17.
 */

public class UserFollowIndustryResult {

    private List<ResponseObjectBean> responseObject;

    public List<ResponseObjectBean> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ResponseObjectBean> responseObject) {
        this.responseObject = responseObject;
    }

    public static class ResponseObjectBean {
        /**
         * appUserId : e725814fc0ae4102a5f2fb81ca3b9963
         * appUserName : 测试账号
         * appUserPic :
         * companyLogo :
         * companyName :
         * companyPosition :
         * stypeName : 基金
         */

        private String appUserId;
        private String appUserName;
        private String appUserPic;
        private String companyLogo;
        private String companyName;
        private String companyPosition;
        private String stypeName;

        public String getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }

        public String getAppUserName() {
            return appUserName;
        }

        public void setAppUserName(String appUserName) {
            this.appUserName = appUserName;
        }

        public String getAppUserPic() {
            return appUserPic;
        }

        public void setAppUserPic(String appUserPic) {
            this.appUserPic = appUserPic;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyPosition() {
            return companyPosition;
        }

        public void setCompanyPosition(String companyPosition) {
            this.companyPosition = companyPosition;
        }

        public String getStypeName() {
            return stypeName;
        }

        public void setStypeName(String stypeName) {
            this.stypeName = stypeName;
        }
    }
}
