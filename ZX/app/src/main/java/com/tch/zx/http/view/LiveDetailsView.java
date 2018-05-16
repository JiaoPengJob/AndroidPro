package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.LiveDetailsResultBean;

/**
 * 直播详情
 */

public interface LiveDetailsView extends View {

    void onSuccess(LiveDetailsResultBean liveDetailsResultBean);

    void onError(String result);

}
