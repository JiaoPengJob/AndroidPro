package com.tch.youmuwa.ui.view;

import android.content.Context;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.tch.youmuwa.R;
import com.tch.youmuwa.util.HelperUtil;

import java.util.Date;

/**
 * Created by peng on 2017/9/22.
 */

public class EnableDecorator implements DayViewDecorator {

    private boolean can;
    private Context context;
    private Date date;

    public EnableDecorator(Context context) {
        this.context = context;
        date = new Date();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String dateStr = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
        Log.e("TAG", "第一个=========" + HelperUtil.getStringToDate(dateStr, "yyyy-MM-dd"));
        Log.e("TAG", "第二个=========" + HelperUtil.getStringToDate(HelperUtil.simpleDate(date), "yyyy-MM-dd"));
        if (((int) HelperUtil.getStringToDate(dateStr, "yyyy-MM-dd")) < (int) HelperUtil.getStringToDate(HelperUtil.simpleDate(date), "yyyy-MM-dd")) {
            can = false;
        } else {
            can = true;
        }
        return can;
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (can) {
            view.setSelectionDrawable(context.getResources().getDrawable(R.drawable.oval_day_not_select));
        } else {
            view.setSelectionDrawable(context.getResources().getDrawable(R.drawable.oval_day_select));
        }
//        view.setDaysDisabled(can);
    }
}
