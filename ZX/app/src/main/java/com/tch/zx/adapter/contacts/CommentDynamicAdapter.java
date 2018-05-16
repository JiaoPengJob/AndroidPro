package com.tch.zx.adapter.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tch.zx.R;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.result.GetDynamicCommentListResult;
import com.tch.zx.http.presenter.BasePresenter;
import com.tch.zx.http.view.BaseView;
import com.tch.zx.util.HelperUtil;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 动态评论的适配器
 */

public class CommentDynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<GetDynamicCommentListResult.ResponseObjectBean> list;
    private BasePresenter<Object> presenter;
    private UserBeanDao userBeanDao;
    private DaoSession daoSession;
    private int selIndex;
    private int num;

    public CommentDynamicAdapter(Context context, List<GetDynamicCommentListResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
        daoSession = ((MyApplication) context.getApplicationContext()).getDaoSession();
        userBeanDao = daoSession.getUserBeanDao();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_comment_dynamic, parent, false);
        return new CommentDynamicAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommentDynamicAdapter.MyHolder) {
            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    ((MyHolder) holder).civUserPhoto.setImageBitmap(resource);
                }
            };
            Glide.with(context).asBitmap().load(list.get(position).getUser_picture()).into(target);
            ((MyHolder) holder).tvAppUserName.setText(list.get(position).getName());
            ((MyHolder) holder).tvPosition.setText(list.get(position).getCompany_position());
            ((MyHolder) holder).tvCommCreateTime.setText(list.get(position).getSu_ccomment_create_time());
            ((MyHolder) holder).tvCompanyName.setText(list.get(position).getCompany_name());
            ((MyHolder) holder).tvCommContent.setText(list.get(position).getSu_ccomment_content());
            if (list.get(position).getFabulous_num().equals("")) {
                ((MyHolder) holder).tvFabulousNum.setText("0");
            } else {
                ((MyHolder) holder).tvFabulousNum.setText(list.get(position).getFabulous_num());
            }
            //点赞
            ((MyHolder) holder).ivZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selIndex = position;
                    num = Integer.parseInt(((MyHolder) holder).tvFabulousNum.getText().toString());
                    updateDynamicCommentFN(list.get(position).getSu_ccomment_id());
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

    private void updateDynamicCommentFN(String id) {
        presenter = new BasePresenter<Object>(context);
        presenter.onCreate();
        presenter.attachView(updateDynamicCommentFNView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("su_ccomment_id", id);
        map.put("app_user_id", userBeanDao.loadAll().get(0).getAppUserId());

        String data = HelperUtil.getParameter(map);
        presenter.updateDynamicCommentFN(data);
    }

    private BaseView<Object> updateDynamicCommentFNView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                String s = String.valueOf(num + 1);
                list.get(selIndex).setFabulous_num(s);
                notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "updateDynamicCommentFNView接口错误" + result);
        }
    };

}
