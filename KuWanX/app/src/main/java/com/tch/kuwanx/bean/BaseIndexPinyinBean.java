package com.tch.kuwanx.bean;


import com.tch.kuwanx.interfaces.IIndexTargetInterface;

/**
 * 索引类的汉语拼音的接口
 */

public abstract class BaseIndexPinyinBean extends BaseIndexTagBean implements IIndexTargetInterface {

    private String pyIndex;

    public String getPyIndex() {
        return pyIndex;
    }

    public void setPyIndex(String pyIndex) {
        this.pyIndex = pyIndex;
    }
}
