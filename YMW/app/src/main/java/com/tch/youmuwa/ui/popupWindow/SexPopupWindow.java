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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 性别选择
 */

public class SexPopupWindow extends PopupWindow {

    private Context context;
    private View view;
    @BindView(R.id.wlvSex)
    WheelListView wlvSex;

    private PopupInterface pi;
    private List<String> sexs;


    public SexPopupWindow(Context context, PopupInterface pi) {
        this.context = context;
        this.pi = pi;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_sex, null);
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
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_popup);
    }

    private void initView() {
        LineConfig config = new LineConfig();
        config.setColor(Color.parseColor("#000000"));//线颜色
        config.setAlpha(50);//线透明度
        config.setRatio(1f);//线比率
        config.setThick(ConvertUtils.toPx(context, 1));//线粗
        config.setShadowVisible(false);

        sexs = new ArrayList<String>();
        sexs.add("男");
        sexs.add("女");

        wlvSex.setItems(sexs, 0);
        wlvSex.setSelectedTextColor(Color.parseColor("#000000"));
        wlvSex.setLineConfig(config);
        wlvSex.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(boolean isUserScroll, int index, String item) {
                pi.getResult(6, sexs.get(index));
            }
        });
    }

    @OnClick({R.id.btSexCancel, R.id.btSexDetermine})
    public void sexCancel() {
        dismiss();
    }
}
