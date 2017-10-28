package com.hb.rssai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResUserInformation;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {
    private Context mContext;
    List<ResUserInformation.RetObjBean.RowsBean> userInformations;
    private LayoutInflater layoutInflater;

    private MyOnItemClickedListener myOnItemClickedListener;

    public RecordAdapter.MyOnItemClickedListener getMyOnItemClickedListener() {
        return myOnItemClickedListener;
    }

    public void setMyOnItemClickedListener(RecordAdapter.MyOnItemClickedListener myOnItemClickedListener) {
        this.myOnItemClickedListener = myOnItemClickedListener;
    }

    public interface MyOnItemClickedListener {
        void onItemClicked(ResUserInformation.RetObjBean.RowsBean rowsBean);
    }
    public RecordAdapter(Context mContext, List<ResUserInformation.RetObjBean.RowsBean> mMessages) {
        this.mContext = mContext;
        this.userInformations = mMessages;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_record, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.item_msg_tv_title.setText(userInformations.get(position).getInformationTitle());
        holder.item_msg_tv_link.setText(userInformations.get(position).getInformationLink());
        holder.item_msg_tv_time.setText("阅读时间："+ userInformations.get(position).getOprTime());

        holder.v.setOnClickListener(v -> {
            //TODO
            myOnItemClickedListener.onItemClicked(userInformations.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return userInformations == null ? 0 : userInformations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;

        TextView item_msg_tv_time;
        TextView item_msg_tv_link;
        TextView item_msg_tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            item_msg_tv_time = (TextView) itemView.findViewById(R.id.item_msg_tv_time);
            item_msg_tv_link = (TextView) itemView.findViewById(R.id.item_msg_tv_link);
            item_msg_tv_title = (TextView) itemView.findViewById(R.id.item_msg_tv_title);
        }
    }
}
