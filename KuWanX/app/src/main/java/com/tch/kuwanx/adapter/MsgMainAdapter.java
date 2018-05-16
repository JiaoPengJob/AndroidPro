package com.tch.kuwanx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tch.kuwanx.R;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by jiaop on 2017/11/30.
 */

public class MsgMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

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
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MsgMainAdapter(Context context, List<Conversation> list) {
        this.context = context;
        this.list = list;
    }

    private View layout;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.item_msg_private_parent, parent, false);
        }
        ViewGroup vParent = (ViewGroup) layout.getParent();
        if (vParent != null) {
            vParent.removeView(layout);
        }
        layout.setOnClickListener(this);
        return new MsgMainAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MsgMainAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).tvMsgPriChatUnread.setText(String.valueOf(list.get(position).getUnreadMessageCount()));
            if (list.get(position).getLatestMessage() instanceof TextMessage) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText(((TextMessage) list.get(position).getLatestMessage()).getContent());
            } else if (list.get(position).getLatestMessage() instanceof ImageMessage) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("[图片]");
            } else if (list.get(position).getLatestMessage() instanceof VoiceMessage) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("[语音]");
            } else if (list.get(position).getLatestMessage() instanceof FileMessage) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("[文件]");
            } else if (list.get(position).getLatestMessage() instanceof LocationMessage) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("[位置]");
            } else if (list.get(position).getObjectName() != null
                    && list.get(position).getObjectName().equals("RCD:KWMsg")) {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("[物品交换]");
            } else {
                ((MyHolder) holder).tvMsgPriChatNewTxt.setText("未知数据");
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

    class MyHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.tvMsgPriChatUnread)
        TextView tvMsgPriChatUnread;
        @BindView(R.id.tvMsgPriChatNewTxt)
        TextView tvMsgPriChatNewTxt;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
