package com.tch.zx.http.view;

import com.tch.zx.bean.BaseResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface BaseView<T> extends View {

    void onSuccess(BaseResultBean<T> retResultBean);

    void onError(String result);

}
