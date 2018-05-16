package com.tch.zx.adapter.purchased;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.bean.TestBean;
import com.tch.zx.http.bean.result.OrderListResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已购/全部适配器
 */

public class PurchasedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OrderListResultBean.ResultBean.ResponseObjectBean> list;

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
    public PurchasedAdapter(Context context, List<OrderListResultBean.ResultBean.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PurchasedAdapter.TYPE_ONLINE) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_online_player_main,parent, false);
            mView.setOnClickListener(this);
            OnlineHolder viewHolder = new OnlineHolder(mView);
            return viewHolder;
        } else if (viewType == PurchasedAdapter.TYPE_CLASS) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_class_purchased,parent, false);
            mView.setOnClickListener(this);
            ClassHolder viewHolder = new ClassHolder(mView);
            return viewHolder;
        } else if (viewType == PurchasedAdapter.TYPE_COLUMN) {
            View mView = LayoutInflater.from(context).inflate(R.layout.item_column_subsciption,parent, false);
            mView.setOnClickListener(this);
            ColumnHolder viewHolder = new ColumnHolder(mView);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OnlineHolder) {
            ((OnlineHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((OnlineHolder) holder).iv_livePicMin);
            ((OnlineHolder) holder).tv_liveName.setText(list.get(position).getName());
            ((OnlineHolder) holder).tv_position.setText(list.get(position).getPosition());
            ((OnlineHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((OnlineHolder) holder).tv_online_enjoy_num.setText(list.get(position).getLivePepoleNum() + "报名");
            ((OnlineHolder) holder).tv_liveMoney.setText("¥" + list.get(position).getPrice() + "元");
        } else if (holder instanceof ClassHolder) {
            ((ClassHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((ClassHolder) holder).ivPicMin);
            ((ClassHolder) holder).tvName.setText(list.get(position).getName());
            ((ClassHolder) holder).tvPosition.setText(list.get(position).getPosition());
            ((ClassHolder) holder).tvAppUserName.setText(list.get(position).getAppUserName());
            ((ClassHolder) holder).tv_class_enjoy_num.setText(list.get(position).getViewNum() + "观看");
            ((ClassHolder) holder).tvPrice.setText("¥" + list.get(position).getPrice() + "元");
        } else if (holder instanceof ColumnHolder) {
            ((ColumnHolder) holder).itemView.setTag(position);
            Glide.with(context).load(list.get(position).getPicMin()).into(((ColumnHolder) holder).iv_specialColumnClassPicMin);
            ((ColumnHolder) holder).tv_specialColumnName.setText(list.get(position).getName());
            ((ColumnHolder) holder).tv_specialColumnByName.setText(list.get(position).getByName());
            ((ColumnHolder) holder).tv_position_special.setText(list.get(position).getPosition());
            ((ColumnHolder) holder).tv_appUserName.setText(list.get(position).getAppUserName());
            ((ColumnHolder) holder).tvUpdateDate.setText("【" + list.get(position).getUpdateDate() + "更新】");
            ((ColumnHolder) holder).tv_specialColumnPrice.setText("￥" + list.get(position).getPrice() + "/年");

        } else {
            Log.e("Error", "Adapter -1");
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
        if (list.get(position).getOrderClassType().equals("1")) {
            return PurchasedAdapter.TYPE_ONLINE;
        } else if (list.get(position).getOrderClassType().equals("2")) {
            return PurchasedAdapter.TYPE_CLASS;
        } else if (list.get(position).getOrderClassType().equals("3")) {
            return PurchasedAdapter.TYPE_COLUMN;
        }
        return super.getItemViewType(position);
    }

    /**
     * 直播
     */
    class OnlineHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.iv_livePicMin)
        ImageView iv_livePicMin;
        @BindView(R.id.tv_liveName)
        TextView tv_liveName;
        @BindView(R.id.tv_position)
        TextView tv_position;
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        @BindView(R.id.tv_online_enjoy_num)
        TextView tv_online_enjoy_num;
        @BindView(R.id.tv_liveMoney)
        TextView tv_liveMoney;

        public OnlineHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 小课
     */
    class ClassHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.ivPicMin)
        ImageView ivPicMin;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPosition)
        TextView tvPosition;
        @BindView(R.id.tvAppUserName)
        TextView tvAppUserName;
        @BindView(R.id.tv_class_enjoy_num)
        TextView tv_class_enjoy_num;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ClassHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 专栏
     */
    class ColumnHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.iv_specialColumnClassPicMin)
        ImageView iv_specialColumnClassPicMin;
        @BindView(R.id.tv_specialColumnName)
        TextView tv_specialColumnName;
        @BindView(R.id.tv_specialColumnByName)
        TextView tv_specialColumnByName;
        @BindView(R.id.tv_position_special)
        TextView tv_position_special;
        @BindView(R.id.tv_appUserName)
        TextView tv_appUserName;
        @BindView(R.id.tvUpdateDate)
        TextView tvUpdateDate;
        @BindView(R.id.tv_specialColumnPrice)
        TextView tv_specialColumnPrice;

        public ColumnHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
