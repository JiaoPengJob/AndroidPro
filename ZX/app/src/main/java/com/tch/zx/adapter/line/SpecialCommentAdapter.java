package com.tch.zx.adapter.line;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.RetResultBean;
import com.tch.zx.http.bean.result.SmallCommentResultBean;
import com.tch.zx.http.bean.result.SpecialCommentResultBean;
import com.tch.zx.http.presenter.GiveFabulousPresenter;
import com.tch.zx.http.view.GiveFabulousView;
import com.tch.zx.util.HelperUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 专栏评论的适配器
 */

public class SpecialCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String commentType;
    private List<SpecialCommentResultBean.ResultBean.ResponseObjectBean> commentBeans;
    private GiveFabulousPresenter giveFabulousPresenter;

    public SpecialCommentAdapter(Context context, List<SpecialCommentResultBean.ResultBean.ResponseObjectBean> commentBeans, String commentType) {
        this.context = context;
        this.commentBeans = commentBeans;
        this.commentType = commentType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_comment_dynamic, parent, false);
        return new SpecialCommentAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SpecialCommentAdapter.MyHolder) {
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civUserPhoto.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(commentBeans.get(position).getAppUserPic()).into(target);
            ((MyHolder) holder).tvAppUserName.setText(commentBeans.get(position).getAppUserName());
            ((MyHolder) holder).tvPosition.setText(commentBeans.get(position).getPosition());
            ((MyHolder) holder).tvCommCreateTime.setText(commentBeans.get(position).getCommCreateTime());
            ((MyHolder) holder).tvCompanyName.setText(commentBeans.get(position).getCompanyName());
            ((MyHolder) holder).tvCommContent.setText(commentBeans.get(position).getCommContent());
            ((MyHolder) holder).tvFabulousNum.setText(String.valueOf(commentBeans.get(position).getFabulousNum()));
            ((MyHolder) holder).ivZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    giveFabulous(String.valueOf(commentBeans.get(position).getSpecialCommentClassId()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (commentBeans != null) {
            return commentBeans.size();
        } else {
            return 0;
        }
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civUserPhoto)
        CircleImageView civUserPhoto;
        @BindView(R.id.tvAppUserName)
        TextView tvAppUserName;
        @BindView(R.id.tvPosition)
        TextView tvPosition;
        @BindView(R.id.tvCommCreateTime)
        TextView tvCommCreateTime;
        @BindView(R.id.tvCompanyName)
        TextView tvCompanyName;
        @BindView(R.id.tvCommContent)
        TextView tvCommContent;
        @BindView(R.id.tvFabulousNum)
        TextView tvFabulousNum;
        @BindView(R.id.ivZan)
        ImageView ivZan;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

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
        map.put("commentType", commentType);

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
}
