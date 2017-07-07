package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.T;
import com.rss.bean.RSSItemBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RssListAdapter extends RecyclerView.Adapter<RssListAdapter.MyViewHolder> {
    private Context mContext;
    List<RSSItemBean> rssList;
    private LayoutInflater layoutInflater;


    public RssListAdapter(Context mContext, List<RSSItemBean> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rss_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.item_na_name.setText(rssList.get(position).getTitle());
        holder.item_na_type.setText(rssList.get(position).getType());
        holder.item_na_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rssList.get(position).getPubDate()) + (TextUtils.isEmpty(rssList.get(position).getAuthor()) ? "" : (" by " + rssList.get(position).getAuthor())));
        holder.item_na_summery.setText(rssList.get(position).getDescription());

        if (rssList.get(position).getImages() != null && rssList.get(position).getImages().size() > 0) {
            holder.item_na_img.setVisibility(View.VISIBLE);
            HttpLoadImg.loadImg(mContext, rssList.get(position).getImages().get(0), holder.item_na_img);
        } else {
            holder.item_na_img.setVisibility(View.GONE);
        }

        holder.item_na_layout.setOnClickListener(v -> {
            String data = rssList.get(position).getLink();//获取编辑框里面的文本内容
            if (!TextUtils.isEmpty(data)) {
                Intent intent = new Intent();//创建Intent对象
                intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                intent.setData(Uri.parse(data));//为Intent设置数据
                mContext.startActivity(intent);//将Intent传递给Activity
            } else {
                T.ShowToast(mContext, "Sorry！");
            }
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_na_layout;
        TextView item_na_name;
        TextView item_na_time;
        TextView item_na_type;
        TextView item_na_summery;
        ImageView item_na_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_na_layout = (LinearLayout) itemView.findViewById(R.id.item_na_layout);
            item_na_name = (TextView) itemView.findViewById(R.id.item_na_title);
            item_na_summery = (TextView) itemView.findViewById(R.id.item_na_summery);
            item_na_time = (TextView) itemView.findViewById(R.id.item_na_time);
            item_na_type = (TextView) itemView.findViewById(R.id.item_na_type);
            item_na_img = (ImageView) itemView.findViewById(R.id.item_na_img);
        }
    }
}
