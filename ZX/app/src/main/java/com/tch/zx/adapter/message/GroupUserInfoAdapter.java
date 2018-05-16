package com.tch.zx.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.activity.message.SelectFriendsActivity;
import com.tch.zx.http.bean.result.GetGroupListResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 群用户信息适配器
 */

public class GroupUserInfoAdapter extends BaseAdapter {

    private Context context;
    private List<GetGroupListResult.ResponseObjectBean.MenberListBean> list;
    private LayoutInflater layoutInflater;

    public GroupUserInfoAdapter(Context context, List<GetGroupListResult.ResponseObjectBean.MenberListBean> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.list.add(new GetGroupListResult.ResponseObjectBean.MenberListBean());
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public GetGroupListResult.ResponseObjectBean.MenberListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_group_user_info, null);
            viewHolder.civ_group_user_img = (CircleImageView) convertView.findViewById(R.id.civ_group_user_img);
            viewHolder.tv_group_user_name = (TextView) convertView.findViewById(R.id.tv_group_user_name);
            viewHolder.iv_add_group_people = (ImageView) convertView.findViewById(R.id.iv_add_group_people);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(list.get(position).getUser_picture())
                .into(viewHolder.civ_group_user_img);
        viewHolder.tv_group_user_name.setText(list.get(position).getMember_nickname());

        if (position == list.size() - 1) {
            viewHolder.civ_group_user_img.setVisibility(View.GONE);
            viewHolder.tv_group_user_name.setVisibility(View.GONE);
            viewHolder.iv_add_group_people.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public final class ViewHolder {
        public CircleImageView civ_group_user_img;
        public TextView tv_group_user_name;
        public ImageView iv_add_group_people;
    }

}
