package com.tch.zx.http.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/7/19.
 */

public class SpecialSubscribeResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"appUserId":"","appUserName":"","appUserPic":"","confirmPay":0,"position":"","specialColumnByName":"法律讲堂1","specialColumnClassCreateDate":"","specialColumnClassId":"","specialColumnId":"","specialColumnIntroduce":"asdasd\r\n\t\t\t\t\t\t\r\n\t\t\t\t\t\t","specialColumnName":"法律12","specialColumnPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4e91921f6bc743b4b04c2fda6e6a9dab.jpg","specialColumnPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/8ca6b8f2167d4cf9b4fbc42b6e4b8d7d.jpg","specialColumnPrice":3330.1,"subscriptionNotes":"asda","subscriptionNumber":""}}
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
         * responseObject : {"appUserId":"","appUserName":"","appUserPic":"","confirmPay":0,"position":"","specialColumnByName":"法律讲堂1","specialColumnClassCreateDate":"","specialColumnClassId":"","specialColumnId":"","specialColumnIntroduce":"asdasd\r\n\t\t\t\t\t\t\r\n\t\t\t\t\t\t","specialColumnName":"法律12","specialColumnPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/4e91921f6bc743b4b04c2fda6e6a9dab.jpg","specialColumnPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/8ca6b8f2167d4cf9b4fbc42b6e4b8d7d.jpg","specialColumnPrice":3330.1,"subscriptionNotes":"asda","subscriptionNumber":""}
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
             * appUserId :
             * appUserName :
             * appUserPic :
             * confirmPay : 0
             * position :
             * specialColumnByName : 法律讲堂1
             * specialColumnClassCreateDate :
             * specialColumnClassId :
             * specialColumnId :
             * specialColumnIntroduce : asdasd


             * specialColumnName : 法律12
             * specialColumnPicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/4e91921f6bc743b4b04c2fda6e6a9dab.jpg
             * specialColumnPicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/8ca6b8f2167d4cf9b4fbc42b6e4b8d7d.jpg
             * specialColumnPrice : 3330.1
             * subscriptionNotes : asda
             * subscriptionNumber :
             */

            private String appUserId;
            private String appUserName;
            private String appUserPic;
            private int confirmPay;
            private String position;
            private String specialColumnByName;
            private String specialColumnClassCreateDate;
            private String specialColumnClassId;
            private String specialColumnId;
            private String specialColumnIntroduce;
            private String specialColumnName;
            private String specialColumnPicMax;
            private String specialColumnPicMin;
            private double specialColumnPrice;
            private String subscriptionNotes;
            private String subscriptionNumber;

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

            public int getConfirmPay() {
                return confirmPay;
            }

            public void setConfirmPay(int confirmPay) {
                this.confirmPay = confirmPay;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getSpecialColumnByName() {
                return specialColumnByName;
            }

            public void setSpecialColumnByName(String specialColumnByName) {
                this.specialColumnByName = specialColumnByName;
            }

            public String getSpecialColumnClassCreateDate() {
                return specialColumnClassCreateDate;
            }

            public void setSpecialColumnClassCreateDate(String specialColumnClassCreateDate) {
                this.specialColumnClassCreateDate = specialColumnClassCreateDate;
            }

            public String getSpecialColumnClassId() {
                return specialColumnClassId;
            }

            public void setSpecialColumnClassId(String specialColumnClassId) {
                this.specialColumnClassId = specialColumnClassId;
            }

            public String getSpecialColumnId() {
                return specialColumnId;
            }

            public void setSpecialColumnId(String specialColumnId) {
                this.specialColumnId = specialColumnId;
            }

            public String getSpecialColumnIntroduce() {
                return specialColumnIntroduce;
            }

            public void setSpecialColumnIntroduce(String specialColumnIntroduce) {
                this.specialColumnIntroduce = specialColumnIntroduce;
            }

            public String getSpecialColumnName() {
                return specialColumnName;
            }

            public void setSpecialColumnName(String specialColumnName) {
                this.specialColumnName = specialColumnName;
            }

            public String getSpecialColumnPicMax() {
                return specialColumnPicMax;
            }

            public void setSpecialColumnPicMax(String specialColumnPicMax) {
                this.specialColumnPicMax = specialColumnPicMax;
            }

            public String getSpecialColumnPicMin() {
                return specialColumnPicMin;
            }

            public void setSpecialColumnPicMin(String specialColumnPicMin) {
                this.specialColumnPicMin = specialColumnPicMin;
            }

            public double getSpecialColumnPrice() {
                return specialColumnPrice;
            }

            public void setSpecialColumnPrice(double specialColumnPrice) {
                this.specialColumnPrice = specialColumnPrice;
            }

            public String getSubscriptionNotes() {
                return subscriptionNotes;
            }

            public void setSubscriptionNotes(String subscriptionNotes) {
                this.subscriptionNotes = subscriptionNotes;
            }

            public String getSubscriptionNumber() {
                return subscriptionNumber;
            }

            public void setSubscriptionNumber(String subscriptionNumber) {
                this.subscriptionNumber = subscriptionNumber;
            }
        }
    }
}
