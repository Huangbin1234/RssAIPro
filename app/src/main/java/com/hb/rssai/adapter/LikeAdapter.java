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
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {
    private Context mContext;
    List<ResInformation.RetObjBean.RowsBean> resList;
    private LayoutInflater layoutInflater;
    private OnItemClickedListener onItemClickedListener;
    private OnAddClickedListener onAddClickedListener;

    public void setOnAddClickedListener(OnAddClickedListener onAddClickedListener) {
        this.onAddClickedListener = onAddClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(ResInformation.RetObjBean.RowsBean rowsBean);
    }

    public interface OnAddClickedListener {
        void onAdd(ResInformation.RetObjBean.RowsBean rowsBean);
    }

    public LikeAdapter(Context mContext, List<ResInformation.RetObjBean.RowsBean> resList) {
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
        holder.ifm_tv_people.setText("阅读：" + resList.get(position).getCount());
        holder.ifm_tv_abstract.setText(resList.get(position).getAbstractContent());
        holder.ifm_tv_title.setText(resList.get(position).getTitle());
        if (!TextUtils.isEmpty(resList.get(position).getImageUrls())) {
            HttpLoadImg.loadRoundImg(mContext, resList.get(position).getImageUrls().split(",")[0], holder.ifm_iv_img);
        }
        holder.v.setOnClickListener(v -> onItemClickedListener.onItemClicked(resList.get(position)));
    }

    @Override
    public int getItemCount() {
        return resList == null ? 0 : resList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        TextView ifm_tv_people;
        TextView ifm_tv_abstract;
        ImageView ifm_iv_img;
        TextView ifm_tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ifm_tv_people = (TextView) itemView.findViewById(R.id.ifm_tv_people);
            ifm_tv_abstract = (TextView) itemView.findViewById(R.id.ifm_tv_abstract);
            ifm_tv_title = (TextView) itemView.findViewById(R.id.ifm_tv_title);
            ifm_iv_img = (ImageView) itemView.findViewById(R.id.ifm_iv_img);
        }
    }

}
