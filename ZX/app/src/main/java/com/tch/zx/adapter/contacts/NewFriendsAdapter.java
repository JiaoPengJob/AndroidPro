package com.tch.zx.adapter.contacts;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.adapter.line.HotOnlinePlayerRemdAdapter;
import com.tch.zx.bean.BaseResultBean;
import com.tch.zx.bean.CityBean;
import com.tch.zx.http.bean.result.FriendAppyResult;
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
 * 精品小课显示列表的适配器
 */

public class NewFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;

    private List<FriendAppyResult.ResponseObjectBean> list;

    private BasePresenter presenter;

    public NewFriendsAdapter(Context context, List<FriendAppyResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    private NewFriendsAdapter.OnItemClickListener mOnItemClickListener = null;
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
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(NewFriendsAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_add_new_friends, parent, false);
        layout.setOnClickListener(this);
        return new NewFriendsAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewFriendsAdapter.MyHolder) {
            ((NewFriendsAdapter.MyHolder) holder).itemView.setTag(position);

            Glide.with(context)
                    .load(list.get(0).getUser_picture())
                    .into(((MyHolder) holder).civUserSelectPhoto);

            ((MyHolder) holder).tvNewFriendItemName.setText(list.get(0).getName());

            ((MyHolder) holder).tvNewFriendItemVer.setText(list.get(0).getUser_introduce());

            if (list.get(position).isIs_friend()) {
                ((MyHolder) holder).tv_add_friend_if_accept.setVisibility(View.GONE);
                ((MyHolder) holder).tv_add_friend_has_accept.setVisibility(View.VISIBLE);
            } else {
                ((MyHolder) holder).tv_add_friend_if_accept.setVisibility(View.VISIBLE);
                ((MyHolder) holder).tv_add_friend_has_accept.setVisibility(View.GONE);
            }

            ((MyHolder) holder).tv_add_friend_if_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((MyHolder) holder).tv_add_friend_if_accept.getText().toString().equals("接受")) {
                        tvAddFriendIfAccept = ((MyHolder) holder).tv_add_friend_if_accept;
                        tvAddFriendHasAccept = ((MyHolder) holder).tv_add_friend_has_accept;
                        processAppyFriendRequest(list.get(0).getAppyId());
                    }
                }
            });
        }
    }

    private TextView tvAddFriendIfAccept, tvAddFriendHasAccept;

    private void processAppyFriendRequest(String appyId) {
        presenter = new BasePresenter<Object>(context);
        presenter.onCreate();
        presenter.attachView(processAppyFriendView);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appyId", appyId);

        String data = HelperUtil.getParameter(map);
        presenter.processAppyFriendRequest(data);
    }

    private BaseView<Object> processAppyFriendView = new BaseView<Object>() {
        @Override
        public void onSuccess(BaseResultBean<Object> baseResultBean) {
            if (baseResultBean.getResult() != null && baseResultBean.getRet().equals("1")) {
                tvAddFriendIfAccept.setVisibility(View.GONE);
                tvAddFriendHasAccept.setVisibility(View.VISIBLE);
                Toast.makeText(context, "处理成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String result) {
            Log.e("ZX", "processAppyFriendView接口错误" + result);
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
        /**
         * 接受
         */
        @BindView(R.id.tv_add_friend_if_accept)
        TextView tv_add_friend_if_accept;
        /**
         * 已添加
         */
        @BindView(R.id.tv_add_friend_has_accept)
        TextView tv_add_friend_has_accept;
        @BindView(R.id.civ_user_select_photo)
        CircleImageView civUserSelectPhoto;
        @BindView(R.id.tvNewFriendItemName)
        TextView tvNewFriendItemName;
        @BindView(R.id.tvNewFriendItemVer)
        TextView tvNewFriendItemVer;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
