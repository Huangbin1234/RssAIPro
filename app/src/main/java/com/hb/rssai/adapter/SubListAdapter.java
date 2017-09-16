package com.hb.rssai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.view.subscription.SourceListActivity;
import com.hb.rssai.view.subscription.SubListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.MyViewHolder> {
    private Context mContext;
    List<ResFindMore.RetObjBean.RowsBean> rssList;
    //    List<RssSource> rssList;
    private LayoutInflater layoutInflater;
    private SubListActivity activity;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface onItemLongClickedListener {
        void onItemLongClicked(ResFindMore.RetObjBean.RowsBean rssSource);
    }

    public SubListAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> rssList, Activity activity) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
        this.activity = (SubListActivity) activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_sub_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        System.out.println(rssList.get(position).getName());
        holder.item_sla_tv_name.setText(rssList.get(position).getName());


        try {
            holder.item_sla_tv_count.setText("[" + rssList.get(position).getCount() + "条]");
            holder.item_sla_tv_date.setText(TextUtils.isEmpty(rssList.get(position).getLastTime()) ? "" : DateUtil.showDate(sdf.parse(rssList.get(position).getLastTime()), longDatePat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(rssList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_error, holder.item_sla_iv);
        } else {
            HttpLoadImg.loadRoundImg(mContext, rssList.get(position).getImg(), holder.item_sla_iv);
        }

        holder.v.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, SourceListActivity.class);
//            intent.putExtra(SourceListActivity.KEY_LINK, rssList.get(position).getLink());
//            intent.putExtra(SourceListActivity.KEY_TITLE, rssList.get(position).getName());
//            mContext.startActivity(intent);
            Intent intent = new Intent(mContext, SourceListActivity.class);
            intent.putExtra(SourceListActivity.KEY_LINK, rssList.get(position).getLink());
            intent.putExtra(SourceListActivity.KEY_TITLE, rssList.get(position).getName());
            intent.putExtra(SourceListActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getId());
            mContext.startActivity(intent);
        });
        holder.v.setOnLongClickListener(v -> {
            activity.onItemLongClicked(rssList.get(position));
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        TextView item_sla_tv_name;
        TextView item_sla_tv_count;
        TextView item_sla_tv_date;
        ImageView item_sla_iv;


        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            item_sla_tv_count = (TextView) itemView.findViewById(R.id.item_sla_tv_count);
            item_sla_tv_name = (TextView) itemView.findViewById(R.id.item_sla_tv_name);
            item_sla_iv = (ImageView) itemView.findViewById(R.id.item_sla_iv);
            item_sla_tv_date = (TextView) itemView.findViewById(R.id.item_sla_tv_date);
        }
    }
}
