package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResMessageList;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.me.MessageContentActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context mContext;
    List<ResMessageList.RetObjBean.RowsBean> messages;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context mContext, List<ResMessageList.RetObjBean.RowsBean> mMessages) {
        this.mContext = mContext;
        this.messages = mMessages;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.item_msg_tv_title.setText(messages.get(position).getTitle());
        holder.item_msg_tv_link.setText(messages.get(position).getContent());
        holder.item_msg_tv_time.setText("发布时间："+messages.get(position).getPubTime());
        if (SharedPreferencesUtil.getBoolean(mContext, messages.get(position).getId(), false)) {
            holder.irs_tv_msg_flag.setVisibility(View.GONE);
        } else {
            holder.irs_tv_msg_flag.setVisibility(View.VISIBLE);
        }
        holder.v.setOnClickListener(v -> {
            SharedPreferencesUtil.setBoolean(mContext, messages.get(position).getId(), true);
            notifyItemChanged(position,messages.size());
            Intent intent = new Intent(mContext, MessageContentActivity.class);
            intent.putExtra(MessageContentActivity.KEY_MSG_BEAN, messages.get(position));
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;

        TextView item_msg_tv_time;
        TextView item_msg_tv_link;
        TextView item_msg_tv_title;
        TextView irs_tv_msg_flag;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            irs_tv_msg_flag = (TextView) itemView.findViewById(R.id.irs_tv_msg_flag);
            item_msg_tv_time = (TextView) itemView.findViewById(R.id.item_msg_tv_time);
            item_msg_tv_link = (TextView) itemView.findViewById(R.id.item_msg_tv_link);
            item_msg_tv_title = (TextView) itemView.findViewById(R.id.item_msg_tv_title);
        }
    }
}
