package com.tch.zx.bean;

import java.util.List;

/**
 * Created by peng on 2017/7/11.
 */

public class HomeBean {


    private List<SpecialBean> special;
    private List<SmallBean> small;
    private List<LiveBean> live;

    public List<SpecialBean> getSpecial() {
        return special;
    }

    public void setSpecial(List<SpecialBean> special) {
        this.special = special;
    }

    public List<SmallBean> getSmall() {
        return small;
    }

    public void setSmall(List<SmallBean> small) {
        this.small = small;
    }

    public List<LiveBean> getLive() {
        return live;
    }

    public void setLive(List<LiveBean> live) {
        this.live = live;
    }

    public static class SpecialBean {
        /**
         * appUserId : d8f08b34f2b7490481097328bf3a66fb
         * appUserName : 测试账号001
         * appUserPic :
         * position : 市场运营部经理
         * specialColumnByName : 篮球
         * specialColumnClassCreateDate : 2017-06-27 16:47:04.0
         * specialColumnClassId : 14
         * specialColumnClassPicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/1efa9b6d82d84c0caf5fb58ea57aaf4d.jpg
         * specialColumnClassPicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/2714fbc8c8474826afadeabe63dffca9.jpg
         * specialColumnId : 9
         * specialColumnName : 体育 //主
         * specialColumnPrice : 230.40
         */

        private String appUserId;
        private String appUserName;
        private String appUserPic;
        private String position;
        private String specialColumnByName;
        private String specialColumnClassCreateDate;
        private String specialColumnClassId;
        private String specialColumnClassPicMax;
        private String specialColumnClassPicMin;
        private String specialColumnId;
        private String specialColumnName;
        private String specialColumnPrice;

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

        public String getSpecialColumnPrice() {
            return specialColumnPrice;
        }

        public void setSpecialColumnPrice(String specialColumnPrice) {
            this.specialColumnPrice = specialColumnPrice;
        }
    }

    public static class SmallBean {
        /**
         * appUserId : d8f08b34f2b7490481097328bf3a66fb
         * appUserName : 测试账号001
         * appUserPic :
         * audioPath : http://115.28.95.41:8080/uploadfile/zhixian/201706/f5b253e741254132adc489893d4bb768.jpg
         * position : 市场运营部经理
         * signUpNum : 1000 //报名人数
         * smallClassId : 1
         * smallClassModuleId : 1
         * smallClassName : 测试课程
         * smallClassPicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/1b192a0f77fd4906969981b0e118843e.jpg
         * smallClassPicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/e4ee640434e04b72ab773a41d4ce9afa.jpg
         * smallLookNum : 0 //
         * typeId : 1
         * typeName : 互联网
         * videoMoney : 80.52
         * videoName : 测试2
         * videoPath : http://115.28.95.41:8080/uploadfile/zhixian/201706/1262230892034ae494bcf3b256463aac.jpg
         * viewNum : 842 //观看人数
         */

        private String appUserId;
        private String appUserName;
        private String appUserPic;
        private String audioPath;
        private String position;
        private String signUpNum;
        private int smallClassId;
        private int smallClassModuleId;
        private String smallClassName;
        private String smallClassPicMax;
        private String smallClassPicMin;
        private int smallLookNum;
        private int typeId;
        private String typeName;
        private double videoMoney;
        private String videoName;
        private String videoPath;
        private String viewNum;

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

        public int getSmallLookNum() {
            return smallLookNum;
        }

        public void setSmallLookNum(int smallLookNum) {
            this.smallLookNum = smallLookNum;
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

    public static class LiveBean {
        /**
         * appUserId : d8f08b34f2b7490481097328bf3a66fb
         * appUserName : 测试账号001
         * liveId : 2
         * liveMoney : 444.5
         * liveName : 测试1
         * livePepoleNum : 1452
         * livePicMax : http://115.28.95.41:8080/uploadfile/zhixian/201706/a5a538f70fb643c4b0cbd6b823790081.jpg
         * livePicMin : http://115.28.95.41:8080/uploadfile/zhixian/201706/4f289f4170ee46e7948847e1f47655ba.jpg
         * liveStatus : 2
         * liveTime : 1496764800000
         * liveVideo :
         * liveViewNum : 1000
         * position : 市场运营部经理
         * userPic :
         */

        private String appUserId;
        private String appUserName;
        private int liveId;
        private double liveMoney;
        private String liveName;
        private String livePepoleNum;
        private String livePicMax;
        private String livePicMin;
        private String liveStatus;
        private long liveTime;
        private String liveVideo;
        private String liveViewNum;
        private String position;
        private String userPic;

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

        public long getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(long liveTime) {
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
}
