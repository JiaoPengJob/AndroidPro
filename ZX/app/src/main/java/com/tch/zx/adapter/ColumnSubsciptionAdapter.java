package com.tch.zx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.bean.HomeBean;
import com.tch.zx.dao.green.SpecialBean;
import com.tch.zx.http.bean.result.HomeResultBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专栏订阅数据适配器
 */
public class ColumnSubsciptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    //文本对象
    private Context context;
    //数据集合
    private List<SpecialBean> list;

    private RvHotOnlinePlayerAdapter.OnItemClickListener mOnItemClickListener = null;

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

    public void setOnItemClickListener(RvHotOnlinePlayerAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public ColumnSubsciptionAdapter(Context context, List<SpecialBean> list) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_column_subsciption, parent, false);
        layout.setOnClickListener(this);
        return new ColumnSubsciptionAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ColumnSubsciptionAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getSpecialColumnPicMin()).into(((MyHolder) holder).iv_specialColumnClassPicMin);
            ((MyHolder) holder).tv_specialColumnName.setText(list.get(position).getSpecialColumnName());
            ((MyHolder) holder).tv_specialColumnByName.setText(list.get(position).getSpecialColumnByName());
            ((MyHolder) holder).tv_position_special.setText(list.get(position).getPosition());
            ((MyHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tv_specialColumnPrice.setText("￥" + list.get(position).getSpecialColumnPrice() + "/年");
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
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {
        /**
         * 父布局
         */
        View itemView;
        /**
         * 展示小图
         */
        @BindView(R.id.iv_specialColumnClassPicMin)
        ImageView iv_specialColumnClassPicMin;
        /**
         * 主标题
         */
        @BindView(R.id.tv_specialColumnName)
        TextView tv_specialColumnName;
        /**
         * 副标题
         */
        @BindView(R.id.tv_specialColumnByName)
        TextView tv_specialColumnByName;
        /**
         * 职位
         */
        @BindView(R.id.tv_position_special)
        TextView tv_position_special;
        /**
         * 姓名
         */
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        /**
         * 价格
         */
        @BindView(R.id.tv_specialColumnPrice)
        TextView tv_specialColumnPrice;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
