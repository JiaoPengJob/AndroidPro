package com.tch.zx.adapter.login_register;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.http.bean.result.SIndustryListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行业右边
 */

public class ItemRightTradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<SIndustryListResultBean.ResultBean.ResponseObjectBean> list;

    private OnItemClickListener mOnItemClickListener = null;

    private int index = 0;

    public void setSelectedPosition(int index) {
        this.index = index;
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public ItemRightTradeAdapter(Context context, List<SIndustryListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_trade_right, parent, false);
        layout.setOnClickListener(this);
        return new ItemRightTradeAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemRightTradeAdapter.MyHolder) {
            ((ItemRightTradeAdapter.MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).tvSTypeName.setText(list.get(position).getIndustrySTypeName());
            if (position == index) {
                ((ItemRightTradeAdapter.MyHolder) holder).iv_item_trade_right.setImageResource(R.mipmap.item_trade_right_select);
            } else {
                ((ItemRightTradeAdapter.MyHolder) holder).iv_item_trade_right.setImageResource(R.mipmap.item_trade_right_unselect);
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

        @BindView(R.id.iv_item_trade_right)
        ImageView iv_item_trade_right;
        @BindView(R.id.tvSTypeName)
        TextView tvSTypeName;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
