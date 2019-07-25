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
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.HttpLoadImg;
import com.hb.rssai.util.StringUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */
public class SourceListCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<List<ResCardSubscribe.RetObjBean.RowsBean>> rssList;
    private LayoutInflater layoutInflater;
    private String[] images = null;

    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private final int TYPE_FOUR = 4;
    private final int TYPE_FIVE = 5;

    private ResCardSubscribe.RetObjBean.RowsBean tempRowsBean;

    public SourceListCardAdapter(Context mContext, List<List<ResCardSubscribe.RetObjBean.RowsBean>> rssList) {
        this.mContext = mContext;
        this.rssList = rssList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        int len = rssList.get(position).size();
        if (len == 1) {
            return TYPE_ONE;
        } else if (len == 2) {
            return TYPE_TWO;
        } else if (len == 3) {
            return TYPE_THREE;
        } else if (len == 4) {
            return TYPE_FOUR;
        } else if (len == 5) {
            return TYPE_FIVE;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = layoutInflater.inflate(R.layout.item_resource_list_one, parent, false);
            return new TypeOneViewHolder(view);
        } else if (viewType == TYPE_TWO) {
            View view = layoutInflater.inflate(R.layout.item_resource_list_two, parent, false);
            return new TypeTwoViewHolder(view);
        } else if (viewType == TYPE_THREE) {
            View view = layoutInflater.inflate(R.layout.item_resource_list_three, parent, false);
            return new TypeThreeViewHolder(view);
        } else if (viewType == TYPE_FOUR) {
            View view = layoutInflater.inflate(R.layout.item_resource_list_four, parent, false);
            return new TypeFourViewHolder(view);
        } else if (viewType == TYPE_FIVE) {
            View view = layoutInflater.inflate(R.layout.item_resource_list_five, parent, false);
            return new TypeFiveViewHolder(view);
        }
        return null;
    }

    private void bindTypeOne(final RecyclerView.ViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
        tempRowsBean = rowBean.get(0);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadImg(mContext, StringUtil.filterImage(url), ((TypeOneViewHolder) holder).irl_iv_top);
            } else {
                ((TypeOneViewHolder) holder).irl_iv_top.setVisibility(View.GONE);
            }
            ((TypeOneViewHolder) holder).irl_tv_top.setText(tempRowsBean.getTitle());
            try {
                ((TypeOneViewHolder) holder).irl_tv_top_time.setText(DateUtil.showDate(Constant.sdf.parse(tempRowsBean.getPubTime()), Constant.DATE_LONG_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TypeOneViewHolder) holder).irl_top_rl.setOnClickListener(view -> clickItem(position, 0));
        }
    }

    private void bindTypeTwo(final RecyclerView.ViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
        tempRowsBean = rowBean.get(0);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadImg(mContext, StringUtil.filterImage(url), ((TypeTwoViewHolder) holder).irl_iv_top);
            } else {
                ((TypeTwoViewHolder) holder).irl_iv_top.setVisibility(View.GONE);
            }
            ((TypeTwoViewHolder) holder).irl_tv_top.setText(tempRowsBean.getTitle());
            try {
                ((TypeTwoViewHolder) holder).irl_tv_top_time.setText(DateUtil.showDate(Constant.sdf.parse(tempRowsBean.getPubTime()), Constant.DATE_LONG_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TypeTwoViewHolder) holder).irl_top_rl.setOnClickListener(view -> clickItem(position, 0));
        }
        tempRowsBean = rowBean.get(1);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext,  StringUtil.filterImage(url), ((TypeTwoViewHolder) holder).irl_iv_top1);
            } else {
                ((TypeTwoViewHolder) holder).irl_iv_top1.setVisibility(View.GONE);
            }
            ((TypeTwoViewHolder) holder).irl_tv_top1.setText(tempRowsBean.getTitle());
            ((TypeTwoViewHolder) holder).irl_top_ll1.setOnClickListener(view -> clickItem(position, 1));
        }
    }

    private void bindTypeThree(final RecyclerView.ViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
        tempRowsBean = rowBean.get(0);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadImg(mContext,  StringUtil.filterImage(url), ((TypeThreeViewHolder) holder).irl_iv_top);
            } else {
                ((TypeThreeViewHolder) holder).irl_iv_top.setVisibility(View.GONE);
            }
            ((TypeThreeViewHolder) holder).irl_tv_top.setText(tempRowsBean.getTitle());
            try {
                ((TypeThreeViewHolder) holder).irl_tv_top_time.setText(DateUtil.showDate(Constant.sdf.parse(tempRowsBean.getPubTime()), Constant.DATE_LONG_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TypeThreeViewHolder) holder).irl_top_rl.setOnClickListener(view -> clickItem(position, 0));
        }
        tempRowsBean = rowBean.get(1);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext,  StringUtil.filterImage(url), ((TypeThreeViewHolder) holder).irl_iv_top1);
            } else {
                ((TypeThreeViewHolder) holder).irl_iv_top1.setVisibility(View.GONE);
            }
            ((TypeThreeViewHolder) holder).irl_tv_top1.setText(tempRowsBean.getTitle());
            ((TypeThreeViewHolder) holder).irl_top_ll1.setOnClickListener(view -> clickItem(position, 1));
        }
        tempRowsBean = rowBean.get(2);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((TypeThreeViewHolder) holder).irl_iv_top2);
            } else {
                ((TypeThreeViewHolder) holder).irl_iv_top2.setVisibility(View.GONE);
            }
            ((TypeThreeViewHolder) holder).irl_tv_top2.setText(tempRowsBean.getTitle());
            ((TypeThreeViewHolder) holder).irl_top_ll2.setOnClickListener(view -> clickItem(position, 2));
        }
    }

    private void bindTypeFour(final RecyclerView.ViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
        tempRowsBean = rowBean.get(0);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadImg(mContext,  StringUtil.filterImage(url), ((TypeFourViewHolder) holder).irl_iv_top);
            } else {
                ((TypeFourViewHolder) holder).irl_iv_top.setVisibility(View.GONE);
            }
            ((TypeFourViewHolder) holder).irl_tv_top.setText(tempRowsBean.getTitle());
            try {
                ((TypeFourViewHolder) holder).irl_tv_top_time.setText(DateUtil.showDate(Constant.sdf.parse(tempRowsBean.getPubTime()), Constant.DATE_LONG_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TypeFourViewHolder) holder).irl_top_rl.setOnClickListener(view -> clickItem(position, 0));
        }
        tempRowsBean = rowBean.get(1);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext,StringUtil.filterImage(url), ((TypeFourViewHolder) holder).irl_iv_top1);
            } else {
                ((TypeFourViewHolder) holder).irl_iv_top1.setVisibility(View.GONE);
            }
            ((TypeFourViewHolder) holder).irl_tv_top1.setText(tempRowsBean.getTitle());
            ((TypeFourViewHolder) holder).irl_top_ll1.setOnClickListener(view -> clickItem(position, 1));
        }
        tempRowsBean = rowBean.get(2);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((TypeFourViewHolder) holder).irl_iv_top2);
            } else {
                ((TypeFourViewHolder) holder).irl_iv_top2.setVisibility(View.GONE);
            }
            ((TypeFourViewHolder) holder).irl_tv_top2.setText(tempRowsBean.getTitle());
            ((TypeFourViewHolder) holder).irl_top_ll2.setOnClickListener(view -> clickItem(position, 2));
        }
        tempRowsBean = rowBean.get(3);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((TypeFourViewHolder) holder).irl_iv_top3);
            } else {
                ((TypeFourViewHolder) holder).irl_iv_top3.setVisibility(View.GONE);
            }
            ((TypeFourViewHolder) holder).irl_tv_top3.setText(tempRowsBean.getTitle());
            ((TypeFourViewHolder) holder).irl_top_ll3.setOnClickListener(view -> clickItem(position, 3));
        }
    }

    private void bindTypeFive(final RecyclerView.ViewHolder holder, int position) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
        tempRowsBean = rowBean.get(0);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadImg(mContext, StringUtil.filterImage(url), ((TypeFiveViewHolder) holder).irl_iv_top);
            } else {
                ((TypeFiveViewHolder) holder).irl_iv_top.setVisibility(View.GONE);
            }
            ((TypeFiveViewHolder) holder).irl_tv_top.setText(tempRowsBean.getTitle());
            try {
                ((TypeFiveViewHolder) holder).irl_tv_top_time.setText(DateUtil.showDate(Constant.sdf.parse(tempRowsBean.getPubTime()), Constant.DATE_LONG_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TypeFiveViewHolder) holder).irl_top_rl.setOnClickListener(view -> clickItem(position, 0));
        }
        tempRowsBean = rowBean.get(1);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext,  StringUtil.filterImage(url), ((TypeFiveViewHolder) holder).irl_iv_top1);
            } else {
                ((TypeFiveViewHolder) holder).irl_iv_top1.setVisibility(View.GONE);
            }
            ((TypeFiveViewHolder) holder).irl_tv_top1.setText(tempRowsBean.getTitle());
            ((TypeFiveViewHolder) holder).irl_top_ll1.setOnClickListener(view -> clickItem(position, 1));
        }
        tempRowsBean = rowBean.get(2);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((TypeFiveViewHolder) holder).irl_iv_top2);
            } else {
                ((TypeFiveViewHolder) holder).irl_iv_top2.setVisibility(View.GONE);
            }
            ((TypeFiveViewHolder) holder).irl_tv_top2.setText(tempRowsBean.getTitle());
            ((TypeFiveViewHolder) holder).irl_top_ll2.setOnClickListener(view -> clickItem(position, 2));
        }

        tempRowsBean = rowBean.get(3);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext, StringUtil.filterImage(url), ((TypeFiveViewHolder) holder).irl_iv_top3);
            } else {
                ((TypeFiveViewHolder) holder).irl_iv_top3.setVisibility(View.GONE);
            }
            ((TypeFiveViewHolder) holder).irl_tv_top3.setText(tempRowsBean.getTitle());
            ((TypeFiveViewHolder) holder).irl_top_ll3.setOnClickListener(view -> clickItem(position, 3));
        }
        tempRowsBean = rowBean.get(4);
        if (null != tempRowsBean) {
            images = TextUtils.isEmpty(tempRowsBean.getImageUrls()) ? null : tempRowsBean.getImageUrls().split(",http");
            if (images != null && images.length > 0) {
                String url = URLDecoder.decode(images[0]);
                HttpLoadImg.loadRoundImg(mContext,StringUtil.filterImage(url), ((TypeFiveViewHolder) holder).irl_iv_top4);
            } else {
                ((TypeFiveViewHolder) holder).irl_iv_top4.setVisibility(View.GONE);
            }
            ((TypeFiveViewHolder) holder).irl_tv_top4.setText(tempRowsBean.getTitle());
            ((TypeFiveViewHolder) holder).irl_top_ll4.setOnClickListener(view -> clickItem(position, 4));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeOneViewHolder) {
            bindTypeOne(holder, position);
        } else if (holder instanceof TypeTwoViewHolder) {
            bindTypeTwo(holder, position);
        } else if (holder instanceof TypeThreeViewHolder) {
            bindTypeThree(holder, position);
        } else if (holder instanceof TypeFourViewHolder) {
            bindTypeFour(holder, position);
        } else if (holder instanceof TypeFiveViewHolder) {
            bindTypeFive(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return rssList == null ? 0 : rssList.size();
    }

    private void clickItem(int position, int index) {
        List<ResCardSubscribe.RetObjBean.RowsBean> rowBean = rssList.get(position);
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
                intent.putExtra("count", rowBean.get(index).getCount());
                mContext.startActivity(intent);//将Intent传递给Activity
            } else {
                T.ShowToast(mContext, "链接错误，无法跳转！");
            }
        } else {
            T.ShowToast(mContext, "请等待数据加载完成！");
        }
    }

    class TypeOneViewHolder extends RecyclerView.ViewHolder {
        ImageView irl_iv_top;
        TextView irl_tv_top;
        TextView irl_tv_top_time;
        RelativeLayout irl_top_rl;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            irl_top_rl = itemView.findViewById(R.id.irl_top_rl);

            irl_tv_top = itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = itemView.findViewById(R.id.irl_tv_top_time);
            irl_iv_top = itemView.findViewById(R.id.irl_iv_top);
        }
    }

    class TypeTwoViewHolder extends RecyclerView.ViewHolder {
        TextView irl_tv_top1;
        TextView irl_tv_top;
        TextView irl_tv_top_time;
        ImageView irl_iv_top1;
        ImageView irl_iv_top;

        RelativeLayout irl_top_rl;
        LinearLayout irl_top_ll1;

        public TypeTwoViewHolder(View itemView) {
            super(itemView);
            irl_top_rl = itemView.findViewById(R.id.irl_top_rl);
            irl_top_ll1 = itemView.findViewById(R.id.irl_top_ll1);

            irl_tv_top1 = itemView.findViewById(R.id.irl_tv_top1);
            irl_tv_top = itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = itemView.findViewById(R.id.irl_tv_top_time);
            irl_iv_top1 = itemView.findViewById(R.id.irl_iv_top1);
            irl_iv_top = itemView.findViewById(R.id.irl_iv_top);
        }
    }

    class TypeThreeViewHolder extends RecyclerView.ViewHolder {
        TextView irl_tv_top2;
        TextView irl_tv_top1;
        TextView irl_tv_top;
        TextView irl_tv_top_time;
        ImageView irl_iv_top2;
        ImageView irl_iv_top1;
        ImageView irl_iv_top;

        RelativeLayout irl_top_rl;
        LinearLayout irl_top_ll1;
        LinearLayout irl_top_ll2;


        public TypeThreeViewHolder(View itemView) {
            super(itemView);
            irl_top_rl = itemView.findViewById(R.id.irl_top_rl);
            irl_top_ll1 = itemView.findViewById(R.id.irl_top_ll1);
            irl_top_ll2 = itemView.findViewById(R.id.irl_top_ll2);

            irl_tv_top2 = itemView.findViewById(R.id.irl_tv_top2);
            irl_tv_top1 = itemView.findViewById(R.id.irl_tv_top1);
            irl_tv_top = itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = itemView.findViewById(R.id.irl_tv_top_time);

            irl_iv_top2 = itemView.findViewById(R.id.irl_iv_top2);
            irl_iv_top1 = itemView.findViewById(R.id.irl_iv_top1);
            irl_iv_top = itemView.findViewById(R.id.irl_iv_top);
        }
    }

    class TypeFourViewHolder extends RecyclerView.ViewHolder {
        TextView irl_tv_top3;
        TextView irl_tv_top2;
        TextView irl_tv_top1;
        TextView irl_tv_top;
        TextView irl_tv_top_time;
        ImageView irl_iv_top3;
        ImageView irl_iv_top2;
        ImageView irl_iv_top1;
        ImageView irl_iv_top;

        RelativeLayout irl_top_rl;
        LinearLayout irl_top_ll1;
        LinearLayout irl_top_ll2;
        LinearLayout irl_top_ll3;

        public TypeFourViewHolder(View itemView) {
            super(itemView);
            irl_top_rl = itemView.findViewById(R.id.irl_top_rl);
            irl_top_ll1 = itemView.findViewById(R.id.irl_top_ll1);
            irl_top_ll2 = itemView.findViewById(R.id.irl_top_ll2);
            irl_top_ll3 = itemView.findViewById(R.id.irl_top_ll3);

            irl_tv_top3 = itemView.findViewById(R.id.irl_tv_top3);
            irl_tv_top2 = itemView.findViewById(R.id.irl_tv_top2);
            irl_tv_top1 = itemView.findViewById(R.id.irl_tv_top1);
            irl_tv_top = itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = itemView.findViewById(R.id.irl_tv_top_time);

            irl_iv_top3 = itemView.findViewById(R.id.irl_iv_top3);
            irl_iv_top2 = itemView.findViewById(R.id.irl_iv_top2);
            irl_iv_top1 = itemView.findViewById(R.id.irl_iv_top1);
            irl_iv_top = itemView.findViewById(R.id.irl_iv_top);
        }
    }

    class TypeFiveViewHolder extends RecyclerView.ViewHolder {
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

        RelativeLayout irl_top_rl;
        LinearLayout irl_top_ll1;
        LinearLayout irl_top_ll2;
        LinearLayout irl_top_ll3;
        LinearLayout irl_top_ll4;

        public TypeFiveViewHolder(View itemView) {
            super(itemView);
            irl_top_rl = itemView.findViewById(R.id.irl_top_rl);
            irl_top_ll1 = itemView.findViewById(R.id.irl_top_ll1);
            irl_top_ll2 = itemView.findViewById(R.id.irl_top_ll2);
            irl_top_ll3 = itemView.findViewById(R.id.irl_top_ll3);
            irl_top_ll4 = itemView.findViewById(R.id.irl_top_ll4);

            irl_tv_top4 = itemView.findViewById(R.id.irl_tv_top4);
            irl_tv_top3 = itemView.findViewById(R.id.irl_tv_top3);
            irl_tv_top2 = itemView.findViewById(R.id.irl_tv_top2);
            irl_tv_top1 = itemView.findViewById(R.id.irl_tv_top1);
            irl_tv_top = itemView.findViewById(R.id.irl_tv_top);
            irl_tv_top_time = itemView.findViewById(R.id.irl_tv_top_time);

            irl_iv_top4 = itemView.findViewById(R.id.irl_iv_top4);
            irl_iv_top3 = itemView.findViewById(R.id.irl_iv_top3);
            irl_iv_top2 = itemView.findViewById(R.id.irl_iv_top2);
            irl_iv_top1 = itemView.findViewById(R.id.irl_iv_top1);
            irl_iv_top = itemView.findViewById(R.id.irl_iv_top);
        }
    }
}
