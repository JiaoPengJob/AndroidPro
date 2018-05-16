package com.tch.zx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.bean.HomeBean;
import com.tch.zx.dao.green.LiveBean;
import com.tch.zx.http.bean.result.HomeResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门直播在推荐主页显示列表的适配器
 */

public class RvHotOnlinePlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<LiveBean> list;

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

    public RvHotOnlinePlayerAdapter(Context context, List<LiveBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_online_player_main, parent, false);
        layout.setOnClickListener(this);
        return new MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getLivePicMin()).into(((MyHolder) holder).iv_livePicMin);
            ((MyHolder) holder).tv_liveName.setText(list.get(position).getLiveName());
            ((MyHolder) holder).tv_position.setText(list.get(position).getPosition());
            ((MyHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tv_online_enjoy_num.setText(list.get(position).getLivePepoleNum() + "报名");
            ((MyHolder) holder).tv_liveMoney.setText("¥" + list.get(position).getLiveMoney() + "元");
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
        /**
         * 报名人数
         */
        @BindView(R.id.tv_online_enjoy_num)
        TextView tv_online_enjoy_num;
        /**
         * 展示图片
         */
        @BindView(R.id.iv_livePicMin)
        ImageView iv_livePicMin;
        /**
         * 直播标题
         */
        @BindView(R.id.tv_liveName)
        TextView tv_liveName;
        /**
         * 职位
         */
        @BindView(R.id.tv_position)
        TextView tv_position;
        /**
         * 用户姓名
         */
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        /**
         * 直播价格
         */
        @BindView(R.id.tv_liveMoney)
        TextView tv_liveMoney;
        /**
         * 父布局
         */
        View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
