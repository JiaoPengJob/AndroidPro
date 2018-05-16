package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by peng on 2017/9/27.
 */
@Entity
public class TabBar {
    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

    private int typeId;
    private String TypeStr;

    @Generated(hash = 1777825057)
    public TabBar(Long id, int typeId, String TypeStr) {
        this.id = id;
        this.typeId = typeId;
        this.TypeStr = TypeStr;
    }

    @Generated(hash = 449610825)
    public TabBar() {
    }

    public TabBar(int typeId, String typeStr) {
        this.typeId = typeId;
        TypeStr = typeStr;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeStr() {
        return this.TypeStr;
    }

    public void setTypeStr(String TypeStr) {
        this.TypeStr = TypeStr;
    }

}
