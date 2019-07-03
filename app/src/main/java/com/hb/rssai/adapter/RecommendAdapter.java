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
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.view.subscription.OfflineListActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.MyViewHolder> {
    private Context mContext;
    List<ResFindMore.RetObjBean.RowsBean> rssList;
    private LayoutInflater layoutInflater;
    private FindMoreAdapter.OnAddClickedListener onAddClickedListener;
    String userId = "";

    public void setOnAddClickedListener(FindMoreAdapter.OnAddClickedListener onAddClickedListener) {
        this.onAddClickedListener = onAddClickedListener;
    }

    public RecommendAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
        init();
    }

    public void init() {
        userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_recommend, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ir_tv_name.setText(rssList.get(position).getName());
        if (!TextUtils.isEmpty(rssList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, rssList.get(position).getImg(), holder.ir_iv_logo);
        } else {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_no_image, holder.ir_iv_logo);
        }
        if (!rssList.get(position).isCheck()) {
            holder.ir_iv_add.setImageResource(R.mipmap.ic_recommend_add);
        } else {
            holder.ir_iv_add.setImageResource(R.color.trans);
        }
        holder.ir_iv_add.setOnClickListener(v -> onAddClickedListener.onAdd(rssList.get(position), v));
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
                intent.putExtra(SourceCardActivity.KEY_IS_CHECK, rssList.get(position).isCheck());

                Pair<View, String> pImg = Pair.create(holder.ir_iv_logo, "img");
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pImg );
                mContext.startActivity(intent, compat.toBundle());

//                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        TextView ir_tv_name;
        ImageView ir_iv_logo;
        ImageView ir_iv_add;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ir_tv_name = (TextView) itemView.findViewById(R.id.ir_tv_name);
            ir_iv_logo = (ImageView) itemView.findViewById(R.id.ir_iv_logo);
            ir_iv_add = (ImageView) itemView.findViewById(R.id.ir_iv_add);
        }
    }
}
