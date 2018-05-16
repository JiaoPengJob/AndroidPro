package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/21.
 */

public class OfflineCommentResultBean {


    /**
     * ret : 1
     * result : {"pageNow":2,"responseObject":[{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:20:11.0","fabulousNum":0,"offlineCommentId":3,"offlineId":4,"position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:19:57.0","fabulousNum":0,"offlineCommentId":2,"offlineId":4,"position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:19:38.0","fabulousNum":0,"offlineCommentId":1,"offlineId":4,"position":"市场运营部经理"}]}
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
         * responseObject : [{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:20:11.0","fabulousNum":0,"offlineCommentId":3,"offlineId":4,"position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:19:57.0","fabulousNum":0,"offlineCommentId":2,"offlineId":4,"position":"市场运营部经理"},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"测试内容","commCreateTime":"2017-07-20 10:19:38.0","fabulousNum":0,"offlineCommentId":1,"offlineId":4,"position":"市场运营部经理"}]
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
             * appUserIntroduce : 测试
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * commContent : 测试内容
             * commCreateTime : 2017-07-20 10:20:11.0
             * fabulousNum : 0
             * offlineCommentId : 3
             * offlineId : 4
             * position : 市场运营部经理
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private String commContent;
            private String commCreateTime;
            private int fabulousNum;
            private int offlineCommentId;
            private int offlineId;
            private String position;
            private String companyName;

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

            public int getFabulousNum() {
                return fabulousNum;
            }

            public void setFabulousNum(int fabulousNum) {
                this.fabulousNum = fabulousNum;
            }

            public int getOfflineCommentId() {
                return offlineCommentId;
            }

            public void setOfflineCommentId(int offlineCommentId) {
                this.offlineCommentId = offlineCommentId;
            }

            public int getOfflineId() {
                return offlineId;
            }

            public void setOfflineId(int offlineId) {
                this.offlineId = offlineId;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }
        }
    }
}
