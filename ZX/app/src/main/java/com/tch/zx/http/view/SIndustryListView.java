package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SIndustryListResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SIndustryListView extends View {

    void onSuccess(SIndustryListResultBean sIndustryListResultBean);

    void onError(String result);

}
