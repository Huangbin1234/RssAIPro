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
import com.hb.rssai.view.subscription.SourceListActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RssSourceAdapter extends RecyclerView.Adapter<RssSourceAdapter.MyViewHolder> {
    private Context mContext;
    List<RssSource> rssList;
    private LayoutInflater layoutInflater;

    public RssSourceAdapter(Context mContext, List<RssSource> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rss_source, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.irs_tv_name.setText(rssList.get(position).getName());
        holder.irs_tv_link.setText(rssList.get(position).getLink());
        holder.irs_tv_count.setText("29");

        holder.irs_layout.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, SourceListActivity.class);
            intent.putExtra(SourceListActivity.KEY_LINK, rssList.get(position).getLink());
            intent.putExtra(SourceListActivity.KEY_TITLE, rssList.get(position).getName());
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout irs_layout;
        TextView irs_tv_name;
        TextView irs_tv_link;
        TextView irs_tv_count;


        public MyViewHolder(View itemView) {
            super(itemView);
            irs_layout = (LinearLayout) itemView.findViewById(R.id.irs_layout);

            irs_tv_count = (TextView) itemView.findViewById(R.id.irs_tv_count);
            irs_tv_link = (TextView) itemView.findViewById(R.id.irs_tv_link);
            irs_tv_name = (TextView) itemView.findViewById(R.id.irs_tv_name);
        }
    }
}
