package com.tch.zx.adapter.line;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.SpecialListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专栏订阅列表适配器
 */

public class ColumnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<SpecialListResultBean.ResultBean.ResponseObjectBean> list;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public ColumnAdapter(Context context, List<SpecialListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

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
     * 可以根据viewType判断去创建不同item的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_column, parent, false);
        layout.setOnClickListener(this);
        return new ColumnAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ColumnAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getSpecialColumnPicMin()).into(((MyHolder) holder).ivSpecialColumnPicMin);
            ((MyHolder) holder).tvSpecialColumnName.setText(list.get(position).getSpecialColumnName());
            ((MyHolder) holder).tvPosition.setText(list.get(position).getPosition());
            ((MyHolder) holder).tvAppUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tvSpecialColumnByName.setText(list.get(position).getSpecialColumnByName());
            ((MyHolder) holder).tvSpecialColumnPrice.setText("￥" + list.get(position).getSpecialColumnPrice() + "/年");
            ((MyHolder) holder).tvSubscriptionNumber.setText(list.get(position).getSubscriptionNumber() + "人订阅");
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
        /**
         * 图片
         */
        @BindView(R.id.ivSpecialColumnPicMin)
        ImageView ivSpecialColumnPicMin;
        /**
         * 标题
         */
        @BindView(R.id.tvSpecialColumnName)
        TextView tvSpecialColumnName;
        /**
         * 职位
         */
        @BindView(R.id.tvPosition)
        TextView tvPosition;
        /**
         * 姓名
         */
        @BindView(R.id.tvAppUserName)
        TextView tvAppUserName;
        /**
         * 副标题
         */
        @BindView(R.id.tvSpecialColumnByName)
        TextView tvSpecialColumnByName;
        /**
         * 价格
         */
        @BindView(R.id.tvSpecialColumnPrice)
        TextView tvSpecialColumnPrice;
        /**
         * 订阅人数
         */
        @BindView(R.id.tvSubscriptionNumber)
        TextView tvSubscriptionNumber;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
