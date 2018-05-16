package com.tch.zx.dao.green;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_BEAN".
*/
public class UserBeanDao extends AbstractDao<UserBean, Long> {

    public static final String TABLENAME = "USER_BEAN";

    /**
     * Properties of entity UserBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property LoginType = new Property(1, String.class, "loginType", false, "LOGIN_TYPE");
        public final static Property Add_frid_set = new Property(2, String.class, "add_frid_set", false, "ADD_FRID_SET");
        public final static Property AdrCity = new Property(3, String.class, "adrCity", false, "ADR_CITY");
        public final static Property AdrCounty = new Property(4, String.class, "adrCounty", false, "ADR_COUNTY");
        public final static Property AdrProvince = new Property(5, String.class, "adrProvince", false, "ADR_PROVINCE");
        public final static Property AppUserId = new Property(6, String.class, "appUserId", false, "APP_USER_ID");
        public final static Property AppUserIntroduce = new Property(7, String.class, "appUserIntroduce", false, "APP_USER_INTRODUCE");
        public final static Property AppUserName = new Property(8, String.class, "appUserName", false, "APP_USER_NAME");
        public final static Property AppUserPic = new Property(9, String.class, "appUserPic", false, "APP_USER_PIC");
        public final static Property AppUserSex = new Property(10, String.class, "appUserSex", false, "APP_USER_SEX");
        public final static Property CategoryName = new Property(11, String.class, "categoryName", false, "CATEGORY_NAME");
        public final static Property CompanyAddress = new Property(12, String.class, "companyAddress", false, "COMPANY_ADDRESS");
        public final static Property CompanyGif = new Property(13, String.class, "companyGif", false, "COMPANY_GIF");
        public final static Property CompanyId = new Property(14, String.class, "companyId", false, "COMPANY_ID");
        public final static Property CompanyIndustryFType = new Property(15, int.class, "companyIndustryFType", false, "COMPANY_INDUSTRY_FTYPE");
        public final static Property CompanyIndustrySType = new Property(16, int.class, "companyIndustrySType", false, "COMPANY_INDUSTRY_STYPE");
        public final static Property CompanyIntroduce = new Property(17, String.class, "companyIntroduce", false, "COMPANY_INTRODUCE");
        public final static Property CompanyLogo = new Property(18, String.class, "companyLogo", false, "COMPANY_LOGO");
        public final static Property CompanyName = new Property(19, String.class, "companyName", false, "COMPANY_NAME");
        public final static Property CompanyPosition = new Property(20, String.class, "companyPosition", false, "COMPANY_POSITION");
        public final static Property CompanyVideo = new Property(21, String.class, "companyVideo", false, "COMPANY_VIDEO");
        public final static Property Email = new Property(22, String.class, "email", false, "EMAIL");
        public final static Property FollowIndustry = new Property(23, String.class, "followIndustry", false, "FOLLOW_INDUSTRY");
        public final static Property NeedIndustry = new Property(24, String.class, "needIndustry", false, "NEED_INDUSTRY");
        public final static Property OfferIndustry = new Property(25, String.class, "offerIndustry", false, "OFFER_INDUSTRY");
        public final static Property Only_dk = new Property(26, String.class, "only_dk", false, "ONLY_DK");
        public final static Property StypeName = new Property(27, String.class, "stypeName", false, "STYPE_NAME");
        public final static Property UserFreeType = new Property(28, String.class, "userFreeType", false, "USER_FREE_TYPE");
        public final static Property UserType = new Property(29, String.class, "userType", false, "USER_TYPE");
        public final static Property YunToken = new Property(30, String.class, "yunToken", false, "YUN_TOKEN");
        public final static Property CompanyPicList = new Property(31, String.class, "companyPicList", false, "COMPANY_PIC_LIST");
    }


    public UserBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LOGIN_TYPE\" TEXT," + // 1: loginType
                "\"ADD_FRID_SET\" TEXT," + // 2: add_frid_set
                "\"ADR_CITY\" TEXT," + // 3: adrCity
                "\"ADR_COUNTY\" TEXT," + // 4: adrCounty
                "\"ADR_PROVINCE\" TEXT," + // 5: adrProvince
                "\"APP_USER_ID\" TEXT," + // 6: appUserId
                "\"APP_USER_INTRODUCE\" TEXT," + // 7: appUserIntroduce
                "\"APP_USER_NAME\" TEXT," + // 8: appUserName
                "\"APP_USER_PIC\" TEXT," + // 9: appUserPic
                "\"APP_USER_SEX\" TEXT," + // 10: appUserSex
                "\"CATEGORY_NAME\" TEXT," + // 11: categoryName
                "\"COMPANY_ADDRESS\" TEXT," + // 12: companyAddress
                "\"COMPANY_GIF\" TEXT," + // 13: companyGif
                "\"COMPANY_ID\" TEXT," + // 14: companyId
                "\"COMPANY_INDUSTRY_FTYPE\" INTEGER NOT NULL ," + // 15: companyIndustryFType
                "\"COMPANY_INDUSTRY_STYPE\" INTEGER NOT NULL ," + // 16: companyIndustrySType
                "\"COMPANY_INTRODUCE\" TEXT," + // 17: companyIntroduce
                "\"COMPANY_LOGO\" TEXT," + // 18: companyLogo
                "\"COMPANY_NAME\" TEXT," + // 19: companyName
                "\"COMPANY_POSITION\" TEXT," + // 20: companyPosition
                "\"COMPANY_VIDEO\" TEXT," + // 21: companyVideo
                "\"EMAIL\" TEXT," + // 22: email
                "\"FOLLOW_INDUSTRY\" TEXT," + // 23: followIndustry
                "\"NEED_INDUSTRY\" TEXT," + // 24: needIndustry
                "\"OFFER_INDUSTRY\" TEXT," + // 25: offerIndustry
                "\"ONLY_DK\" TEXT," + // 26: only_dk
                "\"STYPE_NAME\" TEXT," + // 27: stypeName
                "\"USER_FREE_TYPE\" TEXT," + // 28: userFreeType
                "\"USER_TYPE\" TEXT," + // 29: userType
                "\"YUN_TOKEN\" TEXT," + // 30: yunToken
                "\"COMPANY_PIC_LIST\" TEXT);"); // 31: companyPicList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String loginType = entity.getLoginType();
        if (loginType != null) {
            stmt.bindString(2, loginType);
        }
 
        String add_frid_set = entity.getAdd_frid_set();
        if (add_frid_set != null) {
            stmt.bindString(3, add_frid_set);
        }
 
        String adrCity = entity.getAdrCity();
        if (adrCity != null) {
            stmt.bindString(4, adrCity);
        }
 
        String adrCounty = entity.getAdrCounty();
        if (adrCounty != null) {
            stmt.bindString(5, adrCounty);
        }
 
        String adrProvince = entity.getAdrProvince();
        if (adrProvince != null) {
            stmt.bindString(6, adrProvince);
        }
 
        String appUserId = entity.getAppUserId();
        if (appUserId != null) {
            stmt.bindString(7, appUserId);
        }
 
        String appUserIntroduce = entity.getAppUserIntroduce();
        if (appUserIntroduce != null) {
            stmt.bindString(8, appUserIntroduce);
        }
 
        String appUserName = entity.getAppUserName();
        if (appUserName != null) {
            stmt.bindString(9, appUserName);
        }
 
        String appUserPic = entity.getAppUserPic();
        if (appUserPic != null) {
            stmt.bindString(10, appUserPic);
        }
 
        String appUserSex = entity.getAppUserSex();
        if (appUserSex != null) {
            stmt.bindString(11, appUserSex);
        }
 
        String categoryName = entity.getCategoryName();
        if (categoryName != null) {
            stmt.bindString(12, categoryName);
        }
 
        String companyAddress = entity.getCompanyAddress();
        if (companyAddress != null) {
            stmt.bindString(13, companyAddress);
        }
 
        String companyGif = entity.getCompanyGif();
        if (companyGif != null) {
            stmt.bindString(14, companyGif);
        }
 
        String companyId = entity.getCompanyId();
        if (companyId != null) {
            stmt.bindString(15, companyId);
        }
        stmt.bindLong(16, entity.getCompanyIndustryFType());
        stmt.bindLong(17, entity.getCompanyIndustrySType());
 
        String companyIntroduce = entity.getCompanyIntroduce();
        if (companyIntroduce != null) {
            stmt.bindString(18, companyIntroduce);
        }
 
        String companyLogo = entity.getCompanyLogo();
        if (companyLogo != null) {
            stmt.bindString(19, companyLogo);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(20, companyName);
        }
 
        String companyPosition = entity.getCompanyPosition();
        if (companyPosition != null) {
            stmt.bindString(21, companyPosition);
        }
 
        String companyVideo = entity.getCompanyVideo();
        if (companyVideo != null) {
            stmt.bindString(22, companyVideo);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(23, email);
        }
 
        String followIndustry = entity.getFollowIndustry();
        if (followIndustry != null) {
            stmt.bindString(24, followIndustry);
        }
 
        String needIndustry = entity.getNeedIndustry();
        if (needIndustry != null) {
            stmt.bindString(25, needIndustry);
        }
 
        String offerIndustry = entity.getOfferIndustry();
        if (offerIndustry != null) {
            stmt.bindString(26, offerIndustry);
        }
 
        String only_dk = entity.getOnly_dk();
        if (only_dk != null) {
            stmt.bindString(27, only_dk);
        }
 
        String stypeName = entity.getStypeName();
        if (stypeName != null) {
            stmt.bindString(28, stypeName);
        }
 
        String userFreeType = entity.getUserFreeType();
        if (userFreeType != null) {
            stmt.bindString(29, userFreeType);
        }
 
        String userType = entity.getUserType();
        if (userType != null) {
            stmt.bindString(30, userType);
        }
 
        String yunToken = entity.getYunToken();
        if (yunToken != null) {
            stmt.bindString(31, yunToken);
        }
 
        String companyPicList = entity.getCompanyPicList();
        if (companyPicList != null) {
            stmt.bindString(32, companyPicList);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String loginType = entity.getLoginType();
        if (loginType != null) {
            stmt.bindString(2, loginType);
        }
 
        String add_frid_set = entity.getAdd_frid_set();
        if (add_frid_set != null) {
            stmt.bindString(3, add_frid_set);
        }
 
        String adrCity = entity.getAdrCity();
        if (adrCity != null) {
            stmt.bindString(4, adrCity);
        }
 
        String adrCounty = entity.getAdrCounty();
        if (adrCounty != null) {
            stmt.bindString(5, adrCounty);
        }
 
        String adrProvince = entity.getAdrProvince();
        if (adrProvince != null) {
            stmt.bindString(6, adrProvince);
        }
 
        String appUserId = entity.getAppUserId();
        if (appUserId != null) {
            stmt.bindString(7, appUserId);
        }
 
        String appUserIntroduce = entity.getAppUserIntroduce();
        if (appUserIntroduce != null) {
            stmt.bindString(8, appUserIntroduce);
        }
 
        String appUserName = entity.getAppUserName();
        if (appUserName != null) {
            stmt.bindString(9, appUserName);
        }
 
        String appUserPic = entity.getAppUserPic();
        if (appUserPic != null) {
            stmt.bindString(10, appUserPic);
        }
 
        String appUserSex = entity.getAppUserSex();
        if (appUserSex != null) {
            stmt.bindString(11, appUserSex);
        }
 
        String categoryName = entity.getCategoryName();
        if (categoryName != null) {
            stmt.bindString(12, categoryName);
        }
 
        String companyAddress = entity.getCompanyAddress();
        if (companyAddress != null) {
            stmt.bindString(13, companyAddress);
        }
 
        String companyGif = entity.getCompanyGif();
        if (companyGif != null) {
            stmt.bindString(14, companyGif);
        }
 
        String companyId = entity.getCompanyId();
        if (companyId != null) {
            stmt.bindString(15, companyId);
        }
        stmt.bindLong(16, entity.getCompanyIndustryFType());
        stmt.bindLong(17, entity.getCompanyIndustrySType());
 
        String companyIntroduce = entity.getCompanyIntroduce();
        if (companyIntroduce != null) {
            stmt.bindString(18, companyIntroduce);
        }
 
        String companyLogo = entity.getCompanyLogo();
        if (companyLogo != null) {
            stmt.bindString(19, companyLogo);
        }
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(20, companyName);
        }
 
        String companyPosition = entity.getCompanyPosition();
        if (companyPosition != null) {
            stmt.bindString(21, companyPosition);
        }
 
        String companyVideo = entity.getCompanyVideo();
        if (companyVideo != null) {
            stmt.bindString(22, companyVideo);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(23, email);
        }
 
        String followIndustry = entity.getFollowIndustry();
        if (followIndustry != null) {
            stmt.bindString(24, followIndustry);
        }
 
        String needIndustry = entity.getNeedIndustry();
        if (needIndustry != null) {
            stmt.bindString(25, needIndustry);
        }
 
        String offerIndustry = entity.getOfferIndustry();
        if (offerIndustry != null) {
            stmt.bindString(26, offerIndustry);
        }
 
        String only_dk = entity.getOnly_dk();
        if (only_dk != null) {
            stmt.bindString(27, only_dk);
        }
 
        String stypeName = entity.getStypeName();
        if (stypeName != null) {
            stmt.bindString(28, stypeName);
        }
 
        String userFreeType = entity.getUserFreeType();
        if (userFreeType != null) {
            stmt.bindString(29, userFreeType);
        }
 
        String userType = entity.getUserType();
        if (userType != null) {
            stmt.bindString(30, userType);
        }
 
        String yunToken = entity.getYunToken();
        if (yunToken != null) {
            stmt.bindString(31, yunToken);
        }
 
        String companyPicList = entity.getCompanyPicList();
        if (companyPicList != null) {
            stmt.bindString(32, companyPicList);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserBean readEntity(Cursor cursor, int offset) {
        UserBean entity = new UserBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // loginType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // add_frid_set
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // adrCity
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // adrCounty
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // adrProvince
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // appUserId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // appUserIntroduce
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // appUserName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // appUserPic
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // appUserSex
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // categoryName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // companyAddress
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // companyGif
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // companyId
            cursor.getInt(offset + 15), // companyIndustryFType
            cursor.getInt(offset + 16), // companyIndustrySType
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // companyIntroduce
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // companyLogo
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // companyName
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // companyPosition
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // companyVideo
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // email
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // followIndustry
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // needIndustry
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // offerIndustry
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // only_dk
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // stypeName
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // userFreeType
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // userType
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // yunToken
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31) // companyPicList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLoginType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAdd_frid_set(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAdrCity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAdrCounty(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAdrProvince(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAppUserId(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAppUserIntroduce(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAppUserName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setAppUserPic(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAppUserSex(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCategoryName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCompanyAddress(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCompanyGif(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setCompanyId(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCompanyIndustryFType(cursor.getInt(offset + 15));
        entity.setCompanyIndustrySType(cursor.getInt(offset + 16));
        entity.setCompanyIntroduce(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setCompanyLogo(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCompanyName(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCompanyPosition(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCompanyVideo(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setEmail(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setFollowIndustry(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setNeedIndustry(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setOfferIndustry(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setOnly_dk(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setStypeName(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setUserFreeType(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setUserType(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setYunToken(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setCompanyPicList(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
