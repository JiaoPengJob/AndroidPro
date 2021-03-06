package com.tch.zx.adapter.mine;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我能提供/我的需求列表的适配器
 */

public class GiveOrDemandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<String> list;

    public GiveOrDemandAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_give_or_demand, parent, false);
        return new GiveOrDemandAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GiveOrDemandAdapter.MyHolder) {
            if (position == 0) {
                ((MyHolder) holder).view_top_line_gd.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else if (position == list.size() - 1) {
                ((MyHolder) holder).view_bottom_line_gd.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {
        //上面的线
        @BindView(R.id.view_top_line_gd)
        View view_top_line_gd;
        //下面的线
        @BindView(R.id.view_bottom_line_gd)
        View view_bottom_line_gd;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
