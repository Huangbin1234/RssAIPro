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
import com.hb.rssai.bean.ResTheme;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {
    private Context mContext;
    List<ResTheme.RetObjBean.RowsBean> themeList;
    private LayoutInflater layoutInflater;
    private OnItemClickedListener itemClickedListener;

    public void setItemClickedListener(OnItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }
    public interface OnItemClickedListener {
        void onClick(ResTheme.RetObjBean.RowsBean rowsBean, View v);
    }
    public ThemeAdapter(Context mContext, List<ResTheme.RetObjBean.RowsBean> themeList) {
        this.mContext = mContext;
        this.themeList = themeList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_theme, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ir_tv_name.setText(themeList.get(position).getName());
        if (!TextUtils.isEmpty(themeList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, themeList.get(position).getImg(), holder.ir_iv_logo);
        } else {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_place, holder.ir_iv_logo);
        }
        holder.ir_tv_name.setText(themeList.get(position).getName());
        holder.ir_tv_abs.setText(themeList.get(position).getMark());
        holder.v.setOnClickListener(v -> {
            //TODO
            itemClickedListener.onClick(themeList.get(position),v);
        });
    }


    @Override
    public int getItemCount() {
        return themeList == null ? 0 : themeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        TextView ir_tv_name;
        TextView ir_tv_abs;
        ImageView ir_iv_logo;
        ImageView ir_iv_add;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ir_tv_name = itemView.findViewById(R.id.ir_tv_name);
            ir_iv_logo = itemView.findViewById(R.id.ir_iv_logo);
            ir_iv_add = itemView.findViewById(R.id.ir_iv_add);
            ir_tv_abs = itemView.findViewById(R.id.ir_tv_abs);
        }
    }
}
