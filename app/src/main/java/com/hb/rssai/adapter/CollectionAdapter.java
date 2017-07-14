package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.bean.UserCollection;
import com.hb.rssai.view.common.ContentActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    private Context mContext;
    List<UserCollection> rssList;
    private LayoutInflater layoutInflater;

    public CollectionAdapter(Context mContext, List<UserCollection> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_collection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.cs_tv_name.setText(rssList.get(position).getTitle());
        holder.cs_tv_link.setText(rssList.get(position).getLink());
        holder.cs_tv_time.setText(rssList.get(position).getTime());

        holder.cs_layout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ContentActivity.class);
            intent.putExtra(ContentActivity.KEY_URL, rssList.get(position).getLink());
            intent.putExtra(ContentActivity.KEY_TITLE, rssList.get(position).getTitle());
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cs_layout;
        TextView cs_tv_name;
        TextView cs_tv_link;
        TextView cs_tv_time;


        public MyViewHolder(View itemView) {
            super(itemView);
            cs_layout = (LinearLayout) itemView.findViewById(R.id.cs_layout);
            cs_tv_link = (TextView) itemView.findViewById(R.id.cs_tv_link);
            cs_tv_name = (TextView) itemView.findViewById(R.id.cs_tv_name);
            cs_tv_time = (TextView) itemView.findViewById(R.id.cs_tv_time);
        }
    }
}
