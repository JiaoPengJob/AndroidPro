package com.tch.youmuwa.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 工人搜索历史
 */
@Entity
public class WorkerSearchHistory {

    @Id(autoincrement = true)
    private Long id;

    private String workerSearchHistory;

    @Generated(hash = 1808507435)
    public WorkerSearchHistory(Long id, String workerSearchHistory) {
        this.id = id;
        this.workerSearchHistory = workerSearchHistory;
    }

    public WorkerSearchHistory(String workerSearchHistory) {
        this.workerSearchHistory = workerSearchHistory;
    }

    @Generated(hash = 1120527914)
    public WorkerSearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkerSearchHistory() {
        return this.workerSearchHistory;
    }

    public void setWorkerSearchHistory(String workerSearchHistory) {
        this.workerSearchHistory = workerSearchHistory;
    }
}
