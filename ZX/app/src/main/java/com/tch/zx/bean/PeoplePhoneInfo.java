package com.tch.zx.bean;

import java.util.List;

/**
 * 获取到的联系人手机号
 */

public class PeoplePhoneInfo {

    private String name;

    private List<String> phone;

    public PeoplePhoneInfo() {
    }

    public PeoplePhoneInfo(String name, List<String> phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
}
