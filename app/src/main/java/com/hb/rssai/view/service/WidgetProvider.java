package com.hb.rssai.view.service;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.informationApi;

/**
 * Created by LIUYONGKUI726 on 2017-07-10.
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WidgetProvider extends AppWidgetProvider {

    final String clickAction = "com.rssai.WidgetProvider.onclick";
    final String refreshAction = "refresh";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
//        System.out.println(WidgetProvider.class.getSimpleName() + "============================================================>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            // 获取Widget的组件名
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            // 创建一个RemoteView
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            // 把这个Widget绑定到RemoteViewsService
            Intent intent = new Intent(context, ListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
            // 设置适配器
            remoteViews.setRemoteAdapter(R.id.list_view, intent);
            // 设置当显示的widget_list为空显示的View
            remoteViews.setEmptyView(R.id.list_view, R.layout.layout_no_data);
            // 点击列表触发事件
            Intent clickIntent = new Intent(context, WidgetProvider.class);
            // 设置Action，方便在onReceive中区别点击事件
            clickIntent.setAction(clickAction);
            clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.list_view, pendingIntentTemplate);
            // 刷新按钮
            final Intent refreshIntent = new Intent(context, WidgetProvider.class);
            refreshIntent.setAction(refreshAction);
            final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_iv_refresh, refreshPendingIntent);

            // 创建一个RemoteView
            RemoteViews remoteViews2 = new RemoteViews(context.getPackageName(), R.layout.app_widget_lock_screen);
            // 把这个Widget绑定到RemoteViewsService
            Intent intent2 = new Intent(context, ListWidgetService.class);
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
            // 设置适配器
            remoteViews2.setRemoteAdapter(R.id.list_view, intent2);
            // 设置当显示的widget_list为空显示的View
            remoteViews2.setEmptyView(R.id.list_view, R.layout.layout_no_data);
            // 点击列表触发事件
            Intent clickIntent2 = new Intent(context, WidgetProvider.class);
            // 设置Action，方便在onReceive中区别点击事件
            clickIntent2.setAction(clickAction);
            clickIntent2.setData(Uri.parse(clickIntent2.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntentTemplate2 = PendingIntent.getBroadcast(context, 0, clickIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews2.setPendingIntentTemplate(R.id.list_view, pendingIntentTemplate2);
            // 刷新按钮
            final Intent refreshIntent2 = new Intent(context, WidgetProvider.class);
            refreshIntent2.setAction(refreshAction);
            final PendingIntent refreshPendingIntent2 = PendingIntent.getBroadcast(context, 0, refreshIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews2.setOnClickPendingIntent(R.id.widget_iv_refresh, refreshPendingIntent2);
            // 更新Wdiget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            // 更新Wdiget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews2);
        }

    }

    /**
     * 接收Intent
     */
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        System.out.println(WidgetProvider.class.getSimpleName() + "============================================================>" + action );
        if (action.equals(refreshAction)) {
            mContext = context;
            getLikeByTitle(context);
            // 刷新Widget
//            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            final ComponentName cn = new ComponentName(context, WidgetProvider.class);
//            WdRemoteViewsFactory.listData.clear();
            // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.list_view);
        } else if (action.equals(clickAction)) {
            Intent toIntent = new Intent(context, RichTextActivity.class);
            toIntent.putExtra(ContentActivity.KEY_TITLE, intent.getStringExtra(ContentActivity.KEY_TITLE));
            toIntent.putExtra(ContentActivity.KEY_URL, intent.getStringExtra(ContentActivity.KEY_URL));
            toIntent.putExtra(ContentActivity.KEY_INFORMATION_ID, intent.getStringExtra(ContentActivity.KEY_INFORMATION_ID));
            toIntent.putExtra("pubDate", intent.getStringExtra("pubDate"));
            toIntent.putExtra("whereFrom", intent.getStringExtra("whereFrom"));
            toIntent.putExtra("abstractContent", intent.getStringExtra("abstractContent"));
            toIntent.putExtra("clickGood", intent.getLongExtra("clickGood", 0));
            toIntent.putExtra("clickNotGood", intent.getLongExtra("clickNotGood", 0));
            toIntent.putExtra("id", intent.getStringExtra("id"));
            toIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toIntent);
        }else{
            mContext = context;
            getLikeByTitle(context);
        }
    }

    public void getLikeByTitle(Context context) {
        informationApi.getLikeByTitle(getParams()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resInfo -> {
                    setListResult(resInfo, context);
                }, this::loadError);
    }


    private void setListResult(ResInformation resInformation, Context context) {
        if (resInformation.getRetObj() == null || resInformation.getRetObj().getRows() == null) {
            return;
        }
        if (WdRemoteViewsFactory.listData != null) {
            WdRemoteViewsFactory.listData.clear();
        }
        WdRemoteViewsFactory.listData = resInformation.getRetObj().getRows();
        // 刷新Widget
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, WidgetProvider.class);
//            WdRemoteViewsFactory.listData.clear();
        // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.list_view);
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
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