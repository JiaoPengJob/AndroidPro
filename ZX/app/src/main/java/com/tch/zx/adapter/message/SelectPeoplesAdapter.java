package com.tch.zx.adapter.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tch.zx.R;
import com.tch.zx.http.bean.result.FriendListResult;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 选择联系人列表适配器
 */

public class SelectPeoplesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //文本对象
    private Context context;
    //数据集合
    private List<FriendListResult.ResponseObjectBean> list;
    //是否选中的集合
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    //是否选中
    private boolean mIsSelectable = false;

    /**
     * 构造函数
     *
     * @param context 当前文本对象
     * @param list    需要加载的数据集合
     */
    public SelectPeoplesAdapter(Context context, List<FriendListResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 可以根据viewType判断去创建不同item的ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_selete_proples, parent, false);
        return new SelectPeoplesAdapter.MyHolder(layout);
    }

    /**
     * 加载子控件，设置响应事件
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SelectPeoplesAdapter.MyHolder) {

            Glide.with(context)
                    .load(list.get(position).getUser_picture())
                    .into(((MyHolder) holder).civ_user_select_photo);
            ((MyHolder) holder).tv_user_select_name.setText(list.get(position).getName());

            //设置条目状态
            ((MyHolder) holder).cb_choose_people.setChecked(isItemChecked(position));
            //checkBox的监听
            ((MyHolder) holder).cb_choose_people.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isItemChecked(position)) {
                        setItemChecked(position, false);
                    } else {
                        setItemChecked(position, true);
                    }
                }
            });

            //条目view的监听
            ((MyHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isItemChecked(position)) {
                        setItemChecked(position, false);
                    } else {
                        setItemChecked(position, true);
                    }
                    notifyItemChanged(position);
                }
            });
        }
    }

    /**
     * 获取子项总数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 通过holder的方式来初始化每一个ChildView的内容
     */
    class MyHolder extends RecyclerView.ViewHolder {

        //多选框
        CheckBox cb_choose_people;
        CircleImageView civ_user_select_photo;
        TextView tv_user_select_name;

        public MyHolder(View itemView) {
            super(itemView);
            cb_choose_people = (CheckBox) itemView.findViewById(R.id.cb_choose_people);
            civ_user_select_photo = (CircleImageView) itemView.findViewById(R.id.civ_user_select_photo);
            tv_user_select_name = (TextView) itemView.findViewById(R.id.tv_user_select_name);
        }
    }

    //更新adpter的数据和选择状态
    public void updateDataSet(ArrayList<FriendListResult.ResponseObjectBean> list) {
        this.list = list;
        sparseBooleanArray = new SparseBooleanArray();
    }


    //获得选中条目的结果
    public ArrayList<FriendListResult.ResponseObjectBean> getSelectedItem() {
        ArrayList<FriendListResult.ResponseObjectBean> selectList = new ArrayList<FriendListResult.ResponseObjectBean>();
        for (int i = 0; i < list.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(list.get(i));
            }
        }
        return selectList;
    }

    //设置给定位置条目的选择状态
    private void setItemChecked(int position, boolean isChecked) {
        sparseBooleanArray.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    private boolean isItemChecked(int position) {
        return sparseBooleanArray.get(position);
    }

    //根据位置判断条目是否可选
    private boolean isSelectable() {
        return mIsSelectable;
    }

    //设置给定位置条目的可选与否的状态
    private void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }

}
