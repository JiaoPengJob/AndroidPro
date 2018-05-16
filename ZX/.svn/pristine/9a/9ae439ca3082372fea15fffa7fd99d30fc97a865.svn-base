package com.tch.zx.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tch.zx.R;

/**
 * 打赏的弹出选项
 */

public class BindWeChatPopupWindow extends PopupWindow implements View.OnClickListener {

    //文本对象
    private Context context;
    //父布局
    private View view;
    //整体布局
    private RelativeLayout rl_popup_bind_wechat;
    //取消
    private TextView tv_cancel_bind_wechat;


    /**
     * 无参构造
     *
     * @param context
     */
    public BindWeChatPopupWindow(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.popup_bind_wechat, null);
        initView();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 设置外部可点击
        setOutsideTouchable(true);
        // view添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = rl_popup_bind_wechat.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.share_popup_anim);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        rl_popup_bind_wechat = (RelativeLayout) view.findViewById(R.id.rl_popup_bind_wechat);
        tv_cancel_bind_wechat = (TextView) view.findViewById(R.id.tv_cancel_bind_wechat);


        rl_popup_bind_wechat.setOnClickListener(this);
        tv_cancel_bind_wechat.setOnClickListener(this);

    }

    /**
     * 点击事件监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点屏幕取消
            case R.id.rl_popup_bind_wechat:
                dismiss();
                break;
            //取消按钮
            case R.id.tv_cancel_bind_wechat:
                dismiss();
                break;
        }
    }
}
