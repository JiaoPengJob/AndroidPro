package com.tch.zx.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tch.zx.R;
import com.tch.zx.http.bean.result.PayRecordResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的动态
 */

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<PayRecordResult.ResponseObjectBean> list;

    public PurchaseHistoryAdapter(Context context, List<PayRecordResult.ResponseObjectBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_purchase_history, parent, false);
        return new PurchaseHistoryAdapter.MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PurchaseHistoryAdapter.MyHolder) {
            ((MyHolder) holder).tvPurchaseHistoryTitle.setText(list.get(position).getTitle());
            ((MyHolder) holder).tvPurchaseHistoryDate.setText(list.get(position).getCreatetime());
            ((MyHolder) holder).tvPurchaseHistoryOrderMoney.setText(list.get(position).getOrderMoney() + "元");
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
        @BindView(R.id.tvPurchaseHistoryTitle)
        TextView tvPurchaseHistoryTitle;
        @BindView(R.id.tvPurchaseHistoryDate)
        TextView tvPurchaseHistoryDate;
        @BindView(R.id.tvPurchaseHistoryOrderMoney)
        TextView tvPurchaseHistoryOrderMoney;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
