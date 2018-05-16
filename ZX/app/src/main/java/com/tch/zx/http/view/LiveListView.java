package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.LiveListResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface LiveListView extends View {

    void onSuccess(LiveListResultBean liveListResultBean);

    void onError(String result);

}
