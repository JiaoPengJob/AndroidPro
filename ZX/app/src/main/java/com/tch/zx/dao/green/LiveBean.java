package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by peng on 2017/9/27.
 */
@Entity
public class LiveBean {

    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

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

    @Generated(hash = 1277007589)
    public LiveBean(Long id, String appUserId, String appUserName, int liveId,
                    double liveMoney, String liveName, String livePepoleNum,
                    String livePicMax, String livePicMin, String liveStatus,
                    String liveTime, String liveVideo, String liveViewNum, String position,
                    String userPic) {
        this.id = id;
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

    public LiveBean(String appUserId, String appUserName, int liveId, double liveMoney, String liveName, String livePepoleNum, String livePicMax, String livePicMin, String liveStatus, String liveTime, String liveVideo, String liveViewNum, String position, String userPic) {
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

    @Generated(hash = 1476633094)
    public LiveBean() {
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

    public int getLiveId() {
        return this.liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public double getLiveMoney() {
        return this.liveMoney;
    }

    public void setLiveMoney(double liveMoney) {
        this.liveMoney = liveMoney;
    }

    public String getLiveName() {
        return this.liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }

    public String getLivePepoleNum() {
        return this.livePepoleNum;
    }

    public void setLivePepoleNum(String livePepoleNum) {
        this.livePepoleNum = livePepoleNum;
    }

    public String getLivePicMax() {
        return this.livePicMax;
    }

    public void setLivePicMax(String livePicMax) {
        this.livePicMax = livePicMax;
    }

    public String getLivePicMin() {
        return this.livePicMin;
    }

    public void setLivePicMin(String livePicMin) {
        this.livePicMin = livePicMin;
    }

    public String getLiveStatus() {
        return this.liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getLiveTime() {
        return this.liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public String getLiveVideo() {
        return this.liveVideo;
    }

    public void setLiveVideo(String liveVideo) {
        this.liveVideo = liveVideo;
    }

    public String getLiveViewNum() {
        return this.liveViewNum;
    }

    public void setLiveViewNum(String liveViewNum) {
        this.liveViewNum = liveViewNum;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserPic() {
        return this.userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
