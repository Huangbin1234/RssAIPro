package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.fragment.SubscriptionFragment;
import com.hb.rssai.view.subscription.OfflineListActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RssSourceAdapter extends RecyclerView.Adapter<RssSourceAdapter.MyViewHolder> {
    private Context mContext;
    List<ResFindMore.RetObjBean.RowsBean> rssList;
    private LayoutInflater layoutInflater;
    private SubscriptionFragment fragment;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface onItemLongClickedListener {
        void onItemLongClicked(ResFindMore.RetObjBean.RowsBean rowsBean);
    }

    public RssSourceAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> rssList, Fragment fragment) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
        this.fragment = (SubscriptionFragment) fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rss_source, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.irs_tv_name.setText(rssList.get(position).getName());
        try {
            holder.irs_tv_count.setText(TextUtils.isEmpty(rssList.get(position).getLastTime()) ? "" : DateUtil.showDate(sdf.parse(rssList.get(position).getLastTime()), longDatePat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(rssList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_no_image, holder.irs_iv_logo);
        } else {
            HttpLoadImg.loadImg(mContext, rssList.get(position).getImg(), holder.irs_iv_logo);
        }

        holder.v.setOnClickListener(v -> {
            boolean isOffline = SharedPreferencesUtil.getBoolean(mContext, Constant.KEY_IS_OFFLINE_MODE, false);
            if (isOffline) {
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
                mContext.startActivity(intent);
            }
        });
        holder.v.setOnLongClickListener(v -> {
            if (null != rssList && null != rssList.get(position))
                fragment.onItemLongClicked(rssList.get(position));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        LinearLayout irs_layout;
        TextView irs_tv_name;
        TextView irs_tv_count;
        ImageView irs_iv_logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            irs_layout = (LinearLayout) itemView.findViewById(R.id.irs_layout);
            irs_tv_count = (TextView) itemView.findViewById(R.id.irs_tv_count);
            irs_tv_name = (TextView) itemView.findViewById(R.id.irs_tv_name);
            irs_iv_logo = (ImageView) itemView.findViewById(R.id.irs_iv_logo);
        }
    }
}
