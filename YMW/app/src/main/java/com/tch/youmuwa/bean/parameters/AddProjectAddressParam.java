package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/10.
 */

public class AddProjectAddressParam {

    private String addr_province;
    private String addr_city;
    private String addr_area;
    private String addr_detail;

    public AddProjectAddressParam() {
    }

    public AddProjectAddressParam(String addr_province, String addr_city, String addr_area, String addr_detail) {
        this.addr_province = addr_province;
        this.addr_city = addr_city;
        this.addr_area = addr_area;
        this.addr_detail = addr_detail;
    }

    public String getAddr_province() {
        return addr_province;
    }

    public void setAddr_province(String addr_province) {
        this.addr_province = addr_province;
    }

    public String getAddr_city() {
        return addr_city;
    }

    public void setAddr_city(String addr_city) {
        this.addr_city = addr_city;
    }

    public String getAddr_area() {
        return addr_area;
    }

    public void setAddr_area(String addr_area) {
        this.addr_area = addr_area;
    }

    public String getAddr_detail() {
        return addr_detail;
    }

    public void setAddr_detail(String addr_detail) {
        this.addr_detail = addr_detail;
    }
}
