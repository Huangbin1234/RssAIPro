package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResCollection;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.me.CollectionActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    private Context mContext;
    List<ResCollection.RetObjBean.RowsBean> collections;
    private LayoutInflater layoutInflater;

    private MyOnItemClickedListener myOnItemClickedListener;

    public MyOnItemClickedListener getMyOnItemClickedListener() {
        return myOnItemClickedListener;
    }

    public void setMyOnItemClickedListener(MyOnItemClickedListener myOnItemClickedListener) {
        this.myOnItemClickedListener = myOnItemClickedListener;
    }

    public interface MyOnItemClickedListener {
        void onItemClicked(ResCollection.RetObjBean.RowsBean rowsBean);
    }

    public interface onItemLongClickedListener {
        void onItemLongClicked(ResCollection.RetObjBean.RowsBean rowsBean);
    }

    public CollectionAdapter(Context mContext, List<ResCollection.RetObjBean.RowsBean> collections) {
        this.mContext = mContext;
        this.collections = collections;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_collection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.cs_tv_name.setText(collections.get(position).getTitle());
        holder.cs_tv_link.setText(collections.get(position).getLink());
        holder.cs_tv_time.setText(collections.get(position).getCreateTime());

        holder.v.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, ContentActivity.class);
//            intent.putExtra(ContentActivity.KEY_URL, collections.get(position).getLink());
//            intent.putExtra(ContentActivity.KEY_TITLE, collections.get(position).getTitle());
//            intent.putExtra(ContentActivity.KEY_INFORMATION_ID, collections.get(position).getInformationId());
//            mContext.startActivity(intent);
            myOnItemClickedListener.onItemClicked(collections.get(position));
        });
        holder.v.setOnLongClickListener(v -> {
            ((CollectionActivity)mContext).onItemLongClicked(collections.get(position));
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return collections == null ? 0 : collections.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;
        LinearLayout cs_layout;
        TextView cs_tv_name;
        TextView cs_tv_link;
        TextView cs_tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            cs_layout = (LinearLayout) itemView.findViewById(R.id.cs_layout);
            cs_tv_link = (TextView) itemView.findViewById(R.id.cs_tv_link);
            cs_tv_name = (TextView) itemView.findViewById(R.id.cs_tv_name);
            cs_tv_time = (TextView) itemView.findViewById(R.id.cs_tv_time);
        }
    }
}
