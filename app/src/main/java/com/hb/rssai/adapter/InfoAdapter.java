package com.hb.rssai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
    private Context mContext;
    private List<ResInformation.RetObjBean.RowsBean> rssList;
    private LayoutInflater layoutInflater;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String[] images = null;
    private boolean isLoadImage = true;

    public InfoAdapter(Context mContext, List<ResInformation.RetObjBean.RowsBean> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
        init();
    }
    public void init(){
        isLoadImage = SharedPreferencesUtil.getBoolean(mContext, Constant.KEY_IS_LOAD_IMAGE, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_rss_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.item_na_name.setText(rssList.get(position).getTitle()!=null?rssList.get(position).getTitle():"");
        images = TextUtils.isEmpty(rssList.get(position).getImageUrls()) ? null : rssList.get(position).getImageUrls().split(",");
        if (images != null && images.length > 0) {
            if (images.length >= 3) {

                holder.item_na_img.setVisibility(View.GONE);
                holder.irl_bottom_a.setVisibility(View.GONE);
                holder.irl_bottom_b.setVisibility(View.VISIBLE);

                holder.item_na_type_b.setText(rssList.get(position).getWhereFrom());
                try {
                    holder.item_na_time_b.setText(DateUtil.showDate(sdf.parse(rssList.get(position).getPubTime()), longDatePat));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (isLoadImage) {
                    holder.item_na_group_ll.setVisibility(View.GONE);
                } else {
                    holder.item_na_group_ll.setVisibility(View.VISIBLE);
                    HttpLoadImg.loadImg(mContext, images[0], holder.item_na_group_a);
                    HttpLoadImg.loadImg(mContext, images[1], holder.item_na_group_b);
                    HttpLoadImg.loadImg(mContext, images[2], holder.item_na_group_c);
                }
            } else {
                holder.item_na_group_ll.setVisibility(View.GONE);

                holder.irl_bottom_a.setVisibility(View.VISIBLE);
                holder.irl_bottom_b.setVisibility(View.GONE);

                holder.item_na_type_a.setText(rssList.get(position).getWhereFrom());
                try {
                    holder.item_na_time_a.setText(DateUtil.showDate(sdf.parse(rssList.get(position).getPubTime()), longDatePat));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (isLoadImage) {
                    holder.item_na_img.setVisibility(View.GONE);
                } else {

                    holder.item_na_img.setVisibility(View.VISIBLE);
                    HttpLoadImg.loadImg(mContext, images[0], holder.item_na_img);
                }

            }
        } else {
            holder.item_na_group_ll.setVisibility(View.GONE);
            holder.item_na_img.setVisibility(View.GONE);
            holder.irl_bottom_a.setVisibility(View.VISIBLE);
            holder.irl_bottom_b.setVisibility(View.GONE);

            holder.item_na_type_a.setText(rssList.get(position).getWhereFrom());
            try {
                holder.item_na_time_a.setText(DateUtil.showDate(sdf.parse(rssList.get(position).getPubTime()), longDatePat));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.item_na_layout.setOnClickListener(v -> {
            if(rssList.size()>0){
                String data = rssList.get(position).getLink();//获取编辑框里面的文本内容
                if (!TextUtils.isEmpty(data)) {
                    Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
                    intent.putExtra(ContentActivity.KEY_TITLE, rssList.get(position).getTitle());
                    intent.putExtra(ContentActivity.KEY_URL, rssList.get(position).getLink());
                    intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rssList.get(position).getId());
                    intent.putExtra("pubDate", rssList.get(position).getPubTime());
                    intent.putExtra("whereFrom", rssList.get(position).getWhereFrom());
                    intent.putExtra("abstractContent", rssList.get(position).getAbstractContent());
                    intent.putExtra("clickGood", rssList.get(position).getClickGood());
                    intent.putExtra("clickNotGood", rssList.get(position).getClickNotGood());
                    intent.putExtra("id", rssList.get(position).getId());
                    mContext.startActivity(intent);//将Intent传递给Activity
                } else {
                    T.ShowToast(mContext, "链接错误，无法跳转！");
                }
            }else{
                T.ShowToast(mContext, "请等待数据加载完成！");
            }
        });
    }


    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout irl_bottom_b;
        LinearLayout irl_bottom_a;
        LinearLayout item_na_layout;
        LinearLayout item_na_group_ll;

        ImageView item_na_img;
        ImageView item_na_group_a;
        ImageView item_na_group_b;
        ImageView item_na_group_c;

        TextView item_na_name;
        TextView item_na_time_a;
        TextView item_na_type_a;
        TextView item_na_time_b;
        TextView item_na_type_b;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_na_layout = (LinearLayout) itemView.findViewById(R.id.item_na_layout);
            item_na_group_ll = (LinearLayout) itemView.findViewById(R.id.item_na_group_ll);
            irl_bottom_a = (LinearLayout) itemView.findViewById(R.id.irl_bottom_a);
            irl_bottom_b = (LinearLayout) itemView.findViewById(R.id.irl_bottom_b);

            item_na_name = (TextView) itemView.findViewById(R.id.item_na_title);
            item_na_time_a = (TextView) itemView.findViewById(R.id.item_na_time_a);
            item_na_type_a = (TextView) itemView.findViewById(R.id.item_na_type_a);
            item_na_time_b = (TextView) itemView.findViewById(R.id.item_na_time_b);
            item_na_type_b = (TextView) itemView.findViewById(R.id.item_na_type_b);

            item_na_img = (ImageView) itemView.findViewById(R.id.item_na_img);
            item_na_group_a = (ImageView) itemView.findViewById(R.id.item_na_group_a);
            item_na_group_b = (ImageView) itemView.findViewById(R.id.item_na_group_b);
            item_na_group_c = (ImageView) itemView.findViewById(R.id.item_na_group_c);
        }
    }
}
