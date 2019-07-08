package com.hb.rssai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.subscription.OfflineListActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;
import com.hb.rssai.view.subscription.SubscribeAllActivity;

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
    private SubscribeAllActivity activity;
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
        this.activity = (SubscribeAllActivity) activity;
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
            boolean isOffline = SharedPreferencesUtil.getBoolean(mContext, Constant.KEY_IS_OFFLINE_MODE, false);
            if (isOffline || rssList.get(position).isIsTag() == true) {
                Intent intent = new Intent(mContext, OfflineListActivity.class);
                intent.putExtra(OfflineListActivity.KEY_LINK, rssList.get(position).getLink());
                intent.putExtra(OfflineListActivity.KEY_NAME, rssList.get(position).getName());
                intent.putExtra(OfflineListActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getId());
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, SourceCardActivity.class);
                intent.putExtra(SourceCardActivity.KEY_LINK, rssList.get(position).getLink());
                intent.putExtra(SourceCardActivity.KEY_TITLE, rssList.get(position).getName());
                intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getId());
                intent.putExtra(SourceCardActivity.KEY_IMAGE, rssList.get(position).getImg());
                intent.putExtra(SourceCardActivity.KEY_DESC, rssList.get(position).getAbstractContent());
                intent.putExtra(SourceCardActivity.KEY_IS_CHECK, true);


                Pair<View, String> pImg = Pair.create(holder.item_sla_iv, "img");
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pImg);
                mContext.startActivity(intent, compat.toBundle());

//                mContext.startActivity(intent);
            }
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
