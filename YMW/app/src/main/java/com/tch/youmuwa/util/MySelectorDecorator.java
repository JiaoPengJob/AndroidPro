package com.tch.youmuwa.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.tch.youmuwa.R;

/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private final CalendarDay today;

    public MySelectorDecorator(Context context) {
        today = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.today_select);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return today.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
