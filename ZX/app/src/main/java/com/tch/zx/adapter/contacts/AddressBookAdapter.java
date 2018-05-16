package com.tch.zx.adapter.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.bean.CityBean;
import com.tch.zx.http.bean.result.FriendListResult;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 人脉/通讯录显示列表的适配器
 */

public class AddressBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<FriendListResult.ResponseObjectBean> list;

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

    public AddressBookAdapter(Context context, List<FriendListResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.swipe_menu_layout, parent, false);
        layout.setOnClickListener(this);
        return new AddressBookAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddressBookAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            Glide.with(context)
                    .load(list.get(position).getUser_picture())
                    .into(((MyHolder) holder).civAddressBookUserPhoto);
            ((MyHolder) holder).tv_address_book_user_name.setText(list.get(position).getName());
            ((MyHolder) holder).tvAddressBookUserVer.setText(list.get(position).getUser_introduce());
            ((MyHolder) holder).swipeHorizontalMenuLayout.setSwipeEnable(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {

        View itemView;
        @BindView(R.id.tv_address_book_user_name)
        TextView tv_address_book_user_name;
        @BindView(R.id.swipeHorizontalMenuLayout)
        SwipeHorizontalMenuLayout swipeHorizontalMenuLayout;
        @BindView(R.id.civAddressBookUserPhoto)
        CircleImageView civAddressBookUserPhoto;
        @BindView(R.id.tvAddressBookUserVer)
        TextView tvAddressBookUserVer;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
