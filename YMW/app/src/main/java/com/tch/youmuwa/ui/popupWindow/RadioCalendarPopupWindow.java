package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.othershe.calendarview.CalendarView;
import com.othershe.calendarview.DateBean;
import com.othershe.calendarview.WeekView;
import com.othershe.calendarview.listener.OnMonthItemClickListener;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.myinterface.CPopupInterface;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.myinterface.PopupInterface;
import com.tch.youmuwa.ui.view.CustomCalendarView;
import com.tch.youmuwa.util.HelperUtil;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日历/单选
 */

public class RadioCalendarPopupWindow extends PopupWindow {

    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.weekView)
    WeekView weekView;
    @BindView(R.id.tvDateShow)
    TextView tvDateShow;
    @BindView(R.id.ibLeft)
    ImageButton ibLeft;
    @BindView(R.id.llDialogParent)
    LinearLayout llDialogParent;

    private Context context;
    private View view;
    private CPopupInterface pi;
    private int year = 0, month = 0, day = 0;

    public RadioCalendarPopupWindow(Context context, CPopupInterface pi, int year, int month, int day) {
        this.context = context;
        this.pi = pi;
        this.year = year;
        this.month = month;
        this.day = day;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_radio_calendar, null);
        ButterKnife.bind(this, view);
        initView();

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = view.getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
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

        this.setAnimationStyle(R.style.AnimTop);
    }

    public View rootView;

    /**
     * 初始化
     */
    private void initView() {
        calendar.init();
        weekView.setBackgroundColor(Color.parseColor("#31D09A"));

        tvDateShow.setText(calendar.getDateInit().getSolar()[0] + "-" + calendar.getDateInit().getSolar()[1] + "-" + calendar.getDateInit().getSolar()[2]);

        calendar.setOnItemClickListener(new OnMonthItemClickListener() {
            @Override
            public void onMonthItemClick(View view, DateBean dateBean) {
                view.setBackgroundResource(R.drawable.oval_day_select);
                tvDateShow.setText(dateBean.getSolar()[0] + "-" + dateBean.getSolar()[1] + "-" + dateBean.getSolar()[2]);
                pi.getResult(dateBean.getSolar()[0], dateBean.getSolar()[1], dateBean.getSolar()[2]);
                rootView = view.getRootView();
                rootView.setVisibility(View.GONE);
            }
        });

        calendar.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                tvDateShow.setText(date[0] + "-" + date[1] + "-" + date[2]);
            }
        });

        handler.sendEmptyMessage(0);

    }

    public View getRootView() {
        return rootView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            calendar.toSpecifyDate(year,
                    month,
                    day);
        }
    };

    @OnClick(R.id.ibLeft)
    public void lastMonth(View view) {
        if (calendar != null) {
            calendar.lastMonth();
        }
    }

    @OnClick(R.id.ibRight)
    public void nextMonth(View view) {
        if (calendar != null) {
            calendar.nextMonth();
        }
    }

}
