package com.tch.zx.http.view;

/**
 * Created by peng on 2017/7/10.
 */

public interface FIndustryListView extends View {

    void onSuccess(FIndustryListResultBean fIndustryListResultBean);

    void onError(String result);

}
