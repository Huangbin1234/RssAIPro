package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.view.subscription.SourceListActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    Context mContext;
    List<RssSource> list;

    public CardAdapter(Context mContext, List<RssSource> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HttpLoadImg.loadImg(mContext,list.get(position).getImgUrl(),holder.avatarImageView);
        holder.tv_name.setText(list.get(position).getName().trim());
        holder.tv_view.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SourceListActivity.class);
            intent.putExtra(SourceListActivity.KEY_LINK, list.get(position).getLink());
            intent.putExtra(SourceListActivity.KEY_TITLE, list.get(position).getName());
            mContext.startActivity(intent);
        });
        holder.tv_count.setText("已有" + list.get(position).getCount()+"条资讯");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImageView;
        TextView tv_count,  tv_name;
        Button tv_view;
        MyViewHolder(View itemView) {
            super(itemView);
            avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_view = (Button) itemView.findViewById(R.id.tv_view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

        }
    }
}