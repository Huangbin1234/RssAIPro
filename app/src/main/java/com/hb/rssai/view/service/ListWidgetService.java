package com.hb.rssai.view.service;

/**
 * Created by Administrator on 2017/9/21.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.hb.rssai.R;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.DateUtil;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.informationApi;

public class ListWidgetService extends RemoteViewsService {

    private static final String TAG = "SKYWANG";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "ListWidgetService");
        return new GridRemoteViewsFactory(this, intent);
    }

    public void getLikeByTitle() {
        informationApi.getLikeByTitle(getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setListResult(resInfo);
                }, this::loadError);
    }

    private List<ResInformation.RetObjBean.RowsBean> listData;

    private void setListResult(ResInformation resInformation) {
        if (resInformation.getRetObj() == null || resInformation.getRetObj().getRows() == null) {
            return;
        }
        if(listData!=null){
            listData.clear();
        }
        listData = resInformation.getRetObj().getRows();
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(this, Constant.MSG_NETWORK_ERROR);
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String des = "";
        String title = "";
        String jsonParams = "{\"title\":\"" + title + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }

    private class GridRemoteViewsFactory implements RemoteViewsFactory {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private Context mContext;
        private int mAppWidgetId;

        /**
         * 构造GridRemoteViewsFactory
         *
         * @author skywang
         */
        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d(TAG, "GridRemoteViewsFactory mAppWidgetId:" + mAppWidgetId);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            HashMap<String, Object> map;

            Log.d(TAG, "GridRemoteViewsFactory getViewAt:" + position);
            // 获取 grid_view_item.xml 对应的RemoteViews
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item);

            // 设置 第position位的“视图”的数据
            if (listData.get(position) != null && listData.size() > 0) {
                String[] images = TextUtils.isEmpty(listData.get(position).getImageUrls()) ? null : listData.get(position).getImageUrls().split(",");
                if (images != null && images.length > 0) {
                    if (images.length >= 1) {
//                        Glide.with(mContext).load(images[0]).asBitmap().into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                rv.setImageViewBitmap(R.id.itemImage, resource);
//                            }
//                        });
                        loadImageForListItem(mContext, images[0], rv);
                        System.out.println(position +" _"+images[0]);
                    }
                }
                rv.setTextViewText(R.id.itemText, listData.get(position).getTitle());
                try {
                    rv.setTextViewText(R.id.widget_item_time, DateUtil.showDate(sdf.parse(listData.get(position).getPubTime()), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // 设置 第position位的“视图”对应的响应事件
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(WidgetProvider.COLLECTION_VIEW_EXTRA, position);

            fillInIntent.putExtra(ContentActivity.KEY_TITLE, listData.get(position).getTitle());
            fillInIntent.putExtra(ContentActivity.KEY_URL, listData.get(position).getLink());
            fillInIntent.putExtra(ContentActivity.KEY_INFORMATION_ID, listData.get(position).getId());
            fillInIntent.putExtra("pubDate", listData.get(position).getPubTime());
            fillInIntent.putExtra("whereFrom", listData.get(position).getWhereFrom());
            fillInIntent.putExtra("abstractContent", listData.get(position).getAbstractContent());
            fillInIntent.putExtra("clickGood", listData.get(position).getClickGood());
            fillInIntent.putExtra("clickNotGood", listData.get(position).getClickNotGood());
            fillInIntent.putExtra("id", listData.get(position).getId());

            rv.setOnClickFillInIntent(R.id.itemLayout, fillInIntent);
            return rv;
        }

        private void loadImageForListItem(
                Context context, String pathName, RemoteViews remoteViews) {
            int width = 200;
            int height = 200;
            BitmapRequestBuilder builder =
                    Glide.with(context)
                            .load(pathName)
                            .asBitmap()
                            .centerCrop();
            FutureTarget futureTarget = builder.into(width, height);
            try {
                remoteViews.setImageViewBitmap(R.id.itemImage, (Bitmap) futureTarget.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        /**
         * 初始化GridView的数据
         *
         * @author skywang
         */
        private void initGridViewData() {
//            data = new ArrayList<HashMap<String, Object>>();

//            for (int i = 0; i < 9; i++) {
//                HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put(IMAGE_ITEM, arrImages[i]);
//                map.put(TEXT_ITEM, arrText[i]);
//                data.add(map);
//            }
//            getLikeByTitle();
        }


        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate");
            // 初始化“集合视图”中的数据
//            initGridViewData();
        }

        @Override
        public int getCount() {
            // 返回“集合视图”中的数据的总数
            return listData != null ? listData.size() : 0;
        }

        @Override
        public long getItemId(int position) {
            // 返回当前项在“集合视图”中的位置
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            // 只有一类 GridView
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            getLikeByTitle();
        }

        @Override
        public void onDestroy() {
            listData.clear();
        }
    }
}