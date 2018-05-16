package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SpecialSubscribeResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialSubscribeView extends View {

    void onSuccess(SpecialSubscribeResultBean specialSubscribeResultBean);

    void onError(String result);

}
