package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/18.
 */

public class SmallCommentResultBean {


    /**
     * ret : 1
     * result : {"pageNow":2,"responseObject":[{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 17:31:41.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":8},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddddddd","commCreateTime":"2017-07-18 16:45:54.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":6},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 16:45:31.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":5},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 16:45:27.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":4}]}
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
         * pageNow : 2
         * responseObject : [{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 17:31:41.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":8},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddddddd","commCreateTime":"2017-07-18 16:45:54.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":6},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 16:45:31.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":5},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"Ddd","commCreateTime":"2017-07-18 16:45:27.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","smallClassCommId":4}]
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
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * commContent : Ddd
             * commCreateTime : 2017-07-18 17:31:41.0
             * companyName : 天成合科技
             * fabulousNum : 0
             * position : 市场运营部经理
             * smallClassCommId : 8
             */

            private String appUserId;
            private String appUserName;
            private String appUserPic;
            private String commContent;
            private String commCreateTime;
            private String companyName;
            private int fabulousNum;
            private String position;
            private int smallClassCommId;

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

            public String getCommContent() {
                return commContent;
            }

            public void setCommContent(String commContent) {
                this.commContent = commContent;
            }

            public String getCommCreateTime() {
                return commCreateTime;
            }

            public void setCommCreateTime(String commCreateTime) {
                this.commCreateTime = commCreateTime;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public int getFabulousNum() {
                return fabulousNum;
            }

            public void setFabulousNum(int fabulousNum) {
                this.fabulousNum = fabulousNum;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public int getSmallClassCommId() {
                return smallClassCommId;
            }

            public void setSmallClassCommId(int smallClassCommId) {
                this.smallClassCommId = smallClassCommId;
            }
        }
    }
}
