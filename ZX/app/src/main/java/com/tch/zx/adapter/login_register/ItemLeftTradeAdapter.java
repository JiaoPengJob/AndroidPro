package com.tch.zx.adapter.login_register;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.adapter.RvHotOnlinePlayerAdapter;
import com.tch.zx.http.view.FIndustryListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行业左边
 */

public class ItemLeftTradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<FIndustryListResultBean.ResultBean.ResponseObjectBean> list;

    private OnItemClickListener mOnItemClickListener = null;

    private int index = 0;

    public void setSelectedPosition(int index) {
        this.index = index;
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ItemLeftTradeAdapter(Context context, List<FIndustryListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_trade_left, parent, false);
        layout.setOnClickListener(this);
        return new ItemLeftTradeAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemLeftTradeAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).tvFTypeName.setText(list.get(position).getIndustryFTypeName());
            if (position == index) {
                ((ItemLeftTradeAdapter.MyHolder) holder).view_item_trade_left.setBackgroundColor(Color.parseColor("#2EA168"));
            } else {
                ((ItemLeftTradeAdapter.MyHolder) holder).view_item_trade_left.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_item_trade_left)
        View view_item_trade_left;
        @BindView(R.id.tvFTypeName)
        TextView tvFTypeName;

        View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
