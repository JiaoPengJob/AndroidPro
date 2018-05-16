package com.tch.youmuwa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.bean.DateItem;
import com.tch.youmuwa.bean.ItemISSel;
import com.tch.youmuwa.ui.view.CustomCalendarView;
import com.tch.youmuwa.util.HelperUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日历适配器
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private List<DateItem> list;

    private Context context;
    private CustomCalendarView customCalendarView;
    private int itembackgourd;
    private Time time;
    private List<ItemISSel> isSels;
    private int oldIndex = -1, currentIndex = -1;
    private boolean b;

    public CalendarGridViewAdapter(CustomCalendarView customCalendarView,
                                   Context context, List<DateItem> list, int itembackgourd, boolean b) {
        this.context = context;
        this.list = list;
        this.itembackgourd = itembackgourd;
        this.customCalendarView = customCalendarView;
        this.b = b;
        time = new Time("GMT+8");
        time.setToNow();
        isSels = new ArrayList<ItemISSel>();
    }

    public void setDate(List<DateItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        LinearLayout linear = null;

        if (convertView == null) {
            textView = new TextView(context);
            linear = new LinearLayout(context);
            GridView.LayoutParams layoutParams = new GridView.LayoutParams(90, 90);
            linear.setLayoutParams(layoutParams);
            linear.setGravity(Gravity.CENTER);
            GridView.LayoutParams lp = new GridView.LayoutParams(80, 80);
            textView.setLayoutParams(lp);
            linear.addView(textView);
            convertView = linear;
        }

        textView = (TextView) (((ViewGroup) convertView).getChildAt(0));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);

        if(b){
            textView.setEnabled(true);
        }else{
            textView.setEnabled(false);
        }

        if (list.get(position).isselect()) {
            if (itembackgourd == -1) {
                convertView.setBackgroundResource(R.drawable.oval_day_select);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                textView.setBackgroundResource(itembackgourd);
            }
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            textView.setTextColor(Color.parseColor("#444444"));
        }

        if (list.get(position).getYear() == time.year
                && list.get(position).getMonth() == time.month
                && list.get(position).getDateOfMonth() == time.monthDay) {
            if (list.get(position).isselect()) {
                textView.setBackgroundResource(R.mipmap.today);
            } else {
                textView.setBackgroundResource(R.mipmap.today_select);
            }
        } else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }

        if (list.get(position).getDateOfMonth() > 0) {
            final TextView finalTextView = textView;
            final LinearLayout layout = (LinearLayout) convertView;
            textView.setText(String.valueOf(list.get(position).getDateOfMonth()));

            if (list.get(position).isHasSelect()) {
                textView.setBackgroundResource(R.drawable.oval_day_has_select);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setClickable(false);
            }

            isSels.add(new ItemISSel(false));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (true) {
//                        layout.requestLayout();
//                        Log.e("TAG", "进入第一层");
//                        currentIndex = position;
//                        if (oldIndex == currentIndex) {
//                            Log.e("TAG", "进入第二层true");
//                            if (isSels.get(currentIndex).isSel()) {
//                                Log.e("TAG", "进入第三层true");
//                                isSels.get(currentIndex).setSel(false);
//                                layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                                finalTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                                finalTextView.setTextColor(Color.parseColor("#444444"));
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(list.get(currentIndex).getYear(), list.get(currentIndex).getMonth(), list.get(currentIndex).getDateOfMonth());
//                                customCalendarView.removeSelect(HelperUtil.simpleDate(calendar.getTime()));
//                            } else {
//                                Log.e("TAG", "进入第三层false");
//                                isSels.get(currentIndex).setSel(true);
//                                layout.setBackgroundResource(R.drawable.oval_day_select);
//                                finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(list.get(currentIndex).getYear(), list.get(currentIndex).getMonth(), list.get(currentIndex).getDateOfMonth());
//                                customCalendarView.addSelectDate(HelperUtil.simpleDate(calendar.getTime()));
//                            }
//                        } else {
//                            Log.e("TAG", "进入第二层false");
//                            oldIndex = currentIndex;
//                            if (isSels.get(currentIndex).isSel()) {
//                                Log.e("TAG", "进入第四层true");
//                                isSels.get(currentIndex).setSel(false);
//                                layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                                finalTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                                finalTextView.setTextColor(Color.parseColor("#444444"));
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(list.get(currentIndex).getYear(), list.get(currentIndex).getMonth(), list.get(currentIndex).getDateOfMonth());
//                                customCalendarView.removeSelect(HelperUtil.simpleDate(calendar.getTime()));
//                            } else {
//                                Log.e("TAG", "进入第四层false");
//                                isSels.get(currentIndex).setSel(true);
//                                layout.setBackgroundResource(R.drawable.oval_day_select);
//                                finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
//
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(list.get(currentIndex).getYear(), list.get(currentIndex).getMonth(), list.get(currentIndex).getDateOfMonth());
//                                customCalendarView.addSelectDate(HelperUtil.simpleDate(calendar.getTime()));
//                            }
//                        }

//                        if (isSels.get(position).isSel()) {
//                            isSels.get(position).setSel(false);
//                            layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                            finalTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                            finalTextView.setTextColor(Color.parseColor("#444444"));
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(list.get(position).getYear(), list.get(position).getMonth(), list.get(position).getDateOfMonth());
//                            customCalendarView.removeSelect(HelperUtil.simpleDate(calendar.getTime()));
//                        } else {
//                            isSels.get(position).setSel(true);
//                            layout.setBackgroundResource(R.drawable.oval_day_select);
//                            finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(list.get(position).getYear(), list.get(position).getMonth(), list.get(position).getDateOfMonth());
//                            customCalendarView.addSelectDate(HelperUtil.simpleDate(calendar.getTime()));
//                        }
//                    } else {
                    if (!list.get(position).isHasSelect()) {
                        boolean canClick;
                        if (list.get(position).getYear() > time.year) {
                            canClick = true;
                        } else if (list.get(position).getYear() == time.year) {
                            if (list.get(position).getMonth() > time.month) {
                                canClick = true;
                            } else if (list.get(position).getMonth() == time.month) {
                                if (list.get(position).getDateOfMonth() >= time.monthDay) {
                                    canClick = true;
                                } else {
                                    canClick = false;
                                }
                            } else {
                                canClick = false;
                            }
                        } else {
                            canClick = false;
                        }

                        if (canClick) {
                            list.get(position).setIsselect(!list.get(position).isselect());
                            if (list.get(position).isselect()) {
                                if (itembackgourd == -1) {
                                    if (list.get(position).getYear() == time.year
                                            && list.get(position).getMonth() == time.month
                                            && list.get(position).getDateOfMonth() == time.monthDay) {
                                        layout.setBackgroundResource(R.drawable.oval_day_select);
                                        finalTextView.setBackgroundResource(R.mipmap.today);
                                        finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
                                    } else {
                                        layout.setBackgroundResource(R.drawable.oval_day_select);
                                        finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
                                    }
                                } else {
                                    finalTextView.setBackgroundResource(R.drawable.oval_day_select);
                                    finalTextView.setTextColor(Color.parseColor("#FFFFFF"));
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(list.get(position).getYear(), list.get(position).getMonth(), list.get(position).getDateOfMonth());
                                customCalendarView.addSelectDate(HelperUtil.simpleDate(calendar.getTime()));
                            } else {
                                if (list.get(position).getYear() == time.year
                                        && list.get(position).getMonth() == time.month
                                        && list.get(position).getDateOfMonth() == time.monthDay) {
                                    layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                                    finalTextView.setBackgroundResource(R.mipmap.today_select);
                                    finalTextView.setTextColor(Color.parseColor("#444444"));
                                } else {
                                    layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                                    finalTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                                    finalTextView.setTextColor(Color.parseColor("#444444"));
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(list.get(position).getYear(), list.get(position).getMonth(), list.get(position).getDateOfMonth());
                                customCalendarView.removeSelect(HelperUtil.simpleDate(calendar.getTime()));
                            }
                        }
                    }
//                    }
                }
            });
        } else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            textView.setTextColor(Color.parseColor("#444444"));
            textView.setText("");
        }
        return convertView;
    }
}
