package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/9/1.
 */

public class BindBankCardParam {

    private String bankcard;
    private String mobile;
    private String bankcode;

    public BindBankCardParam() {
    }

    public BindBankCardParam(String bankcard, String mobile, String bankcode) {
        this.bankcard = bankcard;
        this.mobile = mobile;
        this.bankcode = bankcode;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }
}
