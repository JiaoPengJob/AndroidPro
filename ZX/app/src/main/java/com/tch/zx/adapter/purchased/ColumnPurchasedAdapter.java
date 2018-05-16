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
 * 已购/专栏
 */

public class ColumnPurchasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
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

    public ColumnPurchasedAdapter(Context context, List<OrderListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_column_subsciption, parent, false);
        layout.setOnClickListener(this);
        return new ColumnPurchasedAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ColumnPurchasedAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((MyHolder) holder).iv_specialColumnClassPicMin);
            ((MyHolder) holder).tv_specialColumnName.setText(list.get(position).getName());
            ((MyHolder) holder).tv_specialColumnByName.setText(list.get(position).getByName());
            ((MyHolder) holder).tv_position_special.setText(list.get(position).getPosition());
            ((MyHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tvUpdateDate.setText("【" + list.get(position).getUpdateDate() + "更新】");
            ((MyHolder) holder).tv_specialColumnPrice.setText("￥" + list.get(position).getPrice() + "/年");
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

        @BindView(R.id.iv_specialColumnClassPicMin)
        ImageView iv_specialColumnClassPicMin;
        @BindView(R.id.tv_specialColumnName)
        TextView tv_specialColumnName;
        @BindView(R.id.tv_specialColumnByName)
        TextView tv_specialColumnByName;
        @BindView(R.id.tv_position_special)
        TextView tv_position_special;
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        @BindView(R.id.tvUpdateDate)
        TextView tvUpdateDate;
        @BindView(R.id.tv_specialColumnPrice)
        TextView tv_specialColumnPrice;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
