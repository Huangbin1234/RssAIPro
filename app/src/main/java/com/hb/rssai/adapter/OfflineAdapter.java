package com.hb.rssai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.bean.ResDataGroup;
import com.hb.rssai.util.HttpLoadImg;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class OfflineAdapter extends BaseAdapter {
    private List<ResDataGroup.RetObjBean.RowsBean> list;
    private Context mContext;
    private LayoutInflater inflater;
    public static HashMap<Integer, Boolean> isSelectMap;

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelectMap;
    }

    public OfflineAdapter(Context context, List<ResDataGroup.RetObjBean.RowsBean> list) {
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        isSelectMap = new HashMap<>();
        init();
    }

    private void init() {
        int len = list.size();
        for (int i = 0; i < len; i++) {
            OfflineAdapter.getIsSelected().put(i, false);
        }
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_offiline_item, null);
            holder.dialog_item_iv = convertView.findViewById(R.id.dialog_item_iv);
            holder.dialog_item_tv = convertView.findViewById(R.id.dialog_item_tv);
            holder.dialog_item_chk = convertView.findViewById(R.id.dialog_item_chk);
            holder.progressBar = convertView.findViewById(R.id.progressBar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getIsSelected() != null && getIsSelected().containsKey(position)) {
            holder.dialog_item_chk.setChecked(getIsSelected().get(position));
        }
        holder.progressBar.setMax(list.get(position).getMaxVal());
        holder.progressBar.setProgress(list.get(position).getProgressVal());
        holder.dialog_item_tv.setText(list.get(position).getName());
        HttpLoadImg.loadImg(mContext,  ApiRetrofit.BASE_URL +list.get(position).getUrl(), holder.dialog_item_iv);
        return convertView;
    }

    public class ViewHolder {
        ImageView dialog_item_iv;
        TextView dialog_item_tv;
        public CheckBox dialog_item_chk;
        public ProgressBar progressBar;
    }
}
