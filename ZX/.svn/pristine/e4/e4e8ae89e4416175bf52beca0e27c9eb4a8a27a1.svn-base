package com.tch.zx.wheelview.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tch.zx.R;
import com.tch.zx.wheelview.adapter.SexAdapter;
import com.tch.zx.wheelview.utils.Utils;
import com.tch.zx.wheelview.view.listener.OnAddressChangeListener;
import com.tch.zx.wheelview.view.wheelview.MyOnWheelChangedListener;
import com.tch.zx.wheelview.view.wheelview.MyWheelView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置性别
 */
public class ChooseSexWheel implements MyOnWheelChangedListener {


    @BindView(R.id.sex_wheel)
    MyWheelView sex_wheel;

    private List<String> list;
    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;
    private OnAddressChangeListener onAddressChangeListener = null;

    public ChooseSexWheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();
    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_sex_layout, null);
        ButterKnife.bind(this, parentView);
        sex_wheel.setVisibleItems(7);
        sex_wheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    public void bindData(List<String> list) {
        this.list = list;
        sex_wheel.setViewAdapter(new SexAdapter(context, list));
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {

    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.confirm_button)
    public void confirm() {
        if (onAddressChangeListener != null) {
            onAddressChangeListener.onAddressChange("", list.get(sex_wheel.getCurrentItem()), "");
        }
        cancel();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }
}