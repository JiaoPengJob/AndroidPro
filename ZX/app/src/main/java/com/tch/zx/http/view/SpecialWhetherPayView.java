package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialWhetherPayView extends View {

    void onSuccess(SpecialWhetherPayResultBean whetherPayResultBean);

    void onError(String result);

}
