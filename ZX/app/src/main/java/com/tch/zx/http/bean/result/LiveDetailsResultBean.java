package com.tch.zx.http.bean.result;

import java.io.Serializable;

/**
 * 直播详情
 */

public class LiveDetailsResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","confirmPay":1,"isConcern":1,"liveId":2,"liveIntroduce":"123","liveMoney":444.5,"liveName":"测试1","livePepoleNum":"1452","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveVideo":"","liveViewNum":"1000","position":"市场运营部经理"}}
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
         * responseObject : {"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","confirmPay":1,"isConcern":1,"liveId":2,"liveIntroduce":"123","liveMoney":444.5,"liveName":"测试1","livePepoleNum":"1452","livePicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg","livePicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg","liveStatus":"2","liveTime":"2017-06-07 00:00:00.0","liveVideo":"","liveViewNum":"1000","position":"市场运营部经理"}
         */

        private ResponseObjectBean responseObject;

        public ResponseObjectBean getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(ResponseObjectBean responseObject) {
            this.responseObject = responseObject;
        }

        public static class ResponseObjectBean implements Serializable{
            /**
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserIntroduce : 测试
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * confirmPay : 1
             * isConcern : 1
             * liveId : 2
             * liveIntroduce : 123
             * liveMoney : 444.5
             * liveName : 测试1
             * livePepoleNum : 1452
             * livePicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg
             * livePicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg
             * liveStatus : 2
             * liveTime : 2017-06-07 00:00:00.0
             * liveVideo :
             * liveViewNum : 1000
             * position : 市场运营部经理
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private int confirmPay;
            private int isConcern;
            private int liveId;
            private String liveIntroduce;
            private double liveMoney;
            private String liveName;
            private String livePepoleNum;
            private String livePicMax;
            private String livePicMin;
            private String liveStatus;
            private String liveTime;
            private String liveVideo;
            private String liveViewNum;
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

            public String getAppUserPic() {
                return appUserPic;
            }

            public void setAppUserPic(String appUserPic) {
                this.appUserPic = appUserPic;
            }

            public int getConfirmPay() {
                return confirmPay;
            }

            public void setConfirmPay(int confirmPay) {
                this.confirmPay = confirmPay;
            }

            public int getIsConcern() {
                return isConcern;
            }

            public void setIsConcern(int isConcern) {
                this.isConcern = isConcern;
            }

            public int getLiveId() {
                return liveId;
            }

            public void setLiveId(int liveId) {
                this.liveId = liveId;
            }

            public String getLiveIntroduce() {
                return liveIntroduce;
            }

            public void setLiveIntroduce(String liveIntroduce) {
                this.liveIntroduce = liveIntroduce;
            }

            public double getLiveMoney() {
                return liveMoney;
            }

            public void setLiveMoney(double liveMoney) {
                this.liveMoney = liveMoney;
            }

            public String getLiveName() {
                return liveName;
            }

            public void setLiveName(String liveName) {
                this.liveName = liveName;
            }

            public String getLivePepoleNum() {
                return livePepoleNum;
            }

            public void setLivePepoleNum(String livePepoleNum) {
                this.livePepoleNum = livePepoleNum;
            }

            public String getLivePicMax() {
                return livePicMax;
            }

            public void setLivePicMax(String livePicMax) {
                this.livePicMax = livePicMax;
            }

            public String getLivePicMin() {
                return livePicMin;
            }

            public void setLivePicMin(String livePicMin) {
                this.livePicMin = livePicMin;
            }

            public String getLiveStatus() {
                return liveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                this.liveStatus = liveStatus;
            }

            public String getLiveTime() {
                return liveTime;
            }

            public void setLiveTime(String liveTime) {
                this.liveTime = liveTime;
            }

            public String getLiveVideo() {
                return liveVideo;
            }

            public void setLiveVideo(String liveVideo) {
                this.liveVideo = liveVideo;
            }

            public String getLiveViewNum() {
                return liveViewNum;
            }

            public void setLiveViewNum(String liveViewNum) {
                this.liveViewNum = liveViewNum;
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
