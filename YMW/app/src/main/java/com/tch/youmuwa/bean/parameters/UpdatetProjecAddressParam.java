package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/14.
 */

public class UpdatetProjecAddressParam {

    private int address_id;
    private String addr_province;
    private String addr_city;
    private String addr_area;
    private String addr_detail;

    public UpdatetProjecAddressParam() {
    }

    public UpdatetProjecAddressParam(int address_id, String addr_province, String addr_city, String addr_area, String addr_detail) {
        this.address_id = address_id;
        this.addr_province = addr_province;
        this.addr_city = addr_city;
        this.addr_area = addr_area;
        this.addr_detail = addr_detail;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
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
