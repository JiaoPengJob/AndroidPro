package com.tch.zx.adapter.line;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.OfflineListResultBean;
import com.tch.zx.util.HelperUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 线下列表的适配器
 */

public class LineDownMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OfflineListResultBean.ResultBean.ResponseObjectBean> list;
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

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public LineDownMainAdapter(Context context, List<OfflineListResultBean.ResultBean.ResponseObjectBean> list) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_line_down_main, parent, false);
        layout.setOnClickListener(this);
        return new LineDownMainAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LineDownMainAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getOfflinePicMax()).into(((MyHolder) holder).iv_item_fine_class_user_photo);
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civ_user_photo_unline.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getUserPicture()).into(target);
            ((MyHolder) holder).tvAppUserName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tvPosition.setText(list.get(position).getPosition());
            ((MyHolder) holder).tvOfflineName.setText(list.get(position).getOfflineName());
            ((MyHolder) holder).tvOfflineClassIntroduce.setText(list.get(position).getOfflineClassIntroduce());
            ((MyHolder) holder).tvOfflineTime.setText(HelperUtil.getShortTime(list.get(position).getOfflineStartTime()) + "~" +
                    HelperUtil.getShortTime(list.get(position).getOfflineEndTime()));
            ((MyHolder) holder).tvOfflineAddress.setText(list.get(position).getOfflineAddress());
            ((MyHolder) holder).tvOfflineMoney.setText("¥" + list.get(position).getOfflineMoney() + "元");

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
         * 背景图片
         */
        @BindView(R.id.iv_item_fine_class_user_photo)
        ImageView iv_item_fine_class_user_photo;
        /**
         * 头像
         */
        @BindView(R.id.civ_user_photo_unline)
        CircleImageView civ_user_photo_unline;
        /**
         * 姓名
         */
        @BindView(R.id.tvAppUserName)
        TextView tvAppUserName;
        /**
         * 职位
         */
        @BindView(R.id.tvPosition)
        TextView tvPosition;
        /**
         * 标题
         */
        @BindView(R.id.tvOfflineName)
        TextView tvOfflineName;
        /**
         * 介绍
         */
        @BindView(R.id.tvOfflineClassIntroduce)
        TextView tvOfflineClassIntroduce;
        /**
         * 时长
         */
        @BindView(R.id.tvOfflineTime)
        TextView tvOfflineTime;
        /**
         * 地址
         */
        @BindView(R.id.tvOfflineAddress)
        TextView tvOfflineAddress;
        /**
         * 价格
         */
        @BindView(R.id.tvOfflineMoney)
        TextView tvOfflineMoney;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
