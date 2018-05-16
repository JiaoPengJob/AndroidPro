package com.tch.zx.adapter.line;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.adapter.FineLittleClassAdapter;
import com.tch.zx.bean.SmallListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 精品小课/全部
 */
public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<SmallListBean.ResponseObject> list;
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
    public ClassAdapter(Context context, List<SmallListBean.ResponseObject> list) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_class_all, parent, false);
        layout.setOnClickListener(this);
        return new ClassAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClassAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getSmallClassPicMax()).into(((MyHolder) holder).iv_item_fine_class_user_photo);
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civ_user_photo.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getAppUserPic()).into(target);

            ((MyHolder) holder).tvModuleClassName.setText(list.get(position).getModuleClassName());
            ((MyHolder) holder).tv_appUserName_small.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tv_position_small.setText(list.get(position).getPosition());
            ((MyHolder) holder).tv_smallClassName.setText(list.get(position).getSmallClassName());
            ((MyHolder) holder).tv_item_fine_class.setText(list.get(position).getViewNum() + "观看");
            ((MyHolder) holder).tv_videoMoney_small.setText("¥" + list.get(position).getVideoMoney() + "元");

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
        /**
         * 父布局
         */
        View itemView;
        /**
         * 板块名称
         */
        @BindView(R.id.tvModuleClassName)
        TextView tvModuleClassName;
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
         * 视频名称
         */
        @BindView(R.id.tv_smallClassName)
        TextView tv_smallClassName;
        /**
         * 背景图片
         */
        @BindView(R.id.iv_item_fine_class_user_photo)
        ImageView iv_item_fine_class_user_photo;
        /**
         * 用户头像
         */
        @BindView(R.id.civ_user_photo)
        CircleImageView civ_user_photo;
        /**
         * 观看人数
         */
        @BindView(R.id.tv_item_fine_class)
        TextView tv_item_fine_class;
        /**
         * 价格
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
