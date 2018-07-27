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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResCardSubscribe;
import com.hb.rssai.bean.ResTheme;
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
public class SourceListCardAdapter extends RecyclerView.Adapter<SourceListCardAdapter.MyViewHolder> {
    private Context mContext;
    private List<List<ResCardSubscribe.RetObjBean.RowsBean>> rssList;
    private LayoutInflater layoutInflater;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat sdf = new SimpleDateFormat(longDatePat);
    private String[] images = null;

    public SourceListCardAdapter(Context mContext, List<List<ResCardSubscribe.RetObjBean.RowsBean>> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_resource_list, parent, false);
        return new MyViewHolder(view);
    }

    private void clickItem(int position, int index) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean= rssList.get(position) ;
        if (rssList.size() > 0) {
            if (!TextUtils.isEmpty(rowBean.get(index).getLink())) {
                Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
                intent.putExtra(ContentActivity.KEY_TITLE, rowBean.get(index).getTitle());
                intent.putExtra(ContentActivity.KEY_URL, rowBean.get(index).getLink());
                intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowBean.get(index).getId());
                intent.putExtra("pubDate", rowBean.get(index).getPubTime());
                intent.putExtra("whereFrom", rowBean.get(index).getWhereFrom());
                intent.putExtra("abstractContent", rowBean.get(index).getAbstractContent());
                intent.putExtra("clickGood", rowBean.get(index).getClickGood());
                intent.putExtra("clickNotGood", rowBean.get(index).getClickNotGood());
                intent.putExtra("id", rowBean.get(index).getId());
                mContext.startActivity(intent);//将Intent传递给Activity
            } else {
                T.ShowToast(mContext, "链接错误，无法跳转！");
            }
        } else {
            T.ShowToast(mContext, "请等待数据加载完成！");
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean= rssList.get(position) ;
        int len = rowBean.size();
        if (len >= 1 && rowBean.get(0) != null) {
            try {
                holder.irl_tv_top_time.setText(DateUtil.showDate(sdf.parse(rowBean.get(0).getPubTime()), longDatePat));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.irl_tv_top.setText(rowBean.get(0).getTitle());
            holder.irl_top_rl.setOnClickListener(v -> clickItem(position, 0));
            images = TextUtils.isEmpty(rowBean.get(0).getImageUrls()) ? null : rowBean.get(0).getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                HttpLoadImg.loadImg(mContext, images[0], holder.irl_iv_top);
            } else {
                holder.irl_iv_top.setVisibility(View.GONE);
            }
        } else {
            holder.irl_top_rl.setVisibility(View.GONE);
        }
        if (len >= 2 && rowBean.get(1) != null) {
            holder.irl_tv_top1.setText(rowBean.get(1).getTitle());
            holder.irl_top_ll1.setOnClickListener(v -> clickItem(position, 1));
            images = TextUtils.isEmpty(rowBean.get(1).getImageUrls()) ? null : rowBean.get(1).getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                HttpLoadImg.loadImg(mContext, images[0], holder.irl_iv_top1);
            } else {
                holder.irl_iv_top1.setVisibility(View.GONE);
            }
        } else {
            holder.irl_top_ll1.setVisibility(View.GONE);
        }
        if (len >= 3 && rowBean.get(2) != null) {
            holder.irl_tv_top2.setText(rowBean.get(2).getTitle());
            holder.irl_top_ll2.setOnClickListener(v -> clickItem(position, 2));
            images = TextUtils.isEmpty(rowBean.get(2).getImageUrls()) ? null : rowBean.get(2).getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                HttpLoadImg.loadImg(mContext, images[0], holder.irl_iv_top2);
            } else {
                holder.irl_iv_top2.setVisibility(View.GONE);
            }
        } else {
            holder.irl_top_ll2.setVisibility(View.GONE);
        }
        if (len >= 4 && rowBean.get(3) != null) {
            holder.irl_tv_top3.setText(rowBean.get(3).getTitle());
            holder.irl_top_ll3.setOnClickListener(v -> clickItem(position, 3));
            images = TextUtils.isEmpty(rowBean.get(3).getImageUrls()) ? null : rowBean.get(3).getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                HttpLoadImg.loadImg(mContext, images[0], holder.irl_iv_top3);
            } else {
                holder.irl_iv_top3.setVisibility(View.GONE);
            }
        } else {
            holder.irl_top_ll3.setVisibility(View.GONE);
        }
        if (len >= 5 && rowBean.get(4) != null) {
            holder.irl_tv_top4.setText(rowBean.get(4).getTitle());
            holder.irl_top_ll4.setOnClickListener(v -> clickItem(position, 4));
            images = TextUtils.isEmpty(rowBean.get(4).getImageUrls()) ? null : rowBean.get(4).getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                HttpLoadImg.loadImg(mContext, images[0], holder.irl_iv_top4);
            } else {
                holder.irl_iv_top4.setVisibility(View.GONE);
            }
        } else {
            holder.irl_top_ll4.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView irl_tv_top4;
        TextView irl_tv_top3;
        TextView irl_tv_top2;
        TextView irl_tv_top1;
        TextView irl_tv_top;
        TextView irl_tv_top_time;

        ImageView irl_iv_top4;
        ImageView irl_iv_top3;
        ImageView irl_iv_top2;
        ImageView irl_iv_top1;
        ImageView irl_iv_top;

        View irl_view_line_2;
        View irl_view_line_3;
        View irl_view_line_4;

        RelativeLayout irl_top_rl;
        LinearLayout irl_top_ll4;
        LinearLayout irl_top_ll3;
        LinearLayout irl_top_ll2;
        LinearLayout irl_top_ll1;
        View v;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            irl_tv_top4 = (TextView) itemView.findViewById(R.id.irl_tv_top4);
            irl_tv_top3 = (TextView) itemView.findViewById(R.id.irl_tv_top3);
            irl_tv_top2 = (TextView) itemView.findViewById(R.id.irl_tv_top2);
            irl_tv_top1 = (TextView) itemView.findViewById(R.id.irl_tv_top1);
            irl_tv_top = (TextView) itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = (TextView) itemView.findViewById(R.id.irl_tv_top_time);

            irl_iv_top4 = (ImageView) itemView.findViewById(R.id.irl_iv_top4);
            irl_iv_top3 = (ImageView) itemView.findViewById(R.id.irl_iv_top3);
            irl_iv_top2 = (ImageView) itemView.findViewById(R.id.irl_iv_top2);
            irl_iv_top1 = (ImageView) itemView.findViewById(R.id.irl_iv_top1);
            irl_iv_top = (ImageView) itemView.findViewById(R.id.irl_iv_top);

            irl_view_line_2 = (View) itemView.findViewById(R.id.irl_view_line_2);
            irl_view_line_3 = (View) itemView.findViewById(R.id.irl_view_line_3);
            irl_view_line_4 = (View) itemView.findViewById(R.id.irl_view_line_4);

            irl_top_rl = (RelativeLayout) itemView.findViewById(R.id.irl_top_rl);
            irl_top_ll4 = (LinearLayout) itemView.findViewById(R.id.irl_top_ll4);
            irl_top_ll3 = (LinearLayout) itemView.findViewById(R.id.irl_top_ll3);
            irl_top_ll2 = (LinearLayout) itemView.findViewById(R.id.irl_top_ll2);
            irl_top_ll1 = (LinearLayout) itemView.findViewById(R.id.irl_top_ll1);
        }
    }
}
