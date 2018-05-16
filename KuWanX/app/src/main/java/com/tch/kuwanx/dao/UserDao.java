package com.tch.kuwanx.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Phone = new Property(1, String.class, "phone", false, "PHONE");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
        public final static Property User_idcard = new Property(4, String.class, "user_idcard", false, "USER_IDCARD");
        public final static Property User_realname = new Property(5, String.class, "user_realname", false, "USER_REALNAME");
        public final static Property Paypassword = new Property(6, String.class, "paypassword", false, "PAYPASSWORD");
        public final static Property Accountnum = new Property(7, String.class, "accountnum", false, "ACCOUNTNUM");
        public final static Property YunToken = new Property(8, String.class, "yunToken", false, "YUN_TOKEN");
        public final static Property Headpic = new Property(9, String.class, "headpic", false, "HEADPIC");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PHONE\" TEXT," + // 1: phone
                "\"USER_ID\" TEXT," + // 2: userId
                "\"NICKNAME\" TEXT," + // 3: nickname
                "\"USER_IDCARD\" TEXT," + // 4: user_idcard
                "\"USER_REALNAME\" TEXT," + // 5: user_realname
                "\"PAYPASSWORD\" TEXT," + // 6: paypassword
                "\"ACCOUNTNUM\" TEXT," + // 7: accountnum
                "\"YUN_TOKEN\" TEXT," + // 8: yunToken
                "\"HEADPIC\" TEXT);"); // 9: headpic
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(2, phone);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
 
        String user_idcard = entity.getUser_idcard();
        if (user_idcard != null) {
            stmt.bindString(5, user_idcard);
        }
 
        String user_realname = entity.getUser_realname();
        if (user_realname != null) {
            stmt.bindString(6, user_realname);
        }
 
        String paypassword = entity.getPaypassword();
        if (paypassword != null) {
            stmt.bindString(7, paypassword);
        }
 
        String accountnum = entity.getAccountnum();
        if (accountnum != null) {
            stmt.bindString(8, accountnum);
        }
 
        String yunToken = entity.getYunToken();
        if (yunToken != null) {
            stmt.bindString(9, yunToken);
        }
 
        String headpic = entity.getHeadpic();
        if (headpic != null) {
            stmt.bindString(10, headpic);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(2, phone);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(3, userId);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
 
        String user_idcard = entity.getUser_idcard();
        if (user_idcard != null) {
            stmt.bindString(5, user_idcard);
        }
 
        String user_realname = entity.getUser_realname();
        if (user_realname != null) {
            stmt.bindString(6, user_realname);
        }
 
        String paypassword = entity.getPaypassword();
        if (paypassword != null) {
            stmt.bindString(7, paypassword);
        }
 
        String accountnum = entity.getAccountnum();
        if (accountnum != null) {
            stmt.bindString(8, accountnum);
        }
 
        String yunToken = entity.getYunToken();
        if (yunToken != null) {
            stmt.bindString(9, yunToken);
        }
 
        String headpic = entity.getHeadpic();
        if (headpic != null) {
            stmt.bindString(10, headpic);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // phone
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // nickname
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // user_idcard
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // user_realname
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // paypassword
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // accountnum
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // yunToken
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // headpic
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPhone(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUser_idcard(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUser_realname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPaypassword(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAccountnum(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setYunToken(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setHeadpic(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
