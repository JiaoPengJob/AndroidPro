package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SpecialDetailResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialDetailView extends View {

    void onSuccess(SpecialDetailResultBean specialDetailResultBean);

    void onError(String result);

}
