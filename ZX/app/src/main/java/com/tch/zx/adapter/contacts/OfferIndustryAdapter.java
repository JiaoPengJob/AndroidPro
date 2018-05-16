package com.tch.zx.adapter.contacts;

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
import com.tch.zx.http.bean.result.UserFollowIndustryResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 人脉/合作/提供
 */

public class OfferIndustryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;
    private List<UserFollowIndustryResult.ResponseObjectBean> list;

    public OfferIndustryAdapter(Context context, List<UserFollowIndustryResult.ResponseObjectBean> list) {
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_industry, parent, false);
        layout.setOnClickListener(this);
        return new OfferIndustryAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OfferIndustryAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civIndustryItemPhoto.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getAppUserPic()).into(target);
            ((MyHolder) holder).tvIndustryItemName.setText(list.get(position).getAppUserName());
            ((MyHolder) holder).tvIndustryItemPosition.setText(list.get(position).getCompanyPosition());
            ((MyHolder) holder).tvIndustryItemStyle.setText(list.get(position).getStypeName());
            Glide.with(context)
                    .load(list.get(position).getCompanyLogo())
                    .into(((MyHolder) holder).ivIndustryItemCompanyLogo);
            ((MyHolder) holder).tvIndustryItemCompanyName.setText(list.get(position).getCompanyName());
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

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civIndustryItemPhoto)
        CircleImageView civIndustryItemPhoto;
        @BindView(R.id.tvIndustryItemName)
        TextView tvIndustryItemName;
        @BindView(R.id.tvIndustryItemPosition)
        TextView tvIndustryItemPosition;
        @BindView(R.id.tvIndustryItemStyle)
        TextView tvIndustryItemStyle;
        @BindView(R.id.ivIndustryItemCompanyLogo)
        ImageView ivIndustryItemCompanyLogo;
        @BindView(R.id.tvIndustryItemCompanyName)
        TextView tvIndustryItemCompanyName;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
