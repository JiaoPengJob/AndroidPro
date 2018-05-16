package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SearchInfoResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SearchInfoView extends View {

    void onSuccess(SearchInfoResultBean searchInfoResultBean);

    void onError(String result);

}
