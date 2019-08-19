package com.hb.rssai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.api.ApiRetrofit;
import com.hb.rssai.bean.ResAdviceList;
import com.hb.rssai.util.HttpLoadImg;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.MyViewHolder> {
    private Context mContext;
    private List<ResAdviceList.RetObjBean.RowsBean> adviceList;
    private LayoutInflater layoutInflater;

    public AdviceAdapter(Context mContext, List<ResAdviceList.RetObjBean.RowsBean> mMessages) {
        this.mContext = mContext;
        this.adviceList = mMessages;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_advice, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_content.setText(TextUtils.isEmpty(adviceList.get(position).getContent()) ? "" : adviceList.get(position).getContent());
        holder.tv_create_time.setText(TextUtils.isEmpty(adviceList.get(position).getCreateTime()) ? "" : adviceList.get(position).getCreateTime());
        holder.tv_user.setText(TextUtils.isEmpty(adviceList.get(position).getNickName()) ? "" : adviceList.get(position).getNickName());
        if (!TextUtils.isEmpty(adviceList.get(position).getMark())) {
            holder.ll_reply.setVisibility(View.VISIBLE);
            holder.tv_mark.setText(TextUtils.isEmpty(adviceList.get(position).getMark()) ? "" : adviceList.get(position).getMark());
            holder.tv_update_time.setText(TextUtils.isEmpty(adviceList.get(position).getUpdateTime()) ? "" : adviceList.get(position).getUpdateTime());
        } else {
            holder.ll_reply.setVisibility(View.GONE);
        }
        holder.tv_type_name.setText(TextUtils.isEmpty(adviceList.get(position).getAdviceTypeName()) ? "" : adviceList.get(position).getAdviceTypeName());
        if (!TextUtils.isEmpty(adviceList.get(position).getAvatar())) {
            HttpLoadImg.loadCircleImg(mContext, ApiRetrofit.BASE_URL + adviceList.get(position).getAvatar(), holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_default_avar);
        }
    }


    @Override
    public int getItemCount() {
        return adviceList == null ? 0 : adviceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;

        TextView tv_content;
        TextView tv_create_time;
        TextView tv_user;
        TextView tv_mark;
        TextView tv_update_time;
        TextView tv_type_name;
        ImageView iv_avatar;
        LinearLayout ll_reply;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_create_time = itemView.findViewById(R.id.tv_create_time);
            tv_user = itemView.findViewById(R.id.tv_user);
            tv_mark = itemView.findViewById(R.id.tv_mark);
            tv_update_time = itemView.findViewById(R.id.tv_update_time);
            tv_type_name = itemView.findViewById(R.id.tv_type_name);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            ll_reply = itemView.findViewById(R.id.ll_reply);

        }
    }
}
