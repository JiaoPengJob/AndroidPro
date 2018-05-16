package com.tch.zx.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.SearchInfoResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索历史
 */

public class SearchedEXAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupList;
    private List<List<SearchInfoResultBean.ResultBean.ResponseObjectBean>> itemList;

    /**
     * 构造函数
     *
     * @param context
     * @param groupList
     * @param itemList
     */
    public SearchedEXAdapter(Context context, List<String> groupList, List<List<SearchInfoResultBean.ResultBean.ResponseObjectBean>> itemList) {
        this.context = context;
        this.groupList = groupList;
        this.itemList = itemList;
    }

    /**
     * 获取组的个数
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        if (groupList != null) {
            return groupList.size();
        } else {
            return 0;
        }
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (itemList != null && itemList.size() > 0) {
            return itemList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     */
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public SearchInfoResultBean.ResultBean.ResponseObjectBean getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ex_first, null);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.tv_ex_first.setText(groupList.get(groupPosition));
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ex_second, null);
            itemHolder = new ItemHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        if (itemList.get(groupPosition).get(childPosition).getType().equals("1")) {
            Glide.with(context).load(itemList.get(groupPosition).get(childPosition).getLivePicMin()).into(itemHolder.ivPicMin);
            itemHolder.tvItemName.setText(itemList.get(groupPosition).get(childPosition).getLiveName());
        } else if (itemList.get(groupPosition).get(childPosition).getType().equals("2")) {
            Glide.with(context).load(itemList.get(groupPosition).get(childPosition).getSmallClassPicMin()).into(itemHolder.ivPicMin);
            itemHolder.tvItemName.setText(itemList.get(groupPosition).get(childPosition).getSmallClassName());
        } else if (itemList.get(groupPosition).get(childPosition).getType().equals("3")) {
            Glide.with(context).load(itemList.get(groupPosition).get(childPosition).getSpecialColumnPicMin()).into(itemHolder.ivPicMin);
            itemHolder.tvItemName.setText(itemList.get(groupPosition).get(childPosition).getSpecialColumnName());
        } else if (itemList.get(groupPosition).get(childPosition).getType().equals("4")) {
            Glide.with(context).load(itemList.get(groupPosition).get(childPosition).getOfflinePicMin()).into(itemHolder.ivPicMin);
            itemHolder.tvItemName.setText(itemList.get(groupPosition).get(childPosition).getOfflineName());
        }
        itemHolder.tv_ex_second_user.setText(itemList.get(groupPosition).get(childPosition).getPosition());
        itemHolder.tvUserName.setText(itemList.get(groupPosition).get(childPosition).getAppUserName());
        itemHolder.tvViewNum.setText(itemList.get(groupPosition).get(childPosition).getViewNum());
        return convertView;
    }

    /**
     * 是否选中指定位置上的子元素。是否选中指定位置上的子元素
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        @BindView(R.id.tv_ex_first)
        TextView tv_ex_first;

        public GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ItemHolder {
        @BindView(R.id.tv_ex_second_user)
        TextView tv_ex_second_user;
        @BindView(R.id.ivPicMin)
        ImageView ivPicMin;
        @BindView(R.id.tvItemName)
        TextView tvItemName;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvViewNum)
        TextView tvViewNum;

        public ItemHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
