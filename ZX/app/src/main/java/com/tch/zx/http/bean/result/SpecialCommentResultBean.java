package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/20.
 */

public class SpecialCommentResultBean {


    /**
     * ret : 1
     * result : {"pageNow":2,"responseObject":[{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"45as46a4sd","commCreateTime":"2017-07-19 16:51:59.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":4},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"assdasd112313","commCreateTime":"2017-07-19 16:51:12.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":3},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"12fwfdf","commCreateTime":"2017-07-19 16:51:05.0","companyName":"天成合科技","fabulousNum":1,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":2},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"asdasdad","commCreateTime":"2017-07-19 16:50:49.0","companyName":"天成合科技","fabulousNum":1,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":1}]}
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
         * responseObject : [{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"45as46a4sd","commCreateTime":"2017-07-19 16:51:59.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":4},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"assdasd112313","commCreateTime":"2017-07-19 16:51:12.0","companyName":"天成合科技","fabulousNum":0,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":3},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"12fwfdf","commCreateTime":"2017-07-19 16:51:05.0","companyName":"天成合科技","fabulousNum":1,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":2},{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","commContent":"asdasdad","commCreateTime":"2017-07-19 16:50:49.0","companyName":"天成合科技","fabulousNum":1,"position":"市场运营部经理","specialCommentClassId":4,"specialCommentId":1}]
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
             * commContent : 45as46a4sd
             * commCreateTime : 2017-07-19 16:51:59.0
             * companyName : 天成合科技
             * fabulousNum : 0
             * position : 市场运营部经理
             * specialCommentClassId : 4
             * specialCommentId : 4
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private String commContent;
            private String commCreateTime;
            private String companyName;
            private int fabulousNum;
            private String position;
            private int specialCommentClassId;
            private int specialCommentId;

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

            public int getSpecialCommentClassId() {
                return specialCommentClassId;
            }

            public void setSpecialCommentClassId(int specialCommentClassId) {
                this.specialCommentClassId = specialCommentClassId;
            }

            public int getSpecialCommentId() {
                return specialCommentId;
            }

            public void setSpecialCommentId(int specialCommentId) {
                this.specialCommentId = specialCommentId;
            }
        }
    }
}
