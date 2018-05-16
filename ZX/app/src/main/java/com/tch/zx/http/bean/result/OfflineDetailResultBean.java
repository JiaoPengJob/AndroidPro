package com.tch.zx.http.bean.result;

/**
 * Created by peng on 2017/7/20.
 */

public class OfflineDetailResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"appUserId":"","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPhone":"17319311613","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","isCollect":0,"isConcern":0,"offlineAddress":"北京","offlineClassIntroduce":"123","offlineEndTime":"2005-12-12 00:00:00.0","offlineId":"4","offlineMoney":333,"offlineName":"英语","offlinePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg","offlinePicMin":"","offlinePlaces":"11","offlineSignupNum":"","offlineStartTime":"2004-12-12 00:00:00.0","position":"市场运营部经理"}}
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
         * responseObject : {"appUserId":"","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPhone":"17319311613","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","isCollect":0,"isConcern":0,"offlineAddress":"北京","offlineClassIntroduce":"123","offlineEndTime":"2005-12-12 00:00:00.0","offlineId":"4","offlineMoney":333,"offlineName":"英语","offlinePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg","offlinePicMin":"","offlinePlaces":"11","offlineSignupNum":"","offlineStartTime":"2004-12-12 00:00:00.0","position":"市场运营部经理"}
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
             * appUserId :
             * appUserIntroduce : 测试
             * appUserName : 测试账号001
             * appUserPhone : 17319311613
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * isCollect : 0
             * isConcern : 0
             * offlineAddress : 北京
             * offlineClassIntroduce : 123
             * offlineEndTime : 2005-12-12 00:00:00.0
             * offlineId : 4
             * offlineMoney : 333
             * offlineName : 英语
             * offlinePicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/464d95ecc9c94b09837ef1f283d3d7d2.jpg
             * offlinePicMin :
             * offlinePlaces : 11
             * offlineSignupNum :
             * offlineStartTime : 2004-12-12 00:00:00.0
             * position : 市场运营部经理
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPhone;
            private String appUserPic;
            private int isCollect;
            private int isConcern;
            private String offlineAddress;
            private String offlineClassIntroduce;
            private String offlineEndTime;
            private String offlineId;
            private int offlineMoney;
            private String offlineName;
            private String offlinePicMax;
            private String offlinePicMin;
            private String offlinePlaces;
            private String offlineSignupNum;
            private String offlineStartTime;
            private String position;

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

            public String getAppUserPhone() {
                return appUserPhone;
            }

            public void setAppUserPhone(String appUserPhone) {
                this.appUserPhone = appUserPhone;
            }

            public String getAppUserPic() {
                return appUserPic;
            }

            public void setAppUserPic(String appUserPic) {
                this.appUserPic = appUserPic;
            }

            public int getIsCollect() {
                return isCollect;
            }

            public void setIsCollect(int isCollect) {
                this.isCollect = isCollect;
            }

            public int getIsConcern() {
                return isConcern;
            }

            public void setIsConcern(int isConcern) {
                this.isConcern = isConcern;
            }

            public String getOfflineAddress() {
                return offlineAddress;
            }

            public void setOfflineAddress(String offlineAddress) {
                this.offlineAddress = offlineAddress;
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

            public int getOfflineMoney() {
                return offlineMoney;
            }

            public void setOfflineMoney(int offlineMoney) {
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

            public String getOfflinePlaces() {
                return offlinePlaces;
            }

            public void setOfflinePlaces(String offlinePlaces) {
                this.offlinePlaces = offlinePlaces;
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
        }
    }
}
