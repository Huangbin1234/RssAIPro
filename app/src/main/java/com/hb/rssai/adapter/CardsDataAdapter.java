package com.hb.rssai.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.RssSource;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;


public class CardsDataAdapter extends BaseAdapter {

    private List<RssSource> list;
    private Context mContext;

    public CardsDataAdapter(Context context, List<RssSource> list) {
        this.list = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public RssSource getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
//        if (null == convertView) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_layout, null); //mContext指的是调用的Activtty
            holder.irs_tv_name = (TextView) convertView.findViewById(R.id.irs_tv_name);
            holder.irs_iv_logo = (ImageView) convertView.findViewById(R.id.irs_iv_logo);
//            convertView.setTag(holder);
//        } else {
//            holder = (Holder) convertView.getTag();
//        }
        holder.irs_tv_name.setText(list.get(position).getName());
        if (TextUtils.isEmpty(list.get(position).getImgUrl())) {
            HttpLoadImg.loadCircleImg(mContext, R.mipmap.ic_no_sub, holder.irs_iv_logo);
        } else {
            HttpLoadImg.loadCircleImg(mContext, list.get(position).getImgUrl(), holder.irs_iv_logo);
        }
        return convertView;
    }

    static class Holder {
        public TextView irs_tv_name;
        public ImageView irs_iv_logo;
    }
}

