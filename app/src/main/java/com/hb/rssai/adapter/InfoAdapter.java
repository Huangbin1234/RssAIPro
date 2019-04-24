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
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ResInformation.RetObjBean.RowsBean> rssList;
    private LayoutInflater layoutInflater;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String[] images = null;
    private boolean isNoImageMode = true;

    private static final int TYPE_NO_IMAGE = 1;
    private static final int TYPE_ONE_IMAGE = 2;
    private static final int TYPE_THREE_IMAGE = 3;
    private String title;
    private String time;
    private ResInformation.RetObjBean.RowsBean rowsBean;

    public InfoAdapter(Context mContext, List<ResInformation.RetObjBean.RowsBean> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
        init();
    }

    public void init() {
        isNoImageMode = SharedPreferencesUtil.getBoolean(mContext, Constant.KEY_IS_NO_IMAGE_MODE, false);
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(rssList.get(position).getImageUrls())) {
            return TYPE_NO_IMAGE;
        }
        String[] images = rssList.get(position).getImageUrls().split(",http");
        if (images.length >= 3) {
            return TYPE_THREE_IMAGE;
        } else {
            return TYPE_ONE_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_IMAGE) {
            View view = layoutInflater.inflate(R.layout.include_item_no_image, parent, false);
            return new NoImageViewHolder(view);
        } else if (viewType == TYPE_ONE_IMAGE) {
            View view = layoutInflater.inflate(R.layout.include_item_one_image, parent, false);
            return new OneImageViewHolder(view);
        } else if (viewType == TYPE_THREE_IMAGE) {
            View view = layoutInflater.inflate(R.layout.include_item_three_image, parent, false);
            return new ThreeImageViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        rowsBean = rssList.get(position);
        title = rowsBean.getTitle() != null ? rowsBean.getTitle() : "";
        try {
            time = DateUtil.showDate(sdf.parse(rowsBean.getPubTime()), longDatePat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (holder instanceof NoImageViewHolder) {
            ((NoImageViewHolder) holder).item_na_title.setText(title);
            ((NoImageViewHolder) holder).item_na_time.setText(time);
            ((NoImageViewHolder) holder).item_na_where_from.setText(rowsBean.getWhereFrom());

            ((NoImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        } else if (holder instanceof OneImageViewHolder) {
            ((OneImageViewHolder) holder).item_na_title.setText(title);
            ((OneImageViewHolder) holder).item_na_where_from.setText(rowsBean.getWhereFrom());
            ((OneImageViewHolder) holder).item_na_time.setText(time);
            if (isNoImageMode) {
                ((OneImageViewHolder) holder).item_na_img.setVisibility(View.GONE);
            } else {
                ((OneImageViewHolder) holder).item_na_img.setVisibility(View.VISIBLE);
                images = TextUtils.isEmpty(rowsBean.getImageUrls()) ? null : rowsBean.getImageUrls().split(",http");
                String url = URLDecoder.decode(images[0]);
                //TODO 过滤网址
                HttpLoadImg.loadRoundImg(mContext, filterImage(url), ((OneImageViewHolder) holder).item_na_img);
            }
            ((OneImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        } else if (holder instanceof ThreeImageViewHolder) {
            ((ThreeImageViewHolder) holder).item_na_title.setText(title);
            ((ThreeImageViewHolder) holder).item_na_where_from.setText(rowsBean.getWhereFrom());
            ((ThreeImageViewHolder) holder).item_na_time.setText(time);

            if (isNoImageMode) {
                ((ThreeImageViewHolder) holder).item_na_image_group.setVisibility(View.GONE);
            } else {
                ((ThreeImageViewHolder) holder).item_na_image_group.setVisibility(View.VISIBLE);
                images = TextUtils.isEmpty(rowsBean.getImageUrls()) ? null : rowsBean.getImageUrls().split(",http");
                HttpLoadImg.loadRoundImg(mContext, images[0], ((ThreeImageViewHolder) holder).item_na_image_a);
                HttpLoadImg.loadRoundImg(mContext, "http" + images[1], ((ThreeImageViewHolder) holder).item_na_image_b);
                HttpLoadImg.loadRoundImg(mContext, "http" + images[2], ((ThreeImageViewHolder) holder).item_na_image_c);
            }
            ((ThreeImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        }
    }

    private String filterImage(String url) {
        if (-1 != url.indexOf("image_uri")) {
            String temp = url.substring(url.indexOf("image_uri") + 10);
            return temp.substring(0, temp.indexOf("&#38"));
        } else {
            return url;
        }
    }

    private void click(int position) {
        if (rssList.size() > 0) {
            ResInformation.RetObjBean.RowsBean rowsBean = rssList.get(position);
            String data = rowsBean.getLink();//获取编辑框里面的文本内容
            if (!TextUtils.isEmpty(data)) {
                Intent intent = new Intent(mContext, RichTextActivity.class);//创建Intent对象
                intent.putExtra(ContentActivity.KEY_TITLE, rowsBean.getTitle());
                intent.putExtra(ContentActivity.KEY_URL, rowsBean.getLink());
                intent.putExtra(ContentActivity.KEY_INFORMATION_ID, rowsBean.getId());
                intent.putExtra("pubDate", rowsBean.getPubTime());
                intent.putExtra("whereFrom", rowsBean.getWhereFrom());
                intent.putExtra("abstractContent", rowsBean.getAbstractContent());
                intent.putExtra("clickGood", rowsBean.getClickGood());
                intent.putExtra("clickNotGood", rowsBean.getClickNotGood());
                intent.putExtra("id", rowsBean.getId());
                mContext.startActivity(intent);//将Intent传递给Activity
            } else {
                T.ShowToast(mContext, "链接错误，无法跳转！");
            }
        } else {
            T.ShowToast(mContext, "请等待数据加载完成！");
        }
    }

    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    class NoImageViewHolder extends RecyclerView.ViewHolder {
        TextView item_na_title;
        TextView item_na_where_from;
        TextView item_na_time;
        RelativeLayout item_na_layout;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            item_na_title = itemView.findViewById(R.id.item_na_title);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_time = itemView.findViewById(R.id.item_na_time);

            item_na_layout = itemView.findViewById(R.id.item_na_layout);
        }
    }

    class OneImageViewHolder extends RecyclerView.ViewHolder {
        ImageView item_na_img;
        TextView item_na_title;
        TextView item_na_time;
        TextView item_na_where_from;

        LinearLayout item_na_layout;

        public OneImageViewHolder(View itemView) {
            super(itemView);
            item_na_title = itemView.findViewById(R.id.item_na_title);
            item_na_time = itemView.findViewById(R.id.item_na_time);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_img = itemView.findViewById(R.id.item_na_img);

            item_na_layout = itemView.findViewById(R.id.item_na_layout);
        }
    }

    class ThreeImageViewHolder extends RecyclerView.ViewHolder {
        TextView item_na_where_from;
        TextView item_na_time;
        TextView item_na_title;
        ImageView item_na_image_a;
        ImageView item_na_image_b;
        ImageView item_na_image_c;

        LinearLayout item_na_layout;
        LinearLayout item_na_image_group;

        public ThreeImageViewHolder(View itemView) {
            super(itemView);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_time = itemView.findViewById(R.id.item_na_time);
            item_na_title = itemView.findViewById(R.id.item_na_title);
            item_na_image_a = itemView.findViewById(R.id.item_na_image_a);
            item_na_image_b = itemView.findViewById(R.id.item_na_image_b);
            item_na_image_c = itemView.findViewById(R.id.item_na_image_c);

            item_na_layout = itemView.findViewById(R.id.item_na_layout);
            item_na_image_group = itemView.findViewById(R.id.item_na_image_group);
        }
    }
}
