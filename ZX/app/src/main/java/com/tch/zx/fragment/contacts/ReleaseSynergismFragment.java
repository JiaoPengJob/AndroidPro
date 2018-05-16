package com.tch.zx.fragment.contacts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.activity.contacts.AddLabelActivity;
import com.tch.zx.view.FlowTagGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脉/合作/发布
 */

public class ReleaseSynergismFragment extends Fragment {

    /**
     * 标签列表
     */
    @BindView(R.id.ftg_release_industry)
    FlowTagGroup ftg_release_industry;
    @BindView(R.id.ftg_release_give)
    FlowTagGroup ftg_release_give;
    @BindView(R.id.ftg_release_demand)
    FlowTagGroup ftg_release_demand;

    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取父布局View
        viewRoot = inflater.inflate(R.layout.dialog_release_industry, container, false);
        //初始化ButterKnife
        ButterKnife.bind(this, viewRoot);
        initView();
        return viewRoot;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        setGroupData(ftg_release_industry, true);
        setGroupData(ftg_release_give, true);
        setGroupData(ftg_release_demand, true);
    }

    /**
     * 加载各组列表子项
     *
     * @param flowTagGroup
     */
    private void setGroupData(FlowTagGroup flowTagGroup, boolean isAddImg) {
        for (int i = 0; i < 5; i++) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 8, 0, 0);
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(lp);
            textView.setBackground(this.getResources().getDrawable(R.drawable.shape_corner_background_green_fifty));
            textView.setPadding(10, 5, 10, 5);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTextSize(12f);
            textView.setText("测试" + i);

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.clear_textview);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp.setMargins((int) textView.getPaint().measureText(textView.getText().toString()) + 9, 0, 0, 0);
            imageView.setLayoutParams(rlp);

            final RelativeLayout relativeLayout = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 60);
            rlp1.setMargins(5, 5, 5, 5);
            relativeLayout.setLayoutParams(rlp1);
            relativeLayout.addView(textView);
            if (isAddImg) {
                relativeLayout.addView(imageView);
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayout.removeAllViews();
                }
            });

            flowTagGroup.addView(relativeLayout);
        }
    }

    /**
     * 添加自定义标签监听事件
     */
    @OnClick({R.id.ll_add_demand, R.id.ll_add_give})
    public void addLable() {
        Intent intent = new Intent(getContext(), AddLabelActivity.class);
        startActivity(intent);
    }
}
