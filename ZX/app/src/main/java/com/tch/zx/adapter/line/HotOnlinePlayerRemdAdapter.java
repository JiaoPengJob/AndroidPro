package com.tch.zx.adapter.line;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.activity.line.online.OnLinePlayerItemMainActivity;
import com.tch.zx.http.bean.result.LiveListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 热门直播/推荐
 */

public class HotOnlinePlayerRemdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<LiveListResultBean.ResultBean.ResponseObjectBean> list;

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
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public HotOnlinePlayerRemdAdapter(Context context, List<LiveListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 可以根据viewType判断去创建不同item的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_hot_online_player_remd, parent, false);
        layout.setOnClickListener(this);
        return new HotOnlinePlayerRemdAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotOnlinePlayerRemdAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civ_user_photo_hotonline.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getAppUserPic()).into(target);
            ((MyHolder) holder).tvLiveUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tvLivePosition.setText(list.get(position).getPosition());
            if (list.get(position).getLiveStatus().equals("1")) {
                ((MyHolder) holder).tvLiveStatus.setText("报名中");
                ((MyHolder) holder).tvLiveStatusRight.setText("报名中");
                ((MyHolder) holder).tvLiveViewNum.setText(list.get(position).getLivePepoleNum() + "报名");
                ((MyHolder) holder).llTime.setVisibility(View.VISIBLE);
                ((MyHolder) holder).tvLiveTime.setText(list.get(position).getLiveTime());
            } else if (list.get(position).getLiveStatus().equals("2")) {
                ((MyHolder) holder).tvLiveStatus.setText("直播中");
                ((MyHolder) holder).tvLiveStatusRight.setText("直播中");
                ((MyHolder) holder).tvLiveViewNum.setText(list.get(position).getLiveViewNum() + "正在观看");
            } else if (list.get(position).getLiveStatus().equals("3")) {
                ((MyHolder) holder).tvLiveStatus.setText("已经结束");
                ((MyHolder) holder).tvLiveStatusRight.setText("已经结束");
            }
            Glide.with(context).asBitmap().load(list.get(position).getLivePicMax()).into(((MyHolder) holder).ivLivePicMax);
            ((MyHolder) holder).tvLiveName.setText(list.get(position).getLiveName());
            ((MyHolder) holder).tvLiveMoney.setText("￥" + list.get(position).getLiveMoney() + "元");
        }
    }

    /**
     * 获取子项总数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    /**
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.civ_user_photo_hotonline)
        CircleImageView civ_user_photo_hotonline;
        @BindView(R.id.tvLiveUserName)
        TextView tvLiveUserName;
        @BindView(R.id.tvLivePosition)
        TextView tvLivePosition;
        @BindView(R.id.tvLiveStatus)
        TextView tvLiveStatus;
        @BindView(R.id.tvLiveStatusRight)
        TextView tvLiveStatusRight;
        @BindView(R.id.ivLivePicMax)
        ImageView ivLivePicMax;
        @BindView(R.id.tvLiveName)
        TextView tvLiveName;
        @BindView(R.id.tvLiveViewNum)
        TextView tvLiveViewNum;
        @BindView(R.id.tvLiveMoney)
        TextView tvLiveMoney;
        @BindView(R.id.llTime)
        LinearLayout llTime;
        @BindView(R.id.tvLiveTime)
        TextView tvLiveTime;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
