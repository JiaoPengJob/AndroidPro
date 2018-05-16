package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.tch.youmuwa.R;
import com.tch.youmuwa.myinterface.CalenddarPopupInterface;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.MySelectorDecorator;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日历
 */

public class CalendarWindow extends PopupWindow {

    @BindView(R.id.mcv)
    MaterialCalendarView mcv;
    @BindView(R.id.btDateSelect)
    Button btDateSelect;

    private Context context;
    private View view;
    private CalenddarPopupInterface pi;
    private List<String> dateList = new ArrayList<String>();
    private Time time;
    private List<Boolean> isSel;

    public CalendarWindow(Context context, CalenddarPopupInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_calendar_next, null);
        ButterKnife.bind(this, view);
        time = new Time("GMT+8");
        time.setToNow();
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
        // 设置弹出窗体显示时的动画，从顶部向下弹出
        this.setAnimationStyle(R.style.AnimTop);
    }

    /**
     * 初始化
     */
    private void initView() {

//        mcv.setLeftArrowMask(context.getResources().getDrawable(R.mipmap.calendar_left));
//        mcv.setRightArrowMask(context.getResources().getDrawable(R.mipmap.calendar_right));
        mcv.setWeekDayFormatter(new ArrayWeekDayFormatter(context.getResources().getTextArray(R.array.custom_weekdays)));

        mcv.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return day.getYear() + "-" + (day.getMonth() + 1) + "-" + new Date().getDate();
            }
        });

        mcv.addDecorator(new MySelectorDecorator(context));

        mcv.getChildAt(0).setBackgroundColor(context.getResources().getColor(R.color.green_31d09a));
        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);

        mcv.setWeekDayTextAppearance(R.style.CalendarWeekDayStyle);
        mcv.setHeaderTextAppearance(R.style.CalendarTitleStyle);

        mcv.setAllowClickDaysOutsideCurrentMonth(true);

        // 设置日历默认的时间为当前的时间
//        mcv.setSelectedDate(new Date());

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2000, 1, 1))
                .setMaximumDate(CalendarDay.from(2020, 12, 12))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        mcv.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                dateList.clear();
                if (dates.size() == 1) {
                    String month = String.valueOf(dates.get(0).getMonth() + 1);
                    if (month.length() < 2) {
                        month = "0" + month;
                    }
                    String day = String.valueOf(dates.get(0).getDay());
                    if (day.length() < 2) {
                        day = "0" + day;
                    }
                    dateList.add(dates.get(0).getYear() + "-" + month + "-" + day);
                } else {
                    for (CalendarDay cd : dates) {
                        if (cd.getYear() > time.year) {
                            mcv.setDateSelected(cd, true);
                        } else if (cd.getYear() == time.year) {
                            if (cd.getMonth() > time.month) {
                                mcv.setDateSelected(cd, true);
                            } else if (cd.getMonth() == time.month) {
                                if (cd.getDay() >= time.monthDay) {
                                    mcv.setDateSelected(cd, true);
                                } else {
                                    mcv.setDateSelected(cd, false);
                                }
                            } else {
                                mcv.setDateSelected(cd, false);
                            }
                        } else {
                            mcv.setDateSelected(cd, false);
                        }
                        String month = String.valueOf(cd.getMonth() + 1);
                        if (month.length() < 2) {
                            month = "0" + month;
                        }
                        String day = String.valueOf(cd.getDay());
                        if (day.length() < 2) {
                            day = "0" + day;
                        }
                        dateList.add(cd.getYear() + "-" + month + "-" + day);
                    }
                }
            }
        });

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateList.clear();
                String month = String.valueOf(date.getMonth() + 1);
                if (month.length() < 2) {
                    month = "0" + month;
                }
                String day = String.valueOf(date.getDay());
                if (day.length() < 2) {
                    day = "0" + day;
                }
                dateList.add(date.getYear() + "-" + month + "-" + day);
            }
        });
    }

    @OnClick(R.id.btDateSelect)
    public void dateSelect() {
        if (dateList != null && dateList.size() > 0) {
            if (dateList.size() == 1) {
                pi.getResult(dateList);
                dismiss();
            }
            isSel = new ArrayList<Boolean>();
            for (String cd : dateList) {
                int year = Integer.parseInt(cd.substring(0, 4));
                int month = Integer.parseInt(cd.substring(4, 7));
                int day = Integer.parseInt(cd.substring(7, cd.length()));

                if (Math.abs(year) > time.year) {
//                    isSel.add(true);
                } else if (Math.abs(year) == time.year) {
                    if (Math.abs(month) > (time.month + 1)) {
//                        isSel.add(true);
                    } else if (Math.abs(month) == (time.month + 1)) {
                        if (Math.abs(day) >= time.monthDay) {
                            isSel.add(true);
                        } else {
                            isSel.add(false);
                        }
                    } else {
                        isSel.add(false);
                    }
                } else {
                    isSel.add(false);
                }
            }
            if (!isSel.contains(false)) {
                pi.getResult(dateList);
            }
            dismiss();
        }
    }
}
