package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户信息
 */
@Entity
public class UserBean {

    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

    private String loginType;
    private String add_frid_set;
    private String adrCity;
    private String adrCounty;
    private String adrProvince;
    private String appUserId;
    private String appUserIntroduce;
    private String appUserName;
    private String appUserPic;
    private String appUserSex;
    private String categoryName;
    private String companyAddress;
    private String companyGif;
    private String companyId;
    private int companyIndustryFType;
    private int companyIndustrySType;
    private String companyIntroduce;
    private String companyLogo;
    private String companyName;
    private String companyPosition;
    private String companyVideo;
    private String email;
    private String followIndustry;
    private String needIndustry;
    private String offerIndustry;
    private String only_dk;
    private String stypeName;
    private String userFreeType;
    private String userType;
    private String yunToken;
    private String companyPicList;

    @Generated(hash = 1145496134)
    public UserBean(Long id, String loginType, String add_frid_set, String adrCity,
            String adrCounty, String adrProvince, String appUserId,
            String appUserIntroduce, String appUserName, String appUserPic,
            String appUserSex, String categoryName, String companyAddress,
            String companyGif, String companyId, int companyIndustryFType,
            int companyIndustrySType, String companyIntroduce, String companyLogo,
            String companyName, String companyPosition, String companyVideo,
            String email, String followIndustry, String needIndustry,
            String offerIndustry, String only_dk, String stypeName,
            String userFreeType, String userType, String yunToken,
            String companyPicList) {
        this.id = id;
        this.loginType = loginType;
        this.add_frid_set = add_frid_set;
        this.adrCity = adrCity;
        this.adrCounty = adrCounty;
        this.adrProvince = adrProvince;
        this.appUserId = appUserId;
        this.appUserIntroduce = appUserIntroduce;
        this.appUserName = appUserName;
        this.appUserPic = appUserPic;
        this.appUserSex = appUserSex;
        this.categoryName = categoryName;
        this.companyAddress = companyAddress;
        this.companyGif = companyGif;
        this.companyId = companyId;
        this.companyIndustryFType = companyIndustryFType;
        this.companyIndustrySType = companyIndustrySType;
        this.companyIntroduce = companyIntroduce;
        this.companyLogo = companyLogo;
        this.companyName = companyName;
        this.companyPosition = companyPosition;
        this.companyVideo = companyVideo;
        this.email = email;
        this.followIndustry = followIndustry;
        this.needIndustry = needIndustry;
        this.offerIndustry = offerIndustry;
        this.only_dk = only_dk;
        this.stypeName = stypeName;
        this.userFreeType = userFreeType;
        this.userType = userType;
        this.yunToken = yunToken;
        this.companyPicList = companyPicList;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLoginType() {
        return this.loginType;
    }
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    public String getAdd_frid_set() {
        return this.add_frid_set;
    }
    public void setAdd_frid_set(String add_frid_set) {
        this.add_frid_set = add_frid_set;
    }
    public String getAdrCity() {
        return this.adrCity;
    }
    public void setAdrCity(String adrCity) {
        this.adrCity = adrCity;
    }
    public String getAdrCounty() {
        return this.adrCounty;
    }
    public void setAdrCounty(String adrCounty) {
        this.adrCounty = adrCounty;
    }
    public String getAdrProvince() {
        return this.adrProvince;
    }
    public void setAdrProvince(String adrProvince) {
        this.adrProvince = adrProvince;
    }
    public String getAppUserId() {
        return this.appUserId;
    }
    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }
    public String getAppUserIntroduce() {
        return this.appUserIntroduce;
    }
    public void setAppUserIntroduce(String appUserIntroduce) {
        this.appUserIntroduce = appUserIntroduce;
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
    public String getAppUserSex() {
        return this.appUserSex;
    }
    public void setAppUserSex(String appUserSex) {
        this.appUserSex = appUserSex;
    }
    public String getCategoryName() {
        return this.categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCompanyAddress() {
        return this.companyAddress;
    }
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
    public String getCompanyGif() {
        return this.companyGif;
    }
    public void setCompanyGif(String companyGif) {
        this.companyGif = companyGif;
    }
    public String getCompanyId() {
        return this.companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public int getCompanyIndustryFType() {
        return this.companyIndustryFType;
    }
    public void setCompanyIndustryFType(int companyIndustryFType) {
        this.companyIndustryFType = companyIndustryFType;
    }
    public int getCompanyIndustrySType() {
        return this.companyIndustrySType;
    }
    public void setCompanyIndustrySType(int companyIndustrySType) {
        this.companyIndustrySType = companyIndustrySType;
    }
    public String getCompanyIntroduce() {
        return this.companyIntroduce;
    }
    public void setCompanyIntroduce(String companyIntroduce) {
        this.companyIntroduce = companyIntroduce;
    }
    public String getCompanyLogo() {
        return this.companyLogo;
    }
    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyPosition() {
        return this.companyPosition;
    }
    public void setCompanyPosition(String companyPosition) {
        this.companyPosition = companyPosition;
    }
    public String getCompanyVideo() {
        return this.companyVideo;
    }
    public void setCompanyVideo(String companyVideo) {
        this.companyVideo = companyVideo;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFollowIndustry() {
        return this.followIndustry;
    }
    public void setFollowIndustry(String followIndustry) {
        this.followIndustry = followIndustry;
    }
    public String getNeedIndustry() {
        return this.needIndustry;
    }
    public void setNeedIndustry(String needIndustry) {
        this.needIndustry = needIndustry;
    }
    public String getOfferIndustry() {
        return this.offerIndustry;
    }
    public void setOfferIndustry(String offerIndustry) {
        this.offerIndustry = offerIndustry;
    }
    public String getOnly_dk() {
        return this.only_dk;
    }
    public void setOnly_dk(String only_dk) {
        this.only_dk = only_dk;
    }
    public String getStypeName() {
        return this.stypeName;
    }
    public void setStypeName(String stypeName) {
        this.stypeName = stypeName;
    }
    public String getUserFreeType() {
        return this.userFreeType;
    }
    public void setUserFreeType(String userFreeType) {
        this.userFreeType = userFreeType;
    }
    public String getUserType() {
        return this.userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getYunToken() {
        return this.yunToken;
    }
    public void setYunToken(String yunToken) {
        this.yunToken = yunToken;
    }
    public String getCompanyPicList() {
        return this.companyPicList;
    }
    public void setCompanyPicList(String companyPicList) {
        this.companyPicList = companyPicList;
    }

}
