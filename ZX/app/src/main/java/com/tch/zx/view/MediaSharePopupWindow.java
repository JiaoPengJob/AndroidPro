package com.tch.zx.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.Toast;

import com.tch.zx.R;

/**
 * 播放器分享的弹出选项
 */

public class MediaSharePopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;
    //父布局
    private View view;
    //朋友圈分享按钮
    private LinearLayout ll_friends_share;
    //微信分享按钮
    private LinearLayout ll_wechat_share;
    //整体布局
    private TableLayout tlPopView;

    /**
     * 无参构造
     *
     * @param context
     */
    public MediaSharePopupWindow(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.popup_media_share, null);
        ll_friends_share = (LinearLayout) view.findViewById(R.id.ll_wechat_share);
        ll_wechat_share = (LinearLayout) view.findViewById(R.id.ll_wechat_share);
        tlPopView = (TableLayout) view.findViewById(R.id.tlPopView);
        ll_friends_share.setOnClickListener(this);
        ll_wechat_share.setOnClickListener(this);

        // 设置外部可点击
        setOutsideTouchable(true);
        // view添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = tlPopView.getTop();
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
     * 点击事件监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //朋友圈
            case R.id.ll_friends_share:
                Log.d("TAG","朋友圈");
                break;
            //微信
            case R.id.ll_wechat_share:
                Log.d("TAG","微信");
                break;
        }
    }
}
