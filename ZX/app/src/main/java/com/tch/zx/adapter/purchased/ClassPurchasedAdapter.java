package com.tch.zx.adapter.purchased;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.OrderListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已购/小课
 */

public class ClassPurchasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<OrderListResultBean.ResultBean.ResponseObjectBean> list;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;

    public void setSelectedPosition(int index) {
        this.index = index;
    }

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

    public ClassPurchasedAdapter(Context context, List<OrderListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_class_purchased, parent, false);
        layout.setOnClickListener(this);
        return new ClassPurchasedAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClassPurchasedAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((MyHolder) holder).ivPicMin);
            ((MyHolder) holder).tvName.setText(list.get(position).getName());
            ((MyHolder) holder).tvPosition.setText(list.get(position).getPosition());
            ((MyHolder) holder).tvAppUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tv_class_enjoy_num.setText(list.get(position).getViewNum() + "观看");
            ((MyHolder) holder).tvPrice.setText("¥" + list.get(position).getPrice() + "元");
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

        View itemView;
        @BindView(R.id.ivPicMin)
        ImageView ivPicMin;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPosition)
        TextView tvPosition;
        @BindView(R.id.tvAppUserName)
        TextView tvAppUserName;
        @BindView(R.id.tv_class_enjoy_num)
        TextView tv_class_enjoy_num;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
