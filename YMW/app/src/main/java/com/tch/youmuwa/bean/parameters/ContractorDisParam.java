package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/28.
 */

public class ContractorDisParam {

    private int id;
    private String wages;

    public ContractorDisParam() {
    }

    public ContractorDisParam(int id, String wages) {
        this.id = id;
        this.wages = wages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }
}
