package com.tch.zx.adapter.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lqr.ninegridimageview.LQRNineGridImageView;
import com.lqr.ninegridimageview.LQRNineGridImageViewAdapter;
import com.tch.zx.R;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.GetGroupListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.HelperUtil;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 接收群信息的适配器
 */

public class GroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<GetGroupListResult.ResponseObjectBean> list;
    private String groupType;
    private OnItemClickListener mOnItemClickListener = null;
    private int index = 0;
    private BasePresenter<Object> presenter;
    private int selIndex;

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

    public GroupChatAdapter(Context context, List<GetGroupListResult.ResponseObjectBean> list, String groupType) {
        this.context = context;
        this.list = list;
        this.groupType = groupType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.swipe_menu_group_chat, parent, false);
        layout.setOnClickListener(this);
        return new GroupChatAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GroupChatAdapter.MyHolder) {
            ((MyHolder) holder).itemView.setTag(position);

            if (groupType.equals("unGroup")) {
                if (!list.get(position).getGroupType().equals("unGroup")) {
                    ((MyHolder) holder).itemView.setVisibility(View.GONE);
                }
            }

            //设置九宫格
            LQRNineGridImageViewAdapter adapter = new LQRNineGridImageViewAdapter<String>() {
                @Override
                protected void onDisplayImage(Context context, ImageView imageView, String s) {
                    //加载图片数据源
//                      ImageLoaderManager.LoadNetImage(s, imageView);
                    Glide.with(context)
                            .load(s)
                            .into(imageView);
                }

                //重写该方法自定义生成ImageView方式，用于九宫格头像中的一个个图片控件，可以设置ScaleType等属性
//                @Override
//                protected ImageView generateImageView(Context context) {
//                    ImageView imageView = new ImageView(context);
////                    imageView.setImageResource(R.mipmap.title_photo_fine_class);
//                    Glide.with(context)
//                            .load(s)
//                            .into(imageView);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    return imageView;
//                }
            };
            ((MyHolder) holder).lqrn_photo.setAdapter(adapter);
            List<String> mData = new ArrayList<String>();
            for (int i = 0; i < list.get(position).getMenberList().size(); i++) {
                mData.add(list.get(position).getMenberList().get(i).getUser_picture());
            }
            ((MyHolder) holder).lqrn_photo.setImagesData(mData);
            ((MyHolder) holder).tv_group_name.setText(list.get(position).getGroup_nickname());


            ((MyHolder) holder).tv_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyHolder) holder).swipeHorizontalMenuLayout.smoothCloseMenu();
                    selIndex = position;
                    groupQuitOrDismiss(list.get(position).getApp_user_id(), String.valueOf(list.get(position).getId()));
//                    list.remove(holder.getAdapterPosition());
//                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
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
    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lqrn_photo)
        LQRNineGridImageView lqrn_photo;

        View itemView;
        @BindView(R.id.swipeHorizontalMenuLayout)
        SwipeHorizontalMenuLayout swipeHorizontalMenuLayout;

        /**
         * 删除按钮
         */
        @BindView(R.id.tv_item_delete)
        TextView tv_item_delete;
        /**
         * 群昵称
         */
        @BindView(R.id.tv_group_name)
        TextView tv_group_name;

        @BindView(R.id.tvGroupContent)
        TextView tvGroupContent;

        @BindView(R.id.tvGroupNewTime)
        TextView tvGroupNewTime;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void groupQuitOrDismiss(String id, String groupId) {
        presenter = new BasePresenter<Object>(context);
        presenter.onCreate();
        presenter.attachView(groupQuitOrDismissView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", id);
        map.put("groupId", groupId);

        String data = HelperUtil.getParameter(map);
        presenter.groupQuitOrDismiss(data);
    }

    private BaseView<Object> groupQuitOrDismissView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                list.remove(selIndex);
                GroupChatAdapter.this.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "groupQuitOrDismissView接口错误" + result);
        }
    };

}
