package com.hb.rssai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DataTypeAdapter extends BaseAdapter {
    private List<ResDataGroup.RetObjBean.RowsBean> list;
    private Context mContext;
    LayoutInflater inflater;

    public DataTypeAdapter(Context context, List<ResDataGroup.RetObjBean.RowsBean> list) {
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.data_type_item, null);
            holder.dialog_item_iv =  convertView.findViewById(R.id.dialog_item_iv);
            holder.dialog_item_tv =  convertView.findViewById(R.id.dialog_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dialog_item_tv.setText(list.get(position).getName());
        HttpLoadImg.loadImg(mContext, ApiRetrofit.BASE_URL +list.get(position).getUrl(), holder.dialog_item_iv);
        return convertView;
    }

    class ViewHolder {
        ImageView dialog_item_iv;
        TextView dialog_item_tv;
    }
}
