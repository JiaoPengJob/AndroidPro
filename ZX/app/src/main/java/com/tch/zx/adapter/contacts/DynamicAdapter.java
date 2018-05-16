package com.tch.zx.adapter.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.lqr.ninegridimageview.LQRNineGridImageView;
import com.lqr.ninegridimageview.LQRNineGridImageViewAdapter;
import com.tch.zx.R;
import com.tch.zx.activity.contacts.CommentActivity;
import com.tch.zx.adapter.message.GroupChatAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.http.bean.result.GetDynamicListResult;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.presenter.GiveFabulousPresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.http.view.GiveFabulousView;
import com.tch.zx.util.HelperUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 朋友圈动态
 */

public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetDynamicListResult.ResponseObjectBean> list;
    private Intent intent;
    private GiveFabulousPresenter giveFabulousPresenter;
    private int selIndex, num, selPosition;
    private BasePresenter<Object> presenter;

    public DynamicAdapter(Context context, List<GetDynamicListResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_dynamic_main, parent, false);
        return new DynamicAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DynamicAdapter.MyHolder) {
            List<String> imgList = new ArrayList<String>();
            if (list.get(position).getContent_picture() != null
                    && !list.get(position).getContent_picture().equals("")) {
                if (list.get(position).getContent_picture().contains(",")) {
                    String[] s = HelperUtil.stringToArray(list.get(position).getContent_picture(), ",");
                    for (int i = 0; i < s.length; i++) {
                        imgList.add(s[i]);
                    }
                } else {
                    imgList.add(list.get(position).getContent_picture());
                }
            }
            ((MyHolder) holder).ngiv_dynamic_item.setAdapter(mAdapter);
            ((MyHolder) holder).ngiv_dynamic_item.setImagesData(imgList);

            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civ_user_select_photo.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getUser_picture()).into(target);
            ((MyHolder) holder).tvItemDynamicName.setText(list.get(position).getName());
            ((MyHolder) holder).tvItemDynamicPosition.setText(list.get(position).getCompany_position());
            ((MyHolder) holder).tvItemDynamicTime.setText(list.get(position).getDynamic_create_date());
            ((MyHolder) holder).tvItemDynamicCompany.setText(list.get(position).getCompany_name());
            ((MyHolder) holder).tvItemDynamicContent.setText(list.get(position).getDynamic_content());
            if (list.get(position).getFabulous_num().equals("")) {
                ((MyHolder) holder).tvItemDynamicZanCount.setText("0");
            } else {
                ((MyHolder) holder).tvItemDynamicZanCount.setText(list.get(position).getFabulous_num());
            }

            if (list.get(position).getCount().equals("")) {
                ((MyHolder) holder).tvItemDynamicTalkCount.setText("0");
            } else {
                ((MyHolder) holder).tvItemDynamicTalkCount.setText(list.get(position).getCount());
            }

            if (HelperUtil.getAppUserId((Activity) context).equals(list.get(position).getApp_user_id())) {
                ((MyHolder) holder).ivDynamicItemDelete.setVisibility(View.VISIBLE);
            } else {
                ((MyHolder) holder).ivDynamicItemDelete.setVisibility(View.GONE);
            }

            //评论的点击事件监听
            ((MyHolder) holder).ll_send_comment_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("id", list.get(position).getDynamic_id());
                    intent.putExtra("userId", list.get(position).getApp_user_id());
                    context.startActivity(intent);
                }
            });

            ((MyHolder) holder).llZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selIndex = position;
                    num = Integer.parseInt(((MyHolder) holder).tvItemDynamicZanCount.getText().toString());
                    giveFabulous(list.get(position).getDynamic_id());
                }
            });

            ((MyHolder) holder).ivDynamicItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selPosition = position;
                    deleteDyNamic(list.get(position).getDynamic_id());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() != 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ngiv_dynamic_item)
        NineGridImageView ngiv_dynamic_item;
        @BindView(R.id.ll_send_comment_dynamic)
        LinearLayout ll_send_comment_dynamic;
        @BindView(R.id.llZan)
        LinearLayout llZan;
        @BindView(R.id.civ_user_select_photo)
        CircleImageView civ_user_select_photo;
        @BindView(R.id.tvItemDynamicName)
        TextView tvItemDynamicName;
        @BindView(R.id.tvItemDynamicPosition)
        TextView tvItemDynamicPosition;
        @BindView(R.id.tvItemDynamicTime)
        TextView tvItemDynamicTime;
        @BindView(R.id.tvItemDynamicCompany)
        TextView tvItemDynamicCompany;
        @BindView(R.id.tvItemDynamicContent)
        TextView tvItemDynamicContent;
        @BindView(R.id.tvItemDynamicZanCount)
        TextView tvItemDynamicZanCount;
        @BindView(R.id.tvItemDynamicTalkCount)
        TextView tvItemDynamicTalkCount;
        @BindView(R.id.ivDynamicItemDelete)
        ImageView ivDynamicItemDelete;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String photo) {
            Glide.with(context)
                    .load(photo)
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> photoList) {
            super.onItemImageClick(context, index, photoList);
        }
    };

    /**
     * 访问接口
     */
    private void giveFabulous(String commentId) {
        giveFabulousPresenter = new GiveFabulousPresenter(context);
        giveFabulousPresenter.onCreate();
        giveFabulousPresenter.attachView(giveFabulousView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appUserId", HelperUtil.getAppUserId((Activity) context));
        map.put("commentId", commentId);
        map.put("commentType", "4");

        String data = HelperUtil.getParameter(map);
        giveFabulousPresenter.giveFabulous(data);
    }

    /**
     * 接口回调
     */
    private GiveFabulousView giveFabulousView = new GiveFabulousView() {
        @Override
        public void onSuccess(RetResultBean retResultBean) {
            if (retResultBean != null) {
                if (retResultBean.getRet().equals("1")) {
                    Toast.makeText(context, "点赞成功!", Toast.LENGTH_SHORT).show();
                    String s = String.valueOf(num + 1);
                    list.get(selIndex).setFabulous_num(s);
                    notifyDataSetChanged();
                } else if (retResultBean.getRet().equals("2")) {
                    Toast.makeText(context, "不可重复点赞!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "giveFabulousView" + result);
        }
    };

    private void deleteDyNamic(String dynamicId) {
        presenter = new BasePresenter<Object>(context);
        presenter.onCreate();
        presenter.attachView(deleteDyNamicView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dynamicId", dynamicId);

        String data = HelperUtil.getParameter(map);
        presenter.deleteDyNamic(data);
    }

    private BaseView<Object> deleteDyNamicView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                list.remove(selPosition);
                DynamicAdapter.this.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "deleteDyNamicView接口错误" + result);
        }
    };

}
