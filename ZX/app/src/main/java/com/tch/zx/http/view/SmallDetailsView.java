package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SmallDetailsView extends View {

    void onSuccess(SmallDetailsResultBean smallDetailsResultBean);

    void onError(String result);

}
