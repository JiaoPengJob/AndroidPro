package com.tch.kuwanx.bean;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tch.kuwanx.R;

/**
 * Created by peng on 2017/10/25.
 * 主页标题栏管理类
 */

public class TabItem {

    private Context context;
    private int icon;
    private String tab;
    private String tabSpec;
    private Class<? extends Fragment> fragmentClass;
    private TextView tvTabItemMsg;

    public TabItem(Context context, int icon, String tab, String tabSpec,
                   Class<? extends Fragment>
                           fragmentClass) {
        this.context = context;
        this.icon = icon;
        this.tab = tab;
        this.tabSpec = tabSpec;
        this.fragmentClass = fragmentClass;
    }

    public TabItem(Context context, String tab, String tabSpec,
                   Class<? extends Fragment>
                           fragmentClass) {
        this.context = context;
        this.tab = tab;
        this.tabSpec = tabSpec;
        this.fragmentClass = fragmentClass;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public String getTabText() {
        return tab;
    }

    public String getTabSpec() {
        return tabSpec;
    }

    public View getTabView() {
        View view;
        if (tabSpec.equals("center")) {
            view = getCenterView();
        } else {
            view = View.inflate(context, R.layout.tab_item, null);
            ImageView ivTabItemPhoto = (ImageView) view.findViewById(R.id.ivTabItemPhoto);
            TextView tvTabItemTitle = (TextView) view.findViewById(R.id.tvTabItemTitle);
            tvTabItemMsg = (TextView) view.findViewById(R.id.tvTabItemMsg);
            ivTabItemPhoto.setImageResource(icon);
            tvTabItemTitle.setText(tab);
        }
        return view;
    }

    private View getCenterView() {
        View view = View.inflate(context, R.layout.tab_center_item, null);
        return view;
    }

    public void setNewMsgCount(int count) {
        if (count > 0) {
            tvTabItemMsg.setVisibility(View.VISIBLE);
            tvTabItemMsg.setText(String.valueOf(count));
        } else {
            tvTabItemMsg.setVisibility(View.INVISIBLE);
        }
    }

}
