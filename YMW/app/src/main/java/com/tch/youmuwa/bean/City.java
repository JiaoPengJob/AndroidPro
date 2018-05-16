package com.tch.youmuwa.bean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

/**
 * 城市
 */

public class City implements Comparable<City> {

    private String name;
    private String pinyin;

    public City(Context context, String name) {
        this.name = name;
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(context)));
        if (name.equals("厦门")) {
            pinyin = "XIAMEN";
        } else if (name.equals("长春")) {
            pinyin = "CHANGCHUN";
        } else if (name.equals("长沙")) {
            pinyin = "CHANGSHA";
        } else if (name.equals("单县")) {
            pinyin = "SHANXIAN";
        } else if (name.equals("亳州")) {
            pinyin = "BOZHOU";
        } else if (name.equals("和县")) {
            pinyin = "HEXIAN";
        } else if (name.equals("霍邱")) {
            pinyin = "HUOQIU";
        } else if (name.equals("桐城")) {
            pinyin = "TONGCHENG";
        } else if (name.equals("天长")) {
            pinyin = "TIANCHANG";
        } else if (name.equals("六安")) {
            pinyin = "LUAN";
        } else if (name.equals("朝阳")) {
            pinyin = "CHAOYANG";
        } else {
            pinyin = Pinyin.toPinyin(name, "");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(@NonNull City city) {
        String name = city.getName();
        String pinyin = city.getPinyin();
        if (TextUtils.isEmpty(pinyin)) {
            return name.compareToIgnoreCase(TextUtils.isEmpty(pinyin) ? name : pinyin);
        } else {
            return pinyin.compareToIgnoreCase(TextUtils.isEmpty(pinyin) ? name : pinyin);
        }
    }
}
