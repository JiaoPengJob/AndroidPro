package com.tch.zx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tch.zx.R;

import java.util.List;

/**
 * 正在直播/讨论
 */

public class LivingCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //文本对象
    private Context context;
    //数据集合
    private List<String> list;

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public LivingCommentAdapter(Context context, List<String> list) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_living_comment, parent, false);
        return new LivingCommentAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LivingCommentAdapter.MyHolder) {

        }
    }

    /**
     * 获取子项总数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);

        }
    }

}
