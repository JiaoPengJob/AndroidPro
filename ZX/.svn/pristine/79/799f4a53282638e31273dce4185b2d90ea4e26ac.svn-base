package com.tch.zx.dao.green;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 搜索历史记录信息类
 */
@Entity
public class SearchHistoryBean {

    //表ID，是每一条记录的唯一标识,自增
    @Id(autoincrement = true)
    private Long id;

    //搜索历史
    @NotNull
    private String searchHistory;

    @Generated(hash = 2068788410)
    public SearchHistoryBean(Long id, @NotNull String searchHistory) {
        this.id = id;
        this.searchHistory = searchHistory;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchHistory() {
        return this.searchHistory;
    }

    public void setSearchHistory(String searchHistory) {
        this.searchHistory = searchHistory;
    }

}
