package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/14.
 */

public class SmallListResultBean {

    private String ret;

    private Result result;

    public SmallListResultBean() {
    }

    public SmallListResultBean(String ret, Result result) {
        this.ret = ret;
        this.result = result;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private int pageNow;

        private List<ResponseObject> responseObject;

        public Result() {
        }

        public Result(int pageNow, List<ResponseObject> responseObject) {
            this.pageNow = pageNow;
            this.responseObject = responseObject;
        }


        public int getPageNow() {
            return pageNow;
        }

        public void setPageNow(int pageNow) {
            this.pageNow = pageNow;
        }

        public List<ResponseObject> getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(List<ResponseObject> responseObject) {
            this.responseObject = responseObject;
        }
    }

    public class ResponseObject {
        private String appUserId;

        private String appUserName;

        private String appUserPic;

        private String audioPath;

        private String moduleClassName;

        private String position;

        private String signUpNum;

        private int smallClassId;

        private int smallClassModuleId;

        private String smallClassName;

        private String smallClassPicMax;

        private String smallClassPicMin;

        private int typeId;

        private String typeName;

        private double videoMoney;

        private String videoName;

        private String videoPath;

        private String viewNum;

        public ResponseObject() {
        }

        public ResponseObject(String appUserId, String appUserName, String appUserPic, String audioPath, String moduleClassName, String position, String signUpNum, int smallClassId, int smallClassModuleId, String smallClassName, String smallClassPicMax, String smallClassPicMin, int typeId, String typeName, double videoMoney, String videoName, String videoPath, String viewNum) {
            this.appUserId = appUserId;
            this.appUserName = appUserName;
            this.appUserPic = appUserPic;
            this.audioPath = audioPath;
            this.moduleClassName = moduleClassName;
            this.position = position;
            this.signUpNum = signUpNum;
            this.smallClassId = smallClassId;
            this.smallClassModuleId = smallClassModuleId;
            this.smallClassName = smallClassName;
            this.smallClassPicMax = smallClassPicMax;
            this.smallClassPicMin = smallClassPicMin;
            this.typeId = typeId;
            this.typeName = typeName;
            this.videoMoney = videoMoney;
            this.videoName = videoName;
            this.videoPath = videoPath;
            this.viewNum = viewNum;
        }

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

        public String getAudioPath() {
            return audioPath;
        }

        public void setAudioPath(String audioPath) {
            this.audioPath = audioPath;
        }

        public String getModuleClassName() {
            return moduleClassName;
        }

        public void setModuleClassName(String moduleClassName) {
            this.moduleClassName = moduleClassName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getSignUpNum() {
            return signUpNum;
        }

        public void setSignUpNum(String signUpNum) {
            this.signUpNum = signUpNum;
        }

        public int getSmallClassId() {
            return smallClassId;
        }

        public void setSmallClassId(int smallClassId) {
            this.smallClassId = smallClassId;
        }

        public int getSmallClassModuleId() {
            return smallClassModuleId;
        }

        public void setSmallClassModuleId(int smallClassModuleId) {
            this.smallClassModuleId = smallClassModuleId;
        }

        public String getSmallClassName() {
            return smallClassName;
        }

        public void setSmallClassName(String smallClassName) {
            this.smallClassName = smallClassName;
        }

        public String getSmallClassPicMax() {
            return smallClassPicMax;
        }

        public void setSmallClassPicMax(String smallClassPicMax) {
            this.smallClassPicMax = smallClassPicMax;
        }

        public String getSmallClassPicMin() {
            return smallClassPicMin;
        }

        public void setSmallClassPicMin(String smallClassPicMin) {
            this.smallClassPicMin = smallClassPicMin;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public double getVideoMoney() {
            return videoMoney;
        }

        public void setVideoMoney(double videoMoney) {
            this.videoMoney = videoMoney;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getViewNum() {
            return viewNum;
        }

        public void setViewNum(String viewNum) {
            this.viewNum = viewNum;
        }
    }
}
