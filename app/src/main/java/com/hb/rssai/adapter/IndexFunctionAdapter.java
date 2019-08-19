package com.hb.rssai.adapter;

import android.content.Context;
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

/**
 * TODO: 首页功能按钮适配器
 * Created by admin on 2016-06-17.
 */
public class IndexFunctionAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RssSource> mFunctionButtons;

    public IndexFunctionAdapter(Context context, List<RssSource> functionButtons) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mFunctionButtons = functionButtons;
    }

    @Override
    public int getCount() {
        return mFunctionButtons == null ? 0 : mFunctionButtons.size();
    }

    @Override
    public Object getItem(int position) {
        return mFunctionButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            v = mInflater.inflate(R.layout.item_index_function, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.tv.setText(mFunctionButtons.get(position).getName());
        HttpLoadImg.loadImg(mContext, mFunctionButtons.get(position).getImgUrl(), holder.iv);
        return v;
    }

    static class ViewHolder {
        TextView tv;
        ImageView iv;

        public ViewHolder(View itemview) {
            tv = itemview.findViewById(R.id.item_index_function_tv);
            iv = itemview.findViewById(R.id.item_index_function_iv);
        }
    }
}
