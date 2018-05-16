package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by peng on 2017/9/27.
 */
@Entity
public class SmallBean {

    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

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

    @Generated(hash = 1723210589)
    public SmallBean(Long id, String appUserId, String appUserName,
                     String appUserPic, String audioPath, String moduleClassName,
                     String position, String signUpNum, int smallClassId,
                     int smallClassModuleId, String smallClassName, String smallClassPicMax,
                     String smallClassPicMin, int typeId, String typeName, double videoMoney,
                     String videoName, String videoPath, String viewNum, String videoId) {
        this.id = id;
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

    public SmallBean(String appUserId, String appUserName,
                     String appUserPic, String audioPath, String moduleClassName,
                     String position, String signUpNum, int smallClassId,
                     int smallClassModuleId, String smallClassName, String smallClassPicMax,
                     String smallClassPicMin, int typeId, String typeName, double videoMoney,
                     String videoName, String videoPath, String viewNum, String videoId) {
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

    @Generated(hash = 1461656919)
    public SmallBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppUserId() {
        return this.appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getAppUserName() {
        return this.appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getAppUserPic() {
        return this.appUserPic;
    }

    public void setAppUserPic(String appUserPic) {
        this.appUserPic = appUserPic;
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getModuleClassName() {
        return this.moduleClassName;
    }

    public void setModuleClassName(String moduleClassName) {
        this.moduleClassName = moduleClassName;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSignUpNum() {
        return this.signUpNum;
    }

    public void setSignUpNum(String signUpNum) {
        this.signUpNum = signUpNum;
    }

    public int getSmallClassId() {
        return this.smallClassId;
    }

    public void setSmallClassId(int smallClassId) {
        this.smallClassId = smallClassId;
    }

    public int getSmallClassModuleId() {
        return this.smallClassModuleId;
    }

    public void setSmallClassModuleId(int smallClassModuleId) {
        this.smallClassModuleId = smallClassModuleId;
    }

    public String getSmallClassName() {
        return this.smallClassName;
    }

    public void setSmallClassName(String smallClassName) {
        this.smallClassName = smallClassName;
    }

    public String getSmallClassPicMax() {
        return this.smallClassPicMax;
    }

    public void setSmallClassPicMax(String smallClassPicMax) {
        this.smallClassPicMax = smallClassPicMax;
    }

    public String getSmallClassPicMin() {
        return this.smallClassPicMin;
    }

    public void setSmallClassPicMin(String smallClassPicMin) {
        this.smallClassPicMin = smallClassPicMin;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getVideoMoney() {
        return this.videoMoney;
    }

    public void setVideoMoney(double videoMoney) {
        this.videoMoney = videoMoney;
    }

    public String getVideoName() {
        return this.videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return this.videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getViewNum() {
        return this.viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
