package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/15.
 */

public class OfflineListResultBean {


    /**
     * ret : 1
     * result : {"pageNow":3,"responseObject":[{"appUserName":"测试账号001","offlineAddress":"北京","offlineAppUserId":"d8f08b34f2b7490481097328bf3a66fb","offlineClassIntroduce":"123","offlineEndTime":"2005-12-12 00:00:00.0","offlineId":"4","offlineMoney":"333.00","offlineName":"英语","offlinePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg","offlinePicMin":"","offlineSignupNum":"","offlineStartTime":"2004-12-12 00:00:00.0","position":"市场运营部经理","userPicture":""}]}
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
         * pageNow : 3
         * responseObject : [{"appUserName":"测试账号001","offlineAddress":"北京","offlineAppUserId":"d8f08b34f2b7490481097328bf3a66fb","offlineClassIntroduce":"123","offlineEndTime":"2005-12-12 00:00:00.0","offlineId":"4","offlineMoney":"333.00","offlineName":"英语","offlinePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg","offlinePicMin":"","offlineSignupNum":"","offlineStartTime":"2004-12-12 00:00:00.0","position":"市场运营部经理","userPicture":""}]
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
             * appUserName : 测试账号001
             * offlineAddress : 北京
             * offlineAppUserId : d8f08b34f2b7490481097328bf3a66fb
             * offlineClassIntroduce : 123
             * offlineEndTime : 2005-12-12 00:00:00.0
             * offlineId : 4
             * offlineMoney : 333.00
             * offlineName : 英语
             * offlinePicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg
             * offlinePicMin :
             * offlineSignupNum :
             * offlineStartTime : 2004-12-12 00:00:00.0
             * position : 市场运营部经理
             * userPicture :
             */

            private String appUserName;
            private String offlineAddress;
            private String offlineAppUserId;
            private String offlineClassIntroduce;
            private String offlineEndTime;
            private String offlineId;
            private double offlineMoney;
            private String offlineName;
            private String offlinePicMax;
            private String offlinePicMin;
            private String offlineSignupNum;
            private String offlineStartTime;
            private String position;
            private String userPicture;

            public String getAppUserName() {
                return appUserName;
            }

            public void setAppUserName(String appUserName) {
                this.appUserName = appUserName;
            }

            public String getOfflineAddress() {
                return offlineAddress;
            }

            public void setOfflineAddress(String offlineAddress) {
                this.offlineAddress = offlineAddress;
            }

            public String getOfflineAppUserId() {
                return offlineAppUserId;
            }

            public void setOfflineAppUserId(String offlineAppUserId) {
                this.offlineAppUserId = offlineAppUserId;
            }

            public String getOfflineClassIntroduce() {
                return offlineClassIntroduce;
            }

            public void setOfflineClassIntroduce(String offlineClassIntroduce) {
                this.offlineClassIntroduce = offlineClassIntroduce;
            }

            public String getOfflineEndTime() {
                return offlineEndTime;
            }

            public void setOfflineEndTime(String offlineEndTime) {
                this.offlineEndTime = offlineEndTime;
            }

            public String getOfflineId() {
                return offlineId;
            }

            public void setOfflineId(String offlineId) {
                this.offlineId = offlineId;
            }

            public double getOfflineMoney() {
                return offlineMoney;
            }

            public void setOfflineMoney(double offlineMoney) {
                this.offlineMoney = offlineMoney;
            }

            public String getOfflineName() {
                return offlineName;
            }

            public void setOfflineName(String offlineName) {
                this.offlineName = offlineName;
            }

            public String getOfflinePicMax() {
                return offlinePicMax;
            }

            public void setOfflinePicMax(String offlinePicMax) {
                this.offlinePicMax = offlinePicMax;
            }

            public String getOfflinePicMin() {
                return offlinePicMin;
            }

            public void setOfflinePicMin(String offlinePicMin) {
                this.offlinePicMin = offlinePicMin;
            }

            public String getOfflineSignupNum() {
                return offlineSignupNum;
            }

            public void setOfflineSignupNum(String offlineSignupNum) {
                this.offlineSignupNum = offlineSignupNum;
            }

            public String getOfflineStartTime() {
                return offlineStartTime;
            }

            public void setOfflineStartTime(String offlineStartTime) {
                this.offlineStartTime = offlineStartTime;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getUserPicture() {
                return userPicture;
            }

            public void setUserPicture(String userPicture) {
                this.userPicture = userPicture;
            }
        }
    }
}
