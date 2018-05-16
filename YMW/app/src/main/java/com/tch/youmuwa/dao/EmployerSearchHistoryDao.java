package com.tch.youmuwa.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EMPLOYER_SEARCH_HISTORY".
*/
public class EmployerSearchHistoryDao extends AbstractDao<EmployerSearchHistory, Long> {

    public static final String TABLENAME = "EMPLOYER_SEARCH_HISTORY";

    /**
     * Properties of entity EmployerSearchHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property EmployerSearchHistory = new Property(1, String.class, "employerSearchHistory", false, "EMPLOYER_SEARCH_HISTORY");
    }


    public EmployerSearchHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public EmployerSearchHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EMPLOYER_SEARCH_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EMPLOYER_SEARCH_HISTORY\" TEXT);"); // 1: employerSearchHistory
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EMPLOYER_SEARCH_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EmployerSearchHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String employerSearchHistory = entity.getEmployerSearchHistory();
        if (employerSearchHistory != null) {
            stmt.bindString(2, employerSearchHistory);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EmployerSearchHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String employerSearchHistory = entity.getEmployerSearchHistory();
        if (employerSearchHistory != null) {
            stmt.bindString(2, employerSearchHistory);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public EmployerSearchHistory readEntity(Cursor cursor, int offset) {
        EmployerSearchHistory entity = new EmployerSearchHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // employerSearchHistory
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EmployerSearchHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmployerSearchHistory(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(EmployerSearchHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(EmployerSearchHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EmployerSearchHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
