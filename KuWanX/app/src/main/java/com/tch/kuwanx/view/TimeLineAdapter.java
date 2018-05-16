package com.tch.kuwanx.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.tch.kuwanx.R;
import com.tch.kuwanx.result.SysMsgListResult;
import com.tch.kuwanx.ui.mine.settings.SysMessageDetailsActivity;
import com.tch.kuwanx.utils.Utils;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<SysMsgListResult.ResultBean> mFeedList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<SysMsgListResult.ResultBean> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.item_system_msg, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(R.drawable.placeholder)
                .into(holder.ivSysPhoto);
        holder.tvSysDate.setText(Utils.times(mFeedList.get(position).getCreate_time(), "yyyy.MM.dd HH:mm:ss"));
        holder.tvSysTitle.setText(mFeedList.get(position).getMsg_title());
        holder.tvSysMessage.setText(mFeedList.get(position).getMsg_desc());
        holder.rcSysContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SysMessageDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", mFeedList.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

}
