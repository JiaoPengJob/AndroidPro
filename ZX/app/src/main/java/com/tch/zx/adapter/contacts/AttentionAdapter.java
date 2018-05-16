package com.tch.zx.adapter.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.FriendListResult;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.presenter.ConcernCancelPresenter;
import com.tch.zx.http.view.ConcernCancelView;
import com.tch.zx.util.HelperUtil;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 关注显示列表的适配器
 */

public class AttentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<FriendListResult.ResponseObjectBean> list;

    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;
    private ConcernCancelPresenter cancelPresenter;
    private String id;
    private int positionIndex;

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

    public AttentionAdapter(Context context, String id, List<FriendListResult.ResponseObjectBean> list) {
        this.context = context;
        this.id = id;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.swipe_menu_attention, parent, false);
        layout.setOnClickListener(this);
        return new AttentionAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AttentionAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);
            ((MyHolder) holder).swipeHorizontalMenuLayout.setSwipeEnable(true);

            Glide.with(context)
                    .load(list.get(position).getUser_picture())
                    .into(((MyHolder) holder).civAddressBookUserPhoto);

            ((MyHolder) holder).tv_address_book_user_name.setText(list.get(position).getName());
            ((MyHolder) holder).tvAddressBookUserVer.setText(list.get(position).getCompany_name() + "  " + list.get(position).getCompany_position());

            ((MyHolder) holder).tv_unsubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionIndex = position;
                    concernCancel(list.get(position).getApp_user_id());
                    ((MyHolder) holder).swipeHorizontalMenuLayout.smoothCloseMenu();
//                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }
    }

    /**
     * 取消关注
     */
    private void concernCancel(String cancelId) {
        cancelPresenter = new ConcernCancelPresenter(context);
        cancelPresenter.onCreate();
        cancelPresenter.attachView(cancelView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", id);
        map.put("appUserConcern", cancelId);

        String data = HelperUtil.getParameter(map);
        cancelPresenter.concernCancel(data);
    }

    /**
     * 取消关注
     */
    private ConcernCancelView cancelView = new ConcernCancelView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean.getRet().equals("1")) {
                list.remove(positionIndex);
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "取消关注失败!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "cancelView:==" + result);
        }
    };

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

        View itemView;
        @BindView(R.id.swipeHorizontalMenuLayout)
        SwipeHorizontalMenuLayout swipeHorizontalMenuLayout;
        /**
         * 取消关注
         */
        @BindView(R.id.tv_unsubscribe)
        TextView tv_unsubscribe;
        @BindView(R.id.civAddressBookUserPhoto)
        CircleImageView civAddressBookUserPhoto;
        @BindView(R.id.tv_address_book_user_name)
        TextView tv_address_book_user_name;
        @BindView(R.id.tvAddressBookUserVer)
        TextView tvAddressBookUserVer;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
