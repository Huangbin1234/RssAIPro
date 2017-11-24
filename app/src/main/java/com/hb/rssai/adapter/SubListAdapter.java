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
    //    List<RssSource> themeList;
    private LayoutInflater layoutInflater;
    private SubListActivity activity;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public OnItemLongClickedListener getOnItemLongClickedListener() {
        return mOnItemLongClickedListener;
    }

    public void setOnItemLongClickedListener(OnItemLongClickedListener onItemLongClickedListener) {
        mOnItemLongClickedListener = onItemLongClickedListener;
    }

    public OnItemLongClickedListener mOnItemLongClickedListener;

    public interface OnItemLongClickedListener {
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
        holder.item_sla_tv_name.setText(rssList.get(position).getName());
        try {
//            holder.item_sla_tv_count.setText("[" + themeList.get(position).getCount() + "条]");
            holder.item_sla_tv_count.setText("最近更新：");
            holder.item_sla_tv_date.setText(TextUtils.isEmpty(rssList.get(position).getLastTime()) ? "" : DateUtil.showDate(sdf.parse(rssList.get(position).getLastTime()), longDatePat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (position < 6) {
            holder.item_sla_iv_top.setImageResource(R.mipmap.ic_top_tag);
//            holder.item_sla_iv_top.setImageResource(R.color.trans);
        } else {
            holder.item_sla_iv_top.setImageResource(R.color.trans);
        }
        if (TextUtils.isEmpty(rssList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_no_image, holder.item_sla_iv);
        } else {
            HttpLoadImg.loadImg(mContext, rssList.get(position).getImg(), holder.item_sla_iv);
        }
//        holder.item_sla_iv_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.ShowToast(mContext, "2222");
//            }
//        });
        holder.item_sla_iv_menu.setOnClickListener(v -> mOnItemLongClickedListener.onItemLongClicked(rssList.get(position)));
        holder.v.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, SourceListActivity.class);
//            intent.putExtra(SourceListActivity.KEY_LINK, themeList.get(position).getLink());
//            intent.putExtra(SourceListActivity.KEY_TITLE, themeList.get(position).getName());
//            mContext.startActivity(intent);
            Intent intent = new Intent(mContext, SourceListActivity.class);
            intent.putExtra(SourceListActivity.KEY_LINK, rssList.get(position).getLink());
            intent.putExtra(SourceListActivity.KEY_TITLE, rssList.get(position).getName());
            intent.putExtra(SourceListActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getId());
            intent.putExtra(SourceListActivity.KEY_IMAGE, rssList.get(position).getImg());
            intent.putExtra(SourceListActivity.KEY_DESC, rssList.get(position).getAbstractContent());
            mContext.startActivity(intent);
        });
//        holder.v.setOnLongClickListener(v -> {
//            activity.onItemLongClicked(themeList.get(position));
//            return true;
//        });
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
        ImageView item_sla_iv_top;
        ImageView item_sla_iv_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            item_sla_tv_count = (TextView) itemView.findViewById(R.id.item_sla_tv_count);
            item_sla_tv_name = (TextView) itemView.findViewById(R.id.item_sla_tv_name);
            item_sla_iv = (ImageView) itemView.findViewById(R.id.item_sla_iv);
            item_sla_tv_date = (TextView) itemView.findViewById(R.id.item_sla_tv_date);

            item_sla_iv_top = (ImageView) itemView.findViewById(R.id.item_sla_iv_top);
            item_sla_iv_menu = (ImageView) itemView.findViewById(R.id.item_sla_iv_menu);
        }
    }
}
