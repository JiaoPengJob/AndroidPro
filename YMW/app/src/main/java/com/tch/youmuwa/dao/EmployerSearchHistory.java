package com.tch.youmuwa.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 雇主搜索历史
 */
@Entity
public class EmployerSearchHistory {

    @Id(autoincrement = true)
    private Long id;

    private String employerSearchHistory;

    @Generated(hash = 2089097396)
    public EmployerSearchHistory(Long id, String employerSearchHistory) {
        this.id = id;
        this.employerSearchHistory = employerSearchHistory;
    }

    public EmployerSearchHistory(String employerSearchHistory) {
        this.employerSearchHistory = employerSearchHistory;
    }

    @Generated(hash = 617310010)
    public EmployerSearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployerSearchHistory() {
        return this.employerSearchHistory;
    }

    public void setEmployerSearchHistory(String employerSearchHistory) {
        this.employerSearchHistory = employerSearchHistory;
    }

}
