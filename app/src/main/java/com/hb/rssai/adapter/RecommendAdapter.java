package com.hb.rssai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResFindMore;
import com.hb.rssai.util.HttpLoadImg;

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


    public RecommendAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_recommend, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ir_tv_name.setText(rssList.get(position).getName());
        if (TextUtils.isEmpty(rssList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_error, holder.ir_iv_logo);
        } else {
            HttpLoadImg.loadImg(mContext, rssList.get(position).getImg(), holder.ir_iv_logo);
        }
        holder.v.setOnClickListener(v -> {

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

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ir_tv_name = (TextView) itemView.findViewById(R.id.ir_tv_name);
            ir_iv_logo = (ImageView) itemView.findViewById(R.id.ir_iv_logo);
        }
    }
}
