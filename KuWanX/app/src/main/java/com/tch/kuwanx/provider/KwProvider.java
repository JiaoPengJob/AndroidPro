package com.tch.kuwanx.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tch.kuwanx.R;
import com.tch.kuwanx.bean.KwMsg;
import com.tch.kuwanx.message.KwMessage;
import com.tch.kuwanx.ui.exchange.ConfirmActivity;
import com.tch.kuwanx.ui.exchange.ConfirmReceivedActivity;
import com.tch.kuwanx.ui.exchange.EvaluationActivity;
import com.tch.kuwanx.ui.exchange.OtherSubmitActivity;
import com.tch.kuwanx.ui.exchange.PayActivity;
import com.tch.kuwanx.ui.exchange.ReceivedActivity;
import com.tch.kuwanx.ui.exchange.RepayActivity;
import com.tch.kuwanx.ui.exchange.SendActivity;
import com.tch.kuwanx.ui.exchange.SubmitActivity;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by jiaop on 2017/11/24.
 * desc新建一个消息类继承 IContainerItemProvider.MessageProvider 类，实现对应接口方法，
 * 1.注意开头的注解！
 * 2.注意泛型！
 */
@ProviderTag(
        messageContent = KwMessage.class,
        showReadState = true
)
public class KwProvider extends IContainerItemProvider.MessageProvider<KwMessage> {

    public KwProvider() {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kw_msg, null);
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        holder.tvContent = (TextView) view.findViewById(R.id.tvContent);
        holder.tvState = (TextView) view.findViewById(R.id.tvState);
        holder.ivImgUri = (ImageView) view.findViewById(R.id.ivImgUri);
        holder.llKwParent = (LinearLayout) view.findViewById(R.id.llKwParent);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, KwMessage kwMessage, UIMessage uiMessage) {
        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llKwParent.setBackground(view.getContext().getResources().getDrawable(R.drawable.rc_ic_bubble_right));
        } else {
            holder.llKwParent.setBackground(view.getContext().getResources().getDrawable(R.drawable.rc_ic_bubble_left));
        }
        holder.tvTitle.setText(kwMessage.getTitle());
        holder.tvContent.setText(kwMessage.getContent());
        holder.tvState.setText(kwMessage.getState());
        Glide.with(view.getContext())
                .load(kwMessage.getImg_url())
                .into(holder.ivImgUri);
    }

    @Override
    public Spannable getContentSummary(KwMessage kwMessage) {
        return new SpannableString(kwMessage.getContent());
    }

    @Override
    public void onItemClick(View view, int i, KwMessage kwMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int position, KwMessage content, UIMessage message) {
        super.onItemLongClick(view, position, content, message);
    }

    private static class ViewHolder {
        LinearLayout llKwParent;
        TextView tvTitle, tvContent, tvState;
        ImageView ivImgUri;
    }
}
