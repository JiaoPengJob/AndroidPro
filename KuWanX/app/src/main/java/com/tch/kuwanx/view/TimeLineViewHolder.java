package com.tch.kuwanx.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcssloop.widget.RCRelativeLayout;
import com.github.vipulasri.timelineview.TimelineView;
import com.tch.kuwanx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.timeMarker)
    TimelineView mTimelineView;
    @BindView(R.id.tvSysDate)
    TextView tvSysDate;
    @BindView(R.id.ivSysPhoto)
    ImageView ivSysPhoto;
    @BindView(R.id.tvSysTitle)
    TextView tvSysTitle;
    @BindView(R.id.tvSysMessage)
    TextView tvSysMessage;
    @BindView(R.id.rcSysContent)
    RCRelativeLayout rcSysContent;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
