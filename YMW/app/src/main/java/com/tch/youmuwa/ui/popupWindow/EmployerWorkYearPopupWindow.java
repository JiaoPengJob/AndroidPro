package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tch.youmuwa.R;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.util.HelperUtil;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 工龄选择
 */

public class EmployerWorkYearPopupWindow extends PopupWindow {

    private Context context;
    private View view;
    private WheelListView wlvStartTime;
    private WheelListView wlvEndTime;
    private List<String> years, nYears;
    private Button btYearCancel, btYearDetermine;
    private PopupInterface pi;
    private String yearStr = "2016", nYearStr = "2017";

    public EmployerWorkYearPopupWindow(Context context, PopupInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_employer_worker_year, null);
        initView();

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFF000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_popup);
    }

    private void initView() {
        wlvStartTime = (WheelListView) view.findViewById(R.id.wlvStartTime);
        wlvEndTime = (WheelListView) view.findViewById(R.id.wlvEndTime);
        btYearDetermine = (Button) view.findViewById(R.id.btYearDetermine);
        btYearCancel = (Button) view.findViewById(R.id.btYearCancel);

        int year = Integer.parseInt(HelperUtil.getCurrentTime("yyyy"));
        nYearStr = String.valueOf(year);
        yearStr = String.valueOf(year - 1);

        years = new ArrayList<String>();
        nYears = new ArrayList<String>();

        for (int i = 1980; i < year + 1; i++) {
            years.add(String.valueOf(i));
            nYears.add(String.valueOf(i));
        }

        LineConfig config = new LineConfig();
        config.setColor(Color.parseColor("#000000"));//线颜色
        config.setAlpha(50);//线透明度
        config.setRatio(1f);//线比率
        config.setThick(ConvertUtils.toPx(context, 1));//线粗
        config.setShadowVisible(false);

        wlvStartTime.setItems(years, year - 1 - 1980);
        wlvStartTime.setSelectedTextColor(Color.parseColor("#000000"));
        wlvStartTime.setLineConfig(config);
        wlvStartTime.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(boolean isUserScroll, int index, String item) {
                yearStr = item;
            }
        });

        wlvEndTime.setItems(nYears, year - 1980);
        wlvEndTime.setSelectedTextColor(Color.parseColor("#000000"));
        wlvEndTime.setLineConfig(config);
        wlvEndTime.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(boolean isUserScroll, int index, String item) {
                nYearStr = item;
            }
        });

        btYearCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btYearDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = Integer.parseInt(nYearStr) - Integer.parseInt(yearStr);
                if (result <= 0) {
                    pi.getResult(1, "0");
                } else {
                    pi.getResult(1, String.valueOf(result));
                }
                dismiss();
            }
        });
    }
}
