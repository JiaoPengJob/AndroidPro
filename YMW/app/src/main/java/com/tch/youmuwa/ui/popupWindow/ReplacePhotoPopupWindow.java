package com.tch.youmuwa.ui.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tch.youmuwa.R;
import com.tch.youmuwa.myinterface.ShareInterface;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享
 */

public class ReplacePhotoPopupWindow extends PopupWindow {

    private Context context;
    private View view;

    @BindView(R.id.tvAlbum)
    TextView tvAlbum;
    @BindView(R.id.tvShoot)
    TextView tvShoot;

    public ReplacePhotoPopupWindow(Context context) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_replace_photo, null);
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
     * 取消
     */
    @OnClick(R.id.ivCancelReplacePhoto)
    public void cancelReplacePhoto() {
        dismiss();
    }

    public TextView getTvAlbum() {
        return tvAlbum;
    }

    public void setTvAlbum(TextView tvAlbum) {
        this.tvAlbum = tvAlbum;
    }

    public TextView getTvShoot() {
        return tvShoot;
    }

    public void setTvShoot(TextView tvShoot) {
        this.tvShoot = tvShoot;
    }
}
