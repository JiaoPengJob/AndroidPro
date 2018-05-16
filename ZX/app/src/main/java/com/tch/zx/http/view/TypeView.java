package com.tch.zx.http.view;

import com.tch.zx.bean.TypeBean;
import com.tch.zx.http.bean.result.TypeResultBean;

import java.util.List;

/**
 * Created by peng on 2017/7/10.
 */

public interface TypeView extends View {

    void onSuccess(TypeResultBean baseResultBean);

    void onError(String result);

}
