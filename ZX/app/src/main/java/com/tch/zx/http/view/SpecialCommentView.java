package com.tch.zx.http.view;

import com.tch.zx.http.bean.result.SpecialCommentResultBean;

/**
 * Created by peng on 2017/7/10.
 */

public interface SpecialCommentView extends View {

    void onSuccess(SpecialCommentResultBean specialCommentResultBean);

    void onError(String result);

}
