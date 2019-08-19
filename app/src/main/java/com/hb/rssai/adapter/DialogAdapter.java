package com.hb.rssai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.util.HttpLoadImg;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DialogAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> list;
    private Context mContext;
    LayoutInflater inflater;

    public DialogAdapter(Context context, List<HashMap<String, Object>> list) {
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
            convertView = inflater.inflate(R.layout.view_dialog_item, null);
            holder.dialog_item_iv = convertView.findViewById(R.id.dialog_item_iv);
            holder.dialog_item_tv = convertView.findViewById(R.id.dialog_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dialog_item_tv.setText(list.get(position).get("name").toString());
        HttpLoadImg.loadCircleImg(mContext, Integer.valueOf(list.get(position).get("url").toString()), holder.dialog_item_iv);
        return convertView;
    }

    class ViewHolder {
        ImageView dialog_item_iv;
        TextView dialog_item_tv;
    }
}
