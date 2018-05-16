package com.tch.zx.http.view;

import com.tch.zx.bean.BaseResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface FriendListByAppUserIdView extends View {

    void onSuccess(BaseResultBean<Object> result);

    void onError(String result);

}
