package com.tch.youmuwa.bean.result;

import java.io.Serializable;

/**
 * Created by peng on 2017/8/17.
 */

public class IsBindBankResult implements Serializable{


    /**
     * bankcard : 1
     * bankcard_id : 8958********9080
     */

    private int bankcard;
    private String bankcard_id = "";

    public int getBankcard() {
        return bankcard;
    }

    public void setBankcard(int bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankcard_id() {
        return bankcard_id;
    }

    public void setBankcard_id(String bankcard_id) {
        this.bankcard_id = bankcard_id;
    }
}
