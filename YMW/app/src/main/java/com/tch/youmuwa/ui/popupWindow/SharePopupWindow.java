package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tch.youmuwa.R;
import com.tch.youmuwa.myinterface.ShareInterface;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享
 */

public class SharePopupWindow extends PopupWindow {

    private Context context;
    private View view;
    private ShareInterface si;

    public SharePopupWindow(Context context, ShareInterface si) {
        this.context = context;
        this.si = si;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_employer_share, null);
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

    /**
     * 初始化
     */
    private void initView() {

    }

    /**
     * 微信
     */
    @OnClick(R.id.llWeChatShare)
    public void weChatShare() {
        si.getShareResult(SHARE_MEDIA.WEIXIN);
        dismiss();
    }

    /**
     * 朋友圈
     */
    @OnClick(R.id.llFriendsShare)
    public void friendsShare() {
        si.getShareResult(SHARE_MEDIA.WEIXIN_CIRCLE);
        dismiss();
    }

    /**
     * QQ
     */
    @OnClick(R.id.llQQShare)
    public void qqShare() {
        si.getShareResult(SHARE_MEDIA.QQ);
        dismiss();
    }

    /**
     * QQ空间
     */
    @OnClick(R.id.llQzoneShare)
    public void qZoneShare() {
        si.getShareResult(SHARE_MEDIA.QZONE);
        dismiss();
    }

    /**
     * 新浪微博
     */
    @OnClick(R.id.llSinaShare)
    public void sinaShare() {
        si.getShareResult(SHARE_MEDIA.SINA);
        dismiss();
    }


    /**
     * 取消
     */
    @OnClick(R.id.ibCancelShare)
    public void cancelShare() {
        dismiss();
    }
}
