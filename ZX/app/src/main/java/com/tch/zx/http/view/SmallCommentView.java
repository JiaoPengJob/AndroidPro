package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SmallCommentResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SmallCommentView extends View {

    void onSuccess(SmallCommentResultBean smallCommentResultBean);

    void onError(String result);

}
