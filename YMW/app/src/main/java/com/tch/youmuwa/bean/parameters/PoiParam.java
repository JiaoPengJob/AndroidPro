package com.tch.youmuwa.bean.parameters;

/**
 * Created by peng on 2017/8/28.
 */

public class PoiParam {

    private String longitude;
    private String latitude;

    public PoiParam() {
    }

    public PoiParam(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
