package com.tch.zx.adapter.mine;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.tch.zx.R;
import com.tch.zx.activity.contacts.CommentActivity;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.TestBean;
import com.tch.zx.http.bean.result.GetDynamicListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.HelperUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的动态
 */

public class DynamicMineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<GetDynamicListResult.ResponseObjectBean> list;

    private BasePresenter presenter;
    private int selPosition;

    public DynamicMineAdapter(Context context, List<GetDynamicListResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_dynamic_mine, parent, false);
        return new DynamicMineAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DynamicMineAdapter.MyHolder) {
            List<String> pictureList = new ArrayList<String>();
            if (list.get(position).getContent_picture() != null
                    && !list.get(position).getContent_picture().equals("")) {
                if (list.get(position).getContent_picture().contains(",")) {
                    String[] s = HelperUtil.stringToArray(list.get(position).getContent_picture(), ",");
                    for (int i = 0; i < s.length; i++) {
                        pictureList.add(s[i]);
                    }
                } else {
                    pictureList.add(list.get(position).getContent_picture());
                }
            }
            ((MyHolder) holder).ngiv_dynamic_info_mine.setAdapter(mAdapter);
            ((MyHolder) holder).ngiv_dynamic_info_mine.setImagesData(pictureList);

            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civ_user_select_photo.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getUser_picture()).into(target);
            ((MyHolder) holder).tvMyDynamicItemName.setText(list.get(position).getName());
            ((MyHolder) holder).tvMyDynamicItemTime.setText(list.get(position).getDynamic_create_date());
            ((MyHolder) holder).tvMyDynamicItemCompany.setText(list.get(position).getCompany_name());
            ((MyHolder) holder).tvMyDynamicItemPosition.setText(list.get(position).getCompany_position());
            ((MyHolder) holder).tvMyDynamicItemContent.setText(list.get(position).getDynamic_content());
            if (list.get(position).getFabulous_num().equals("")) {
                ((MyHolder) holder).tvMyDynamicItemZanCount.setText("0");
            } else {
                ((MyHolder) holder).tvMyDynamicItemZanCount.setText(list.get(position).getFabulous_num());
            }
            if (list.get(position).getCount().equals("")) {
                ((MyHolder) holder).tvMyDynamicItemTalkCount.setText("0");
            } else {
                ((MyHolder) holder).tvMyDynamicItemTalkCount.setText(list.get(position).getCount());
            }
            //评论
            ((MyHolder) holder).ll_send_comment_dynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("id", list.get(position).getDynamic_id());
                    intent.putExtra("userId", list.get(position).getApp_user_id());
                    context.startActivity(intent);
                }
            });
            //删除
            ((MyHolder) holder).ivMyDynamicItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selPosition = position;
                    deleteDyNamic(list.get(position).getDynamic_id());
                }
            });
        }
    }

    NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
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

        @BindView(R.id.ngiv_dynamic_info_mine)
        NineGridImageView ngiv_dynamic_info_mine;
        /**
         * 评论
         */
        @BindView(R.id.ll_send_comment_dynamic)
        LinearLayout ll_send_comment_dynamic;
        @BindView(R.id.ivMyDynamicItemDelete)
        ImageView ivMyDynamicItemDelete;
        @BindView(R.id.tvMyDynamicItemName)
        TextView tvMyDynamicItemName;
        @BindView(R.id.civ_user_select_photo)
        CircleImageView civ_user_select_photo;
        @BindView(R.id.tvMyDynamicItemPosition)
        TextView tvMyDynamicItemPosition;
        @BindView(R.id.tvMyDynamicItemTime)
        TextView tvMyDynamicItemTime;
        @BindView(R.id.tvMyDynamicItemCompany)
        TextView tvMyDynamicItemCompany;
        @BindView(R.id.tvMyDynamicItemContent)
        TextView tvMyDynamicItemContent;
        @BindView(R.id.tvMyDynamicItemZanCount)
        TextView tvMyDynamicItemZanCount;
        @BindView(R.id.tvMyDynamicItemTalkCount)
        TextView tvMyDynamicItemTalkCount;


        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

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
                DynamicMineAdapter.this.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "deleteDyNamicView接口错误" + result);
        }
    };

}
