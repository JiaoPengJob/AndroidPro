package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.ui.view.CustomCalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日历
 */

public class CalendarPopupWindow extends PopupWindow {

    @BindView(R.id.llCalendarParent)
    LinearLayout llCalendarParent;
    @BindView(R.id.llMakeAppointment)
    LinearLayout llMakeAppointment;

    private Context context;
    private View view;
    private CalenddarPopupInterface pi;
    private List<String> initeData;
    private CustomCalendarView calendarview;
    private List<DateItem> list;
    private boolean isWorkInfo = false;
    private boolean b;

    public CalendarPopupWindow(Context context, CalenddarPopupInterface pi, boolean isWorkInfo, List<DateItem> list,boolean b) {
        this.context = context;
        this.pi = pi;
        this.isWorkInfo = isWorkInfo;
        this.list = list;
        this.b = b;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_calendar, null);
        ButterKnife.bind(this, view);
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
        // 设置弹出窗体显示时的动画，从顶部向上弹出
        this.setAnimationStyle(R.style.AnimTop);
    }

    /**
     * 初始化
     */
    private void initView() {
        if (isWorkInfo) {
            llMakeAppointment.setVisibility(View.GONE);
        } else {
            llMakeAppointment.setVisibility(View.VISIBLE);
        }
        initeData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String data = "2000-01-" + (27 + i);
            initeData.add(data);
        }
        for (int i = 0; i < 4; i++) {
            String data = "2027-12-" + (27 + i);
            initeData.add(data);
        }
//        //设置已选日期
//        list = new ArrayList<DateItem>();
//        list.add(new DateItem(2017, 8, 20));
//        list.add(new DateItem(2017, 8, 10));
//        list.add(new DateItem(2017, 8, 16));

        calendarview = new CustomCalendarView(context, list,b);
        llCalendarParent.addView(calendarview);

    }

    /**
     * 立即预约
     */
    @OnClick(R.id.btMakeAppointment)
    public void makeAppointment() {
        if (calendarview.getSelectdateList() != null &&
                calendarview.getSelectdateList().size() > 0) {
            pi.getResult(calendarview.getSelectdateList());
        }
        dismiss();
    }
}
