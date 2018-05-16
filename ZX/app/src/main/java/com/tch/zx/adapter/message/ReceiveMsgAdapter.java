package com.tch.zx.adapter.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.util.HelperUtil;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 接收信息的适配器
 */

public class ReceiveMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<Conversation> list;

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

    public ReceiveMsgAdapter(Context context, List<Conversation> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.swipe_menu_msg_main, parent, false);
        layout.setOnClickListener(this);
        return new ReceiveMsgAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReceiveMsgAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);

            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civUserItemPhoto.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getPortraitUrl()).into(target);

            ((MyHolder) holder).tvUserItemName.setText(list.get(position).getSenderUserName());
            ((MyHolder) holder).tvUserItemLastMsg.setText(list.get(position).getLatestMessage().encode().toString());
            ((MyHolder) holder).tvUserItemLastTime.setText(HelperUtil.changeDate(String.valueOf(list.get(position).getReceivedTime())));
            if(list.get(position).getUnreadMessageCount() == 0){
                ((MyHolder) holder).tvUserItemNum.setVisibility(View.GONE);
            }else {
                ((MyHolder) holder).tvUserItemNum.setVisibility(View.VISIBLE);
                ((MyHolder) holder).tvUserItemNum.setText(String.valueOf(list.get(position).getUnreadMessageCount()));
            }
            ((MyHolder) holder).tv_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyHolder) holder).swipeHorizontalMenuLayout.smoothCloseMenu();
                    list.remove(position);
                    ((MyHolder) holder).swipeHorizontalMenuLayout.removeAllViews();
                    notifyDataSetChanged();
                }
            });
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
    public class MyHolder extends RecyclerView.ViewHolder {

        View itemView;
        //删除按钮
        @BindView(R.id.tv_item_delete)
        TextView tv_item_delete;
        @BindView(R.id.swipeHorizontalMenuLayout)
        SwipeHorizontalMenuLayout swipeHorizontalMenuLayout;
        @BindView(R.id.civUserItemPhoto)
        CircleImageView civUserItemPhoto;
        @BindView(R.id.tvUserItemNum)
        TextView tvUserItemNum;
        @BindView(R.id.tvUserItemName)
        TextView tvUserItemName;
        @BindView(R.id.tvUserItemPosition)
        TextView tvUserItemPosition;
        @BindView(R.id.tvUserItemLastMsg)
        TextView tvUserItemLastMsg;
        @BindView(R.id.tvUserItemLastTime)
        TextView tvUserItemLastTime;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
