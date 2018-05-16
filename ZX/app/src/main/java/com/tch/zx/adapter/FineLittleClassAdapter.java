package com.tch.zx.adapter;

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
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.bean.HomeBean;
import com.tch.zx.dao.green.SmallBean;
import com.tch.zx.http.bean.result.HomeResultBean;
import com.tch.zx.util.ConstantData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 精品小课显示列表的适配器
 */

public class FineLittleClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<SmallBean> list;
    private int type;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;

    /**
     * 构造函数
     *
     * @param context 文本对象
     * @param list    数据集合
     * @param type    判断是哪个activity调用
     */
    public FineLittleClassAdapter(Context context, List<SmallBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
            //使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_fine_little_class, parent, false);
        layout.setOnClickListener(this);
        return new FineLittleClassAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FineLittleClassAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            //热门榜单调用显示排名图标
            if (type == ConstantData.TOP_LIST_TYPE) {
                if (position == 0) {
                    ((MyHolder) holder).iv_fine_little_class_ranking.setVisibility(View.VISIBLE);
                    ((MyHolder) holder).iv_fine_little_class_ranking.setImageResource(R.mipmap.list_top_one);
                } else if (position == 1) {
                    ((MyHolder) holder).iv_fine_little_class_ranking.setVisibility(View.VISIBLE);
                    ((MyHolder) holder).iv_fine_little_class_ranking.setImageResource(R.mipmap.list_top_second);
                } else if (position == 2) {
                    ((MyHolder) holder).iv_fine_little_class_ranking.setVisibility(View.VISIBLE);
                    ((MyHolder) holder).iv_fine_little_class_ranking.setImageResource(R.mipmap.list_top_three);
                } else {
                    ((MyHolder) holder).iv_fine_little_class_ranking.setVisibility(View.GONE);
                }
            } else {
                //不需要排名的
                Glide.with(context).load(list.get(position).getSmallClassPicMax()).into(((MyHolder) holder).iv_item_fine_class_user_photo);
                //向自定义imageview中使用时,放入bitmap属性值
                SimpleTarget target = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ((MyHolder) holder).civ_user_photo.setImageBitmap(resource);
                    }
                };
                Glide.with(context).asBitmap().load(list.get(position).getAppUserPic()).into(target);
                ((MyHolder) holder).tv_appUserName_small.setText(list.get(position).getAppUserName());
                ((MyHolder) holder).tv_position_small.setText(list.get(position).getPosition());
                ((MyHolder) holder).tv_smallClassName.setText(list.get(position).getSmallClassName());
                ((MyHolder) holder).tv_item_fine_class.setText(list.get(position).getViewNum() + "观看");
                ((MyHolder) holder).tv_videoMoney_small.setText(String.valueOf("¥" + list.get(position).getVideoMoney()) + "元");
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

    /**
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {
        /**
         * 父布局
         */
        View itemView;
        /**
         * 排名图标
         */
        @BindView(R.id.iv_fine_little_class_ranking)
        ImageView iv_fine_little_class_ranking;
        /**
         * 背景大图
         */
        @BindView(R.id.iv_item_fine_class_user_photo)
        ImageView iv_item_fine_class_user_photo;
        /**
         * 头像
         */
        @BindView(R.id.civ_user_photo)
        CircleImageView civ_user_photo;
        /**
         * 姓名
         */
        @BindView(R.id.tv_appUserName_small)
        TextView tv_appUserName_small;
        /**
         * 职位
         */
        @BindView(R.id.tv_position_small)
        TextView tv_position_small;
        /**
         * 课程名称
         */
        @BindView(R.id.tv_smallClassName)
        TextView tv_smallClassName;
        /**
         * 观看人数
         */
        @BindView(R.id.tv_item_fine_class)
        TextView tv_item_fine_class;
        /**
         * 小课价格
         */
        @BindView(R.id.tv_videoMoney_small)
        TextView tv_videoMoney_small;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
