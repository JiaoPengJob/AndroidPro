package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by peng on 2017/9/27.
 */
@Entity
public class SpecialBean {

    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

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

    @Generated(hash = 39471690)
    public SpecialBean(Long id, String appUserId, String appUserName,
                       String appUserPic, String position, String specialColumnByName,
                       String specialColumnClassCreateDate, String specialColumnClassId,
                       String specialColumnId, String specialColumnName,
                       String specialColumnPicMax, String specialColumnPicMin,
                       String specialColumnPrice, String subscriptionNumber) {
        this.id = id;
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

    public SpecialBean(String appUserId, String appUserName, String appUserPic, String position, String specialColumnByName, String specialColumnClassCreateDate, String specialColumnClassId, String specialColumnId, String specialColumnName, String specialColumnPicMax, String specialColumnPicMin, String specialColumnPrice, String subscriptionNumber) {
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

    @Generated(hash = 645540276)
    public SpecialBean() {
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

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpecialColumnByName() {
        return this.specialColumnByName;
    }

    public void setSpecialColumnByName(String specialColumnByName) {
        this.specialColumnByName = specialColumnByName;
    }

    public String getSpecialColumnClassCreateDate() {
        return this.specialColumnClassCreateDate;
    }

    public void setSpecialColumnClassCreateDate(
            String specialColumnClassCreateDate) {
        this.specialColumnClassCreateDate = specialColumnClassCreateDate;
    }

    public String getSpecialColumnClassId() {
        return this.specialColumnClassId;
    }

    public void setSpecialColumnClassId(String specialColumnClassId) {
        this.specialColumnClassId = specialColumnClassId;
    }

    public String getSpecialColumnId() {
        return this.specialColumnId;
    }

    public void setSpecialColumnId(String specialColumnId) {
        this.specialColumnId = specialColumnId;
    }

    public String getSpecialColumnName() {
        return this.specialColumnName;
    }

    public void setSpecialColumnName(String specialColumnName) {
        this.specialColumnName = specialColumnName;
    }

    public String getSpecialColumnPicMax() {
        return this.specialColumnPicMax;
    }

    public void setSpecialColumnPicMax(String specialColumnPicMax) {
        this.specialColumnPicMax = specialColumnPicMax;
    }

    public String getSpecialColumnPicMin() {
        return this.specialColumnPicMin;
    }

    public void setSpecialColumnPicMin(String specialColumnPicMin) {
        this.specialColumnPicMin = specialColumnPicMin;
    }

    public String getSpecialColumnPrice() {
        return this.specialColumnPrice;
    }

    public void setSpecialColumnPrice(String specialColumnPrice) {
        this.specialColumnPrice = specialColumnPrice;
    }

    public String getSubscriptionNumber() {
        return this.subscriptionNumber;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }
}
