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
import com.tch.zx.adapter.RvHotOnlinePlayerAdapter;
import com.tch.zx.http.bean.result.OrderListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已购/直播适配器
 */

public class OnlinePurchasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
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

    public OnlinePurchasedAdapter(Context context, List<OrderListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_online_player_main, parent, false);
        layout.setOnClickListener(this);
        return new OnlinePurchasedAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OnlinePurchasedAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((MyHolder) holder).iv_livePicMin);
            ((MyHolder) holder).tv_liveName.setText(list.get(position).getName());
            ((MyHolder) holder).tv_position.setText(list.get(position).getPosition());
            ((MyHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tv_online_enjoy_num.setText(list.get(position).getLivePepoleNum() + "报名");
            ((MyHolder) holder).tv_liveMoney.setText("¥" + list.get(position).getPrice() + "元");
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
        @BindView(R.id.iv_livePicMin)
        ImageView iv_livePicMin;
        @BindView(R.id.tv_liveName)
        TextView tv_liveName;
        @BindView(R.id.tv_position)
        TextView tv_position;
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        @BindView(R.id.tv_online_enjoy_num)
        TextView tv_online_enjoy_num;
        @BindView(R.id.tv_liveMoney)
        TextView tv_liveMoney;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
