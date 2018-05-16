package com.tch.zx.http.view;

import com.tch.zx.bean.SmallListBean;
import com.tch.zx.http.bean.result.SpecialListResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialListView extends View {

    void onSuccess(SpecialListResultBean specialListResultBean);

    void onError(String result);

}
