package com.tch.zx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by peng on 2017/10/16.
 */

public class SystemMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> list;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;
    private static final int TYPE_ONLINE = 0;
    private static final int TYPE_CLASS = 1;
    private static final int TYPE_COLUMN = 2;

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

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    数据集合
     */
    public SystemMsgAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SystemMsgAdapter.TYPE_ONLINE) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_system_text_content, parent, false);
            mView.setOnClickListener(this);
            TextContentHolder viewHolder = new TextContentHolder(mView);
            return viewHolder;
        } else if (viewType == SystemMsgAdapter.TYPE_CLASS) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_system_video, parent, false);
            mView.setOnClickListener(this);
            VideoHolder viewHolder = new VideoHolder(mView);
            return viewHolder;
        } else if (viewType == SystemMsgAdapter.TYPE_COLUMN) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_system_photo, parent, false);
            mView.setOnClickListener(this);
            PhotoHolder viewHolder = new PhotoHolder(mView);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextContentHolder) {

        } else if (holder instanceof VideoHolder) {

        } else if (holder instanceof PhotoHolder) {

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

    /**
     * 返回值赋值给onCreateViewHolder的参数viewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (list.get(position).equals("0")) {
            return SystemMsgAdapter.TYPE_ONLINE;
        } else if (list.get(position).equals("1")) {
            return SystemMsgAdapter.TYPE_CLASS;
        } else if (list.get(position).equals("2")) {
            return SystemMsgAdapter.TYPE_COLUMN;
        }
        return super.getItemViewType(position);
    }

    /**
     * 文本消息
     */
    class TextContentHolder extends RecyclerView.ViewHolder {

        View itemView;

        public TextContentHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 视频消息
     */
    class VideoHolder extends RecyclerView.ViewHolder {

        View itemView;

        public VideoHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 图片消息
     */
    class PhotoHolder extends RecyclerView.ViewHolder {

        View itemView;

        public PhotoHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
