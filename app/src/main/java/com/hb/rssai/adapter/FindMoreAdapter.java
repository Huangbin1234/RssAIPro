package com.hb.rssai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class FindMoreAdapter extends RecyclerView.Adapter<FindMoreAdapter.MyViewHolder> {
    private Context mContext;
    List<ResFindMore.RetObjBean.RowsBean> resList;
    private LayoutInflater layoutInflater;

    public FindMoreAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> resList) {
        this.mContext = mContext;
        this.resList = resList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_find_more, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.ifm_tv_people.setText("订阅：" + resList.get(position).getCount());
        holder.ifm_tv_abstract.setText(resList.get(position).getAbstractContent());
        holder.ifm_tv_title.setText(resList.get(position).getName());
        HttpLoadImg.loadImg(mContext, resList.get(position).getImg(), holder.ifm_iv_img);
        if (resList.get(position).isIsRecommend()) {
            holder.ifm_iv_add.setBackgroundResource(R.mipmap.ic_no_add);
        } else {
            holder.ifm_iv_add.setBackgroundResource(R.mipmap.ic_add);
        }
    }


    @Override
    public int getItemCount() {
        return resList == null ? 0 : resList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        TextView ifm_tv_people;
//        JustifyTextView ifm_tv_abstract;
        TextView ifm_tv_abstract;
        ImageView ifm_iv_img;
        ImageView ifm_iv_add;
        TextView ifm_tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ifm_tv_people = (TextView) itemView.findViewById(R.id.ifm_tv_people);
//            ifm_tv_abstract = (JustifyTextView) itemView.findViewById(R.id.ifm_tv_abstract);
            ifm_tv_abstract = (TextView) itemView.findViewById(R.id.ifm_tv_abstract);
            ifm_tv_title = (TextView) itemView.findViewById(R.id.ifm_tv_title);
            ifm_iv_img = (ImageView) itemView.findViewById(R.id.ifm_iv_img);
            ifm_iv_add = (ImageView) itemView.findViewById(R.id.ifm_iv_add);
        }
    }

}
