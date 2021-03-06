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
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class FindMoreAdapter extends RecyclerView.Adapter<FindMoreAdapter.MyViewHolder> {
    private Context mContext;
    private List<ResFindMore.RetObjBean.RowsBean> resList;
    private LayoutInflater layoutInflater;
    private OnItemClickedListener onItemClickedListener;
    private OnAddClickedListener onAddClickedListener;
    private String userId = "";

    public void setOnAddClickedListener(OnAddClickedListener onAddClickedListener) {
        this.onAddClickedListener = onAddClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(View itemView,ResFindMore.RetObjBean.RowsBean rowsBean);
    }

    public interface OnAddClickedListener {
        void onAdd(ResFindMore.RetObjBean.RowsBean rowsBean, View v);
    }

    public FindMoreAdapter(Context mContext, List<ResFindMore.RetObjBean.RowsBean> resList) {
        this.mContext = mContext;
        this.resList = resList;
        layoutInflater = LayoutInflater.from(mContext);
        init();
    }

    public void init() {
        userId = SharedPreferencesUtil.getString(mContext, Constant.USER_ID, "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_find_more, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(resList.get(position).getImg())) {
            HttpLoadImg.loadImg(mContext, resList.get(position).getImg(), holder.ifm_iv_img);
        } else {
            HttpLoadImg.loadImg(mContext, R.mipmap.ic_no_image, holder.ifm_iv_img);
        }
        holder.ifm_tv_people.setText("" + resList.get(position).getCount());
        holder.ifm_tv_abstract.setText(resList.get(position).getAbstractContent());
        holder.ifm_tv_title.setText(resList.get(position).getName());
//        1、如果被删除 true
//        那么高亮
//        3、如果没删除fasle ，
//          userId 是空:
//                那么高亮
//          userId 不是空:
//               如果userId等于当前登录的用户ID 那么置灰
//               否则 userId不等于当前登录的用户ID 那么高亮
        if (!resList.get(position).isCheck()) {
            holder.ifm_iv_add.setImageResource(R.mipmap.ic_subscribe_add);
        } else {
            holder.ifm_iv_add.setImageResource(R.mipmap.ic_subscribe_cancel);
        }
        holder.ifm_iv_add.setOnClickListener(v -> {
            onAddClickedListener.onAdd(resList.get(position), v);
        });
        holder.v.setOnClickListener(v -> onItemClickedListener.onItemClicked(v,resList.get(position)));
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
        ImageView ifm_iv_add;
        TextView ifm_tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ifm_tv_people = itemView.findViewById(R.id.ifm_tv_people);
            ifm_tv_abstract = itemView.findViewById(R.id.ifm_tv_abstract);
            ifm_tv_title = itemView.findViewById(R.id.ifm_tv_title);
            ifm_iv_img = itemView.findViewById(R.id.ifm_iv_img);
            ifm_iv_add = itemView.findViewById(R.id.ifm_iv_add);
        }
    }
}
