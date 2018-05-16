package com.tch.zx.http.bean.result;

import java.util.List;

/**
 * Created by peng on 2017/7/14.
 */

public class HomeResultBean {

    private String ret;

    private Result result;

    public HomeResultBean() {
    }

    public HomeResultBean(String ret, Result result) {
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
        private List<Special> special;

        private List<Small> small;

        private List<Live> live;

        public Result() {
        }

        public Result(List<Special> special, List<Small> small, List<Live> live) {
            this.special = special;
            this.small = small;
            this.live = live;
        }

        public List<Special> getSpecial() {
            return special;
        }

        public void setSpecial(List<Special> special) {
            this.special = special;
        }

        public List<Small> getSmall() {
            return small;
        }

        public void setSmall(List<Small> small) {
            this.small = small;
        }

        public List<Live> getLive() {
            return live;
        }

        public void setLive(List<Live> live) {
            this.live = live;
        }
    }

    public class Live {
        private String appUserId;

        private String appUserName;

        private int liveId;

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

        private String userPic;

        public Live() {
        }

        public Live(String appUserId, String appUserName, int liveId, double liveMoney, String liveName, String livePepoleNum, String livePicMax, String livePicMin, String liveStatus, String liveTime, String liveVideo, String liveViewNum, String position, String userPic) {
            this.appUserId = appUserId;
            this.appUserName = appUserName;
            this.liveId = liveId;
            this.liveMoney = liveMoney;
            this.liveName = liveName;
            this.livePepoleNum = livePepoleNum;
            this.livePicMax = livePicMax;
            this.livePicMin = livePicMin;
            this.liveStatus = liveStatus;
            this.liveTime = liveTime;
            this.liveVideo = liveVideo;
            this.liveViewNum = liveViewNum;
            this.position = position;
            this.userPic = userPic;
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

        public int getLiveId() {
            return liveId;
        }

        public void setLiveId(int liveId) {
            this.liveId = liveId;
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

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }
    }

    public class Small {
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

        private String videoId;

        public Small() {
        }

        public Small(String appUserId, String appUserName, String appUserPic, String audioPath, String moduleClassName, String position, String signUpNum, int smallClassId, int smallClassModuleId, String smallClassName, String smallClassPicMax, String smallClassPicMin, int typeId, String typeName, double videoMoney, String videoName, String videoPath, String viewNum, String videoId) {
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
            this.videoId = videoId;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
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

    public class Special {
        private String appUserId;

        private String appUserName;

        private String appUserPic;

        private String position;

        private String specialColumnByName;

        private String specialColumnClassCreateDate;

        private String specialColumnClassId;

        private String specialColumnId;

        private String specialColumnName;

        private String specialColumnPicMax;

        private String specialColumnPicMin;

        private String specialColumnPrice;

        private String subscriptionNumber;

        public Special() {
        }

        public Special(String appUserId, String appUserName, String appUserPic, String position, String specialColumnByName, String specialColumnClassCreateDate, String specialColumnClassId, String specialColumnId, String specialColumnName, String specialColumnPicMax, String specialColumnPicMin, String specialColumnPrice, String subscriptionNumber) {
            this.appUserId = appUserId;
            this.appUserName = appUserName;
            this.appUserPic = appUserPic;
            this.position = position;
            this.specialColumnByName = specialColumnByName;
            this.specialColumnClassCreateDate = specialColumnClassCreateDate;
            this.specialColumnClassId = specialColumnClassId;
            this.specialColumnId = specialColumnId;
            this.specialColumnName = specialColumnName;
            this.specialColumnPicMax = specialColumnPicMax;
            this.specialColumnPicMin = specialColumnPicMin;
            this.specialColumnPrice = specialColumnPrice;
            this.subscriptionNumber = subscriptionNumber;
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

        public String getSpecialColumnPrice() {
            return specialColumnPrice;
        }

        public void setSpecialColumnPrice(String specialColumnPrice) {
            this.specialColumnPrice = specialColumnPrice;
        }

        public String getSubscriptionNumber() {
            return subscriptionNumber;
        }

        public void setSubscriptionNumber(String subscriptionNumber) {
            this.subscriptionNumber = subscriptionNumber;
        }
    }
}
