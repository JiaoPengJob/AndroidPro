package com.tch.youmuwa.bean.result;

/**
 * Created by peng on 2017/8/30.
 */

public class CityPoiResult {


    /**
     * latitude : 39.9299857781
     * longitude : 116.395645038
     * province : 北京
     * city : 北京市
     */

    private String latitude = "";
    private String longitude = "";
    private String province = "";
    private String city = "";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
