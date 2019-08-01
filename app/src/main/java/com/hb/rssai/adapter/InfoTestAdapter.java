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
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;
import com.hb.rssai.view.subscription.SourceCardActivity;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class InfoTestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ResInformation.RetObjBean.RowsBean> rssList;
    private LayoutInflater layoutInflater;
    private String longDatePat = "yyyy-MM-dd HH:mm:ss";

    private String[] images = null;
    private boolean isNoImageMode = true;

    private static final int TYPE_NO_IMAGE = 1;
    private static final int TYPE_ONE_IMAGE = 2;
    private static final int TYPE_THREE_IMAGE = 3;
    private static final int TYPE_FOUR = 4;//分割线
    private String title;

    private ResInformation.RetObjBean.RowsBean rowsBean;

    public InfoTestAdapter(Context mContext, List<ResInformation.RetObjBean.RowsBean> rssList) {
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
        if (null != rssList && null != rssList.get(position) && rssList.get(position).getViewType() == 4) {
            return TYPE_FOUR;
        }
        if (null == rssList || null == rssList.get(position) || TextUtils.isEmpty(rssList.get(position).getImageUrls())) {
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
        } else if (viewType == TYPE_FOUR) {
            View view = layoutInflater.inflate(R.layout.include_item_four, parent, false);
            return new BarViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String time = "";
        rowsBean = rssList.get(position);
        if (null == rowsBean) {
            return;
        }
        title = rowsBean.getTitle() != null ? rowsBean.getTitle() : "";
        try {
            time = DateUtil.showDate(Constant.sdf.parse(rowsBean.getPubTime()), longDatePat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (holder instanceof NoImageViewHolder) {
            ((NoImageViewHolder) holder).item_na_title.setText(title);
            ((NoImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        } else if (holder instanceof OneImageViewHolder) {
            ((OneImageViewHolder) holder).item_na_title.setText(title);
            if (isNoImageMode) {
                ((OneImageViewHolder) holder).item_na_img.setVisibility(View.GONE);
            } else {
                ((OneImageViewHolder) holder).item_na_img.setVisibility(View.VISIBLE);
                images = TextUtils.isEmpty(rowsBean.getImageUrls()) ? null : rowsBean.getImageUrls().split(",http");
                if (null != images && images.length > 0) {
                    System.out.println("==>"+images[0]);
                    String url = URLDecoder.decode(images[0]);
                    //TODO 过滤网址
                    HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((OneImageViewHolder) holder).item_na_img);
                }
            }
            ((OneImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        } else if (holder instanceof ThreeImageViewHolder) {
            ((ThreeImageViewHolder) holder).item_na_title.setText(title);
            if (isNoImageMode) {
                ((ThreeImageViewHolder) holder).item_na_image_group.setVisibility(View.GONE);
            } else {
                ((ThreeImageViewHolder) holder).item_na_image_group.setVisibility(View.VISIBLE);
                images = TextUtils.isEmpty(rowsBean.getImageUrls()) ? null : rowsBean.getImageUrls().split(",http");
                System.out.println("==>"+images[0]);
                System.out.println("==>"+"http" + images[1]);
                System.out.println("==>"+"http" + images[2]);
                HttpLoadImg.loadRoundImg(mContext, images[0], ((ThreeImageViewHolder) holder).item_na_image_a);
                HttpLoadImg.loadRoundImg(mContext, "http" + images[1], ((ThreeImageViewHolder) holder).item_na_image_b);
                HttpLoadImg.loadRoundImg(mContext, "http" + images[2], ((ThreeImageViewHolder) holder).item_na_image_c);
            }
            ((ThreeImageViewHolder) holder).item_na_layout.setOnClickListener(v -> click(position));
        } else if (holder instanceof BarViewHolder) {
            ((BarViewHolder) holder).item_na_where_from.setText(rowsBean.getWhereFrom());
            ((BarViewHolder) holder).item_na_time.setText(time);
            HttpLoadImg.loadCircleImg(mContext, rowsBean.getSubscribeImg(), ((BarViewHolder) holder).item_iv_logo);

            ((BarViewHolder) holder).item_iv_logo.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SourceCardActivity.class);
                intent.putExtra(SourceCardActivity.KEY_TITLE, rssList.get(position).getWhereFrom());
                intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getSubscribeId());
                intent.putExtra(SourceCardActivity.KEY_IMAGE, rssList.get(position).getSubscribeImg());
                mContext.startActivity(intent);
            });
            ((BarViewHolder) holder).item_na_where_from.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SourceCardActivity.class);
                intent.putExtra(SourceCardActivity.KEY_TITLE, rssList.get(position).getWhereFrom());
                intent.putExtra(SourceCardActivity.KEY_SUBSCRIBE_ID, rssList.get(position).getSubscribeId());
                intent.putExtra(SourceCardActivity.KEY_IMAGE, rssList.get(position).getSubscribeImg());
                mContext.startActivity(intent);
            });
        }
    }


    private void click(int position) {
        if (rssList.size() > 0) {
            ResInformation.RetObjBean.RowsBean rowsBean = rssList.get(position);
            String data = rowsBean.getLink();//获取编辑框里面的文本内容
            if (!TextUtils.isEmpty(data) || !TextUtils.isEmpty(rowsBean.getAbstractContent())) {
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
                intent.putExtra("subscribeImg", rowsBean.getSubscribeImg());
                intent.putExtra("count", rowsBean.getCount());
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

    class BarViewHolder extends RecyclerView.ViewHolder {
        TextView item_na_where_from;
        TextView item_na_time;
        ImageView item_iv_logo;

        public BarViewHolder(View itemView) {
            super(itemView);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_time = itemView.findViewById(R.id.item_na_time);
            item_iv_logo = itemView.findViewById(R.id.item_iv_logo);
        }
    }

    class NoImageViewHolder extends RecyclerView.ViewHolder {
        TextView item_na_title;
        TextView item_na_where_from;
        TextView item_na_time;
        RelativeLayout item_na_layout;
        ImageView item_iv_logo;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            item_na_title = itemView.findViewById(R.id.item_na_title);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_time = itemView.findViewById(R.id.item_na_time);
            item_iv_logo = itemView.findViewById(R.id.item_iv_logo);

            item_na_layout = itemView.findViewById(R.id.item_na_layout);
        }
    }

    class OneImageViewHolder extends RecyclerView.ViewHolder {
        ImageView item_na_img;
        TextView item_na_title;
        TextView item_na_time;
        TextView item_na_where_from;
        ImageView item_iv_logo;

        LinearLayout item_na_layout;

        public OneImageViewHolder(View itemView) {
            super(itemView);
            item_na_title = itemView.findViewById(R.id.item_na_title);
            item_na_time = itemView.findViewById(R.id.item_na_time);
            item_na_where_from = itemView.findViewById(R.id.item_na_where_from);
            item_na_img = itemView.findViewById(R.id.item_na_img);

            item_iv_logo = itemView.findViewById(R.id.item_iv_logo);

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
        ImageView item_iv_logo;

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

            item_iv_logo = itemView.findViewById(R.id.item_iv_logo);

            item_na_layout = itemView.findViewById(R.id.item_na_layout);
            item_na_image_group = itemView.findViewById(R.id.item_na_image_group);
        }
    }
}
