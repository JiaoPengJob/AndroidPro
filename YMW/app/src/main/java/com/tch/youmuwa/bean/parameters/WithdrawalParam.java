package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/16.
 */

public class WithdrawalParam {
    private String money;
    private String password;

    public WithdrawalParam() {
    }

    public WithdrawalParam(String money, String password) {
        this.money = money;
        this.password = password;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
