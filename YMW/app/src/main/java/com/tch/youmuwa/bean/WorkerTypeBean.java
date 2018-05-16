package com.tch.youmuwa.bean;

/**
 * Created by peng on 2017/8/10.
 */

public class WorkerTypeBean {

    private int id;
    private String typeName;
    private boolean isSelected;

    public WorkerTypeBean() {
    }

    public WorkerTypeBean(int id, String typeName, boolean isSelected) {
        this.id = id;
        this.typeName = typeName;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
