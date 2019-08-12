package com.hb.rssai.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ImageDialogAdapter extends BaseAdapter {
    private List<String> list;
    private Context mContext;
    LayoutInflater inflater;
    //    public static HashMap<Integer, Boolean> isSelecteds;
    public static SparseBooleanArray isSelecteds;

    public static SparseBooleanArray getIsSelected() {
        return isSelecteds;
    }


    public ImageDialogAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        isSelecteds = new SparseBooleanArray();
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
            convertView = inflater.inflate(R.layout.view_image_dialog_item, null);
            holder.dialog_item_iv = (ImageView) convertView.findViewById(R.id.dialog_item_iv);
            holder.dialog_item_tv = (TextView) convertView.findViewById(R.id.dialog_item_tv);
            holder.dialog_item_chk = (CheckBox) convertView.findViewById(R.id.dialog_item_chk);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getIsSelected() != null && -1 != getIsSelected().indexOfKey(position)) {
            holder.dialog_item_chk.setChecked(getIsSelected().get(position));
        }
        holder.dialog_item_tv.setText("图" + position);
        HttpLoadImg.loadCircleImg(mContext, list.get(position), holder.dialog_item_iv);
        return convertView;
    }

    public class ViewHolder {
        ImageView dialog_item_iv;
        TextView dialog_item_tv;
        public CheckBox dialog_item_chk;
    }
}
