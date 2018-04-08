package com.hb.rssai.view.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.informationApi;

/**
 * Created by LIUYONGKUI726 on 2017-07-10.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WdRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static List<ResInformation.RetObjBean.RowsBean> listData = new ArrayList<>();


    /*
     * 构造函数
     */
    public WdRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    /*
     * MyRemoteViewsFactory调用时执行，这个方法执行时间超过20秒回报错。
     * 如果耗时长的任务应该在onDataSetChanged或者getViewAt中处理
     */
    @Override
    public void onCreate() {
        // 需要显示的数据
        System.out.println("onDataSetChanged========>0");
        getLikeByTitle();
    }

    /*
     * 当调用notifyAppWidgetViewDataChanged方法时，触发这个方法
     * 例如：WdRemoteViewsFactory.notifyAppWidgetViewDataChanged();
     */
    @Override
    public void onDataSetChanged() {
        System.out.println("onDataSetChanged========>1");
        getLikeByTitle();
    }

    /*
     * 这个方法不用多说了把，这里写清理资源，释放内存的操作
     */
    @Override
    public void onDestroy() {
        if (listData != null)
            listData.clear();
    }

    /*
     * 返回集合数量
     */
    @Override
    public int getCount() {
        return listData != null ? listData.size() : 0;
    }

    /*
     * 创建并且填充，在指定索引位置显示的View，这个和BaseAdapter的getView类似
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= listData.size())
            return null;
        // 创建在当前索引位置要显示的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item);
        // 设置要显示的内容
        // 设置 第position位的“视图”的数据
        if (listData.get(position) != null && listData.size() > 0) {
            String[] images = TextUtils.isEmpty(listData.get(position).getImageUrls()) ? null : listData.get(position).getImageUrls().split(",");
            if (images != null && images.length > 0) {
                if (images.length >= 1) {
                    loadImageForListItem(mContext, images[0], rv);
                    System.out.println(position + " _" + images[0]);
                }
            }else{
                loadImageForListItem(mContext, null, rv);
            }
        } else {
            loadImageForListItem(mContext, null, rv);
        }
        rv.setTextViewText(R.id.itemText, listData.get(position).getTitle());
        try {
            rv.setTextViewText(R.id.widget_item_time, DateUtil.showDate(sdf.parse(listData.get(position).getPubTime()), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 设置 第position位的“视图”对应的响应事件
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(ContentActivity.KEY_TITLE, listData.get(position).getTitle());
        fillInIntent.putExtra(ContentActivity.KEY_URL, listData.get(position).getLink());
        fillInIntent.putExtra(ContentActivity.KEY_INFORMATION_ID, listData.get(position).getId());
        fillInIntent.putExtra("pubDate", listData.get(position).getPubTime());
        fillInIntent.putExtra("whereFrom", listData.get(position).getWhereFrom());
        fillInIntent.putExtra("abstractContent", listData.get(position).getAbstractContent());
        fillInIntent.putExtra("clickGood", listData.get(position).getClickGood());
        fillInIntent.putExtra("clickNotGood", listData.get(position).getClickNotGood());
        fillInIntent.putExtra("id", listData.get(position).getId());
        rv.setOnClickFillInIntent(R.id.itemText, fillInIntent);
        return rv;
    }

    private void loadImageForListItem(Context context, String pathName, RemoteViews remoteViews) {
        if(TextUtils.isEmpty(pathName)){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_no_image);
            remoteViews.setImageViewBitmap(R.id.itemImage, bitmap);
            return;
        }
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
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_no_image);
            remoteViews.setImageViewBitmap(R.id.itemImage, bitmap);
        }
    }

    /*
     * 显示一个"加载"View。返回null的时候将使用默认的View
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /*
     * 不同View定义的数量。默认为1（本人一直在使用默认值）
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /*
     * 返回当前索引的。
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 如果每个项提供的ID是稳定的，即她们不会在运行时改变，就返回true（没用过。。。）
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }


    public void getLikeByTitle() {
        informationApi.getLikeByTitle(getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setListResult(resInfo);
                }, this::loadError);
    }


    private void setListResult(ResInformation resInformation) {
        if (resInformation.getRetObj() == null || resInformation.getRetObj().getRows() == null) {
            return;
        }
        if (listData != null) {
            listData.clear();
        }
        listData = resInformation.getRetObj().getRows();

    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        T.ShowToast(mContext, Constant.MSG_NETWORK_ERROR);
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        String des = "";
        String title = "";
        String jsonParams = "{\"title\":\"" + title + "\"}";
        map.put(Constant.KEY_JSON_PARAMS, jsonParams);
        return map;
    }
}
