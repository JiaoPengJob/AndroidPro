package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.LoginResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface LoginView extends View {

    void onSuccess(LoginResultBean baseResultBean);

    void onError(String result);

}
