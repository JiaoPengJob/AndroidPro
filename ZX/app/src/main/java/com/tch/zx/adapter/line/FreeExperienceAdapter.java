package com.tch.zx.adapter.line;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.SpecialWhetherPayResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 免费体验的列表适配器
 */

public class FreeExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<SpecialWhetherPayResultBean.ResultBean.ResponseObjectBean> whetherPayResultBeans;

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
     * @param context               当前文本对象
     * @param whetherPayResultBeans 需要加载的数据集合
     */
    public FreeExperienceAdapter(Context context, List<SpecialWhetherPayResultBean.ResultBean.ResponseObjectBean> whetherPayResultBeans) {
        this.context = context;
        this.whetherPayResultBeans = whetherPayResultBeans;
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_free_experience, parent, false);
        layout.setOnClickListener(this);
        return new FreeExperienceAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FreeExperienceAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).tvSpecialColumnClassName.setText(whetherPayResultBeans.get(position).getSpecialColumnClassName());
            ((MyHolder) holder).tvCreateDate.setText(whetherPayResultBeans.get(position).getCreateDate());
            ((MyHolder) holder).tvViewNum.setText(whetherPayResultBeans.get(position).getViewNum() + "人观看");
            Glide.with(context).load(whetherPayResultBeans.get(position).getSpecialColumnClassPicMax()).into(((MyHolder) holder).ivSpecialColumnClassPicMax);
            ((MyHolder) holder).tvSpecialColumnClassIntroduce.setText(whetherPayResultBeans.get(position).getSpecialColumnClassIntroduce());
        }
    }

    /**
     * 获取子项总数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (whetherPayResultBeans.size() != 0) {
            return whetherPayResultBeans.size();
        } else {
            return 0;
        }
    }

    /**
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.tvSpecialColumnClassName)
        TextView tvSpecialColumnClassName;
        @BindView(R.id.tvCreateDate)
        TextView tvCreateDate;
        @BindView(R.id.tvViewNum)
        TextView tvViewNum;
        @BindView(R.id.ivSpecialColumnClassPicMax)
        ImageView ivSpecialColumnClassPicMax;
        @BindView(R.id.tvSpecialColumnClassIntroduce)
        TextView tvSpecialColumnClassIntroduce;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
