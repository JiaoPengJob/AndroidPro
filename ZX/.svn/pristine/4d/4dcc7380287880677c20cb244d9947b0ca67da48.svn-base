/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tch.zx.wheelview.adapter;

import android.content.Context;

import com.tch.zx.wheelview.base.BaseWheelAdapter;

import java.util.List;

/**
 * 设置性别的适配器
 */
public class SexAdapter extends BaseWheelAdapter<String> {

    private List<String> sex;

    public SexAdapter(Context context, List<String> sex) {
        super(context, sex);
        this.sex = sex;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return sex.get(index);
    }
}
