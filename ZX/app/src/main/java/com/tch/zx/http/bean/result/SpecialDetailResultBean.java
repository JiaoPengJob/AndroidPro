package com.tch.zx.http.bean.result;

/**
 * Created by peng on 2017/7/20.
 */

public class SpecialDetailResultBean {


    /**
     * ret : 1
     * result : {"responseObject":{"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","createDate":"2017-06-24 17:46:59.0","isCollect":0,"isConcern":0,"position":"市场运营部经理","specialColumnClassAudio":"http://115.28.95.41:8080/uploadfile/zhixian/201707/dac0cd89bac64eed852266470b312c0f.mp3","specialColumnClassId":4,"specialColumnClassIntroduce":"123","specialColumnClassName":"一线","specialColumnClassPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ebae89e0c32b4284bd4e5f345cd0981a.jpg","specialColumnClassPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/cfd0990e2eb04aaeaeac4ccdeaecedab.jpg","specialColumnClassVideo":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","specialColumnId":1,"specialColumnSignUpNumber":0,"viewNum":""}}
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
         * responseObject : {"appUserId":"d8f08b34f2b7490481097328bf3a66fb","appUserIntroduce":"测试","appUserName":"测试账号001","appUserPic":"http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg","createDate":"2017-06-24 17:46:59.0","isCollect":0,"isConcern":0,"position":"市场运营部经理","specialColumnClassAudio":"http://115.28.95.41:8080/uploadfile/zhixian/201707/dac0cd89bac64eed852266470b312c0f.mp3","specialColumnClassId":4,"specialColumnClassIntroduce":"123","specialColumnClassName":"一线","specialColumnClassPicMax":"http://115.28.95.41:8080/uploadfile/zhixian/201706/ebae89e0c32b4284bd4e5f345cd0981a.jpg","specialColumnClassPicMin":"http://115.28.95.41:8080/uploadfile/zhixian/201706/cfd0990e2eb04aaeaeac4ccdeaecedab.jpg","specialColumnClassVideo":"http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4","specialColumnId":1,"specialColumnSignUpNumber":0,"viewNum":""}
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
             * appUserId : d8f08b34f2b7490481097328bf3a66fb
             * appUserIntroduce : 测试
             * appUserName : 测试账号001
             * appUserPic : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
             * createDate : 2017-06-24 17:46:59.0
             * isCollect : 0
             * isConcern : 0
             * position : 市场运营部经理
             * specialColumnClassAudio : http://115.28.95.41:8080/uploadfile/zhixian/201707/dac0cd89bac64eed852266470b312c0f.mp3
             * specialColumnClassId : 4
             * specialColumnClassIntroduce : 123
             * specialColumnClassName : 一线
             * specialColumnClassPicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/ebae89e0c32b4284bd4e5f345cd0981a.jpg
             * specialColumnClassPicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/cfd0990e2eb04aaeaeac4ccdeaecedab.jpg
             * specialColumnClassVideo : http://115.28.95.41:8080/uploadfile/zhixian/201706/148c9668a5f54045853288a47e24917d.mp4
             * specialColumnId : 1
             * specialColumnSignUpNumber : 0
             * viewNum :
             */

            private String appUserId;
            private String appUserIntroduce;
            private String appUserName;
            private String appUserPic;
            private String createDate;
            private int isCollect;
            private int isConcern;
            private String position;
            private String specialColumnClassAudio;
            private int specialColumnClassId;
            private String specialColumnClassIntroduce;
            private String specialColumnClassName;
            private String specialColumnClassPicMax;
            private String specialColumnClassPicMin;
            private String specialColumnClassVideo;
            private int specialColumnId;
            private int specialColumnSignUpNumber;
            private String viewNum;

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

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
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

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getSpecialColumnClassAudio() {
                return specialColumnClassAudio;
            }

            public void setSpecialColumnClassAudio(String specialColumnClassAudio) {
                this.specialColumnClassAudio = specialColumnClassAudio;
            }

            public int getSpecialColumnClassId() {
                return specialColumnClassId;
            }

            public void setSpecialColumnClassId(int specialColumnClassId) {
                this.specialColumnClassId = specialColumnClassId;
            }

            public String getSpecialColumnClassIntroduce() {
                return specialColumnClassIntroduce;
            }

            public void setSpecialColumnClassIntroduce(String specialColumnClassIntroduce) {
                this.specialColumnClassIntroduce = specialColumnClassIntroduce;
            }

            public String getSpecialColumnClassName() {
                return specialColumnClassName;
            }

            public void setSpecialColumnClassName(String specialColumnClassName) {
                this.specialColumnClassName = specialColumnClassName;
            }

            public String getSpecialColumnClassPicMax() {
                return specialColumnClassPicMax;
            }

            public void setSpecialColumnClassPicMax(String specialColumnClassPicMax) {
                this.specialColumnClassPicMax = specialColumnClassPicMax;
            }

            public String getSpecialColumnClassPicMin() {
                return specialColumnClassPicMin;
            }

            public void setSpecialColumnClassPicMin(String specialColumnClassPicMin) {
                this.specialColumnClassPicMin = specialColumnClassPicMin;
            }

            public String getSpecialColumnClassVideo() {
                return specialColumnClassVideo;
            }

            public void setSpecialColumnClassVideo(String specialColumnClassVideo) {
                this.specialColumnClassVideo = specialColumnClassVideo;
            }

            public int getSpecialColumnId() {
                return specialColumnId;
            }

            public void setSpecialColumnId(int specialColumnId) {
                this.specialColumnId = specialColumnId;
            }

            public int getSpecialColumnSignUpNumber() {
                return specialColumnSignUpNumber;
            }

            public void setSpecialColumnSignUpNumber(int specialColumnSignUpNumber) {
                this.specialColumnSignUpNumber = specialColumnSignUpNumber;
            }

            public String getViewNum() {
                return viewNum;
            }

            public void setViewNum(String viewNum) {
                this.viewNum = viewNum;
            }
        }
    }
}
