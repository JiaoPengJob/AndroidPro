package com.tch.zx.adapter.line;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.http.bean.result.SmallDetailsResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门直播/推荐
 */

public class TypesSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<SmallDetailsResultBean.ResultBean.ResponseObjectBean.VideoListBean> list;

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

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public TypesSelectAdapter(Context context, List<SmallDetailsResultBean.ResultBean.ResponseObjectBean.VideoListBean> list) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_class_info_types, parent, false);
        layout.setOnClickListener(this);
        return new TypesSelectAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypesSelectAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).tvPositionNum.setText("第" + (position + 1) + "集");
            ((MyHolder) holder).tvVideoName.setText(list.get(position).getVideoName());
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
        @BindView(R.id.tvPositionNum)
        TextView tvPositionNum;
        @BindView(R.id.tvVideoName)
        TextView tvVideoName;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
