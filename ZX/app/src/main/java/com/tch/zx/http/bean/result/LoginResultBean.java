package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/14.
 */

public class LoginResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"add_frid_set":"0","adrCity":"本溪市","adrCounty":"平山区","adrProvince":"辽宁省","appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"","appUserName":"Dads","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png","appUserSex":"","categoryName":"餐饮","companyAddress":"明年","companyGif":"","companyId":"1","companyIndustryFType":0,"companyIndustrySType":11,"companyIntroduce":"哈哈摸索特","companyLogo":"http://115.28.95.41:8080/uploadfile/zhixian/201707/cfc09c125d494e37a379f916ba6fedd9.png","companyName":"葡萄牙语","companyPicList":[{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/1a2d22dfeb294314afd8fb20066c692b.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/996d9f5fc58440fdacbd8778cb83cde9.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/37e20dd7754e46db973d403587e21260.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/379662dc595f460eaac9358f00e024e0.png"}],"companyPosition":"市场运营部经理","companyVideo":"","email":"","followIndustry":"","needIndustry":"","offerIndustry":"","only_dk":"1","stypeName":"快餐","userFreeType":"","userType":"3","yunToken":"+b8n2hNPcB3hs8HQZlBAd+J6J0hRMTD4yJdaFIs7sArIfwUYI+EQTId0cJ55MNpReWsCAyFHESgO5U7unRzU4DCS+54PyzQnjNWU73aeq9wANgal0R8Qur/isupObquHHViSxeUvRfg="}}
     */

    private String ret;
    private ResultBean result;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * responseObject : {"add_frid_set":"0","adrCity":"本溪市","adrCounty":"平山区","adrProvince":"辽宁省","appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"","appUserName":"Dads","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png","appUserSex":"","categoryName":"餐饮","companyAddress":"明年","companyGif":"","companyId":"1","companyIndustryFType":0,"companyIndustrySType":11,"companyIntroduce":"哈哈摸索特","companyLogo":"http://115.28.95.41:8080/uploadfile/zhixian/201707/cfc09c125d494e37a379f916ba6fedd9.png","companyName":"葡萄牙语","companyPicList":[{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/1a2d22dfeb294314afd8fb20066c692b.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/996d9f5fc58440fdacbd8778cb83cde9.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/37e20dd7754e46db973d403587e21260.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/379662dc595f460eaac9358f00e024e0.png"}],"companyPosition":"市场运营部经理","companyVideo":"","email":"","followIndustry":"","needIndustry":"","offerIndustry":"","only_dk":"1","stypeName":"快餐","userFreeType":"","userType":"3","yunToken":"+b8n2hNPcB3hs8HQZlBAd+J6J0hRMTD4yJdaFIs7sArIfwUYI+EQTId0cJ55MNpReWsCAyFHESgO5U7unRzU4DCS+54PyzQnjNWU73aeq9wANgal0R8Qur/isupObquHHViSxeUvRfg="}
         */

        private ResponseObjectBean responseObject;

        public ResponseObjectBean getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(ResponseObjectBean responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean {
            /**
             * add_frid_set : 0
             * adrCity : 本溪市
             * adrCounty : 平山区
             * adrProvince : 辽宁省
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserIntroduce :
             * appUserName : Dads
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201707/194a2bdec36047a490bfa2423ba3a25c.png
             * appUserSex :
             * categoryName : 餐饮
             * companyAddress : 明年
             * companyGif :
             * companyId : 1
             * companyIndustryFType : 0
             * companyIndustrySType : 11
             * companyIntroduce : 哈哈摸索特
             * companyLogo : http://115.28.95.41:8080/uploadfile/zhixian/201707/cfc09c125d494e37a379f916ba6fedd9.png
             * companyName : 葡萄牙语
             * companyPicList : [{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/1a2d22dfeb294314afd8fb20066c692b.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/996d9f5fc58440fdacbd8778cb83cde9.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/37e20dd7754e46db973d403587e21260.png"},{"companyPicture":"http://115.28.95.41:8080/uploadfile/zhixian/201707/379662dc595f460eaac9358f00e024e0.png"}]
             * companyPosition : 市场运营部经理
             * companyVideo :
             * email :
             * followIndustry :
             * needIndustry :
             * offerIndustry :
             * only_dk : 1
             * stypeName : 快餐
             * userFreeType :
             * userType : 3
             * yunToken : +b8n2hNPcB3hs8HQZlBAd+J6J0hRMTD4yJdaFIs7sArIfwUYI+EQTId0cJ55MNpReWsCAyFHESgO5U7unRzU4DCS+54PyzQnjNWU73aeq9wANgal0R8Qur/isupObquHHViSxeUvRfg=
             */

            private String add_frid_set;
            private String adrCity;
            private String adrCounty;
            private String adrProvince;
            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private String appUserSex;
            private String categoryName;
            private String companyAddress;
            private String companyGif;
            private String companyId;
            private int companyIndustryFType;
            private int companyIndustrySType;
            private String companyIntroduce;
            private String companyLogo;
            private String companyName;
            private String companyPosition;
            private String companyVideo;
            private String email;
            private String followIndustry;
            private String needIndustry;
            private String offerIndustry;
            private String only_dk;
            private String stypeName;
            private String userFreeType;
            private String userType;
            private String yunToken;
            private List<CompanyPicListBean> companyPicList;

            public String getAdd_frid_set() {
                return add_frid_set;
            }

            public void setAdd_frid_set(String add_frid_set) {
                this.add_frid_set = add_frid_set;
            }

            public String getAdrCity() {
                return adrCity;
            }

            public void setAdrCity(String adrCity) {
                this.adrCity = adrCity;
            }

            public String getAdrCounty() {
                return adrCounty;
            }

            public void setAdrCounty(String adrCounty) {
                this.adrCounty = adrCounty;
            }

            public String getAdrProvince() {
                return adrProvince;
            }

            public void setAdrProvince(String adrProvince) {
                this.adrProvince = adrProvince;
            }

            public String getAppUserId() {
                return appUserId;
            }

            public void setAppUserId(String appUserId) {
                this.appUserId = appUserId;
            }

            public String getAppUserIntroduce() {
                return appUserIntroduce;
            }

            public void setAppUserIntroduce(String appUserIntroduce) {
                this.appUserIntroduce = appUserIntroduce;
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

            public String getAppUserSex() {
                return appUserSex;
            }

            public void setAppUserSex(String appUserSex) {
                this.appUserSex = appUserSex;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCompanyAddress() {
                return companyAddress;
            }

            public void setCompanyAddress(String companyAddress) {
                this.companyAddress = companyAddress;
            }

            public String getCompanyGif() {
                return companyGif;
            }

            public void setCompanyGif(String companyGif) {
                this.companyGif = companyGif;
            }

            public String getCompanyId() {
                return companyId;
            }

            public void setCompanyId(String companyId) {
                this.companyId = companyId;
            }

            public int getCompanyIndustryFType() {
                return companyIndustryFType;
            }

            public void setCompanyIndustryFType(int companyIndustryFType) {
                this.companyIndustryFType = companyIndustryFType;
            }

            public int getCompanyIndustrySType() {
                return companyIndustrySType;
            }

            public void setCompanyIndustrySType(int companyIndustrySType) {
                this.companyIndustrySType = companyIndustrySType;
            }

            public String getCompanyIntroduce() {
                return companyIntroduce;
            }

            public void setCompanyIntroduce(String companyIntroduce) {
                this.companyIntroduce = companyIntroduce;
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

            public String getCompanyVideo() {
                return companyVideo;
            }

            public void setCompanyVideo(String companyVideo) {
                this.companyVideo = companyVideo;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFollowIndustry() {
                return followIndustry;
            }

            public void setFollowIndustry(String followIndustry) {
                this.followIndustry = followIndustry;
            }

            public String getNeedIndustry() {
                return needIndustry;
            }

            public void setNeedIndustry(String needIndustry) {
                this.needIndustry = needIndustry;
            }

            public String getOfferIndustry() {
                return offerIndustry;
            }

            public void setOfferIndustry(String offerIndustry) {
                this.offerIndustry = offerIndustry;
            }

            public String getOnly_dk() {
                return only_dk;
            }

            public void setOnly_dk(String only_dk) {
                this.only_dk = only_dk;
            }

            public String getStypeName() {
                return stypeName;
            }

            public void setStypeName(String stypeName) {
                this.stypeName = stypeName;
            }

            public String getUserFreeType() {
                return userFreeType;
            }

            public void setUserFreeType(String userFreeType) {
                this.userFreeType = userFreeType;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getYunToken() {
                return yunToken;
            }

            public void setYunToken(String yunToken) {
                this.yunToken = yunToken;
            }

            public List<CompanyPicListBean> getCompanyPicList() {
                return companyPicList;
            }

            public void setCompanyPicList(List<CompanyPicListBean> companyPicList) {
                this.companyPicList = companyPicList;
            }

            public static class CompanyPicListBean {
                /**
                 * companyPicture : http://115.28.95.41:8080/uploadfile/zhixian/201707/1a2d22dfeb294314afd8fb20066c692b.png
                 */

                private String companyPicture;

                public String getCompanyPicture() {
                    return companyPicture;
                }

                public void setCompanyPicture(String companyPicture) {
                    this.companyPicture = companyPicture;
                }
            }
        }
    }
}
