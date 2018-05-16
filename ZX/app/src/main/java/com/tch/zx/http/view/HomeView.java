package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.HomeResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface HomeView extends View {

    void onSuccess(HomeResultBean baseResultBean);

    void onError(String result);

}
