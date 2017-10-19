package com.hb.rssai.view.service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hb.rssai.R;
import com.hb.rssai.bean.ResInformation;
import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.T;
import com.hb.rssai.view.common.ContentActivity;
import com.hb.rssai.view.common.RichTextActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hb.rssai.presenter.BasePresenter.informationApi;

/**
 * Created by lyl on 2017/8/23.
 */

public class WidgetProvider extends AppWidgetProvider {

    // 更新 widget 的广播对应的action
    private final String BT_REFRESH_ACTION = "com.lyl.widget.BT_REFRESH_ACTION";
    private final String COLLECTION_VIEW_ACTION = "com.lyl.widget.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "com.lyl.widget.COLLECTION_VIEW_EXTRA";

    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,  int[] appWidgetIds) {

        Log.d(TAG, "GridWidgetProvider onUpdate");
        for (int appWidgetId:appWidgetIds) {
            // 获取AppWidget对应的视图
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // 设置响应 “按钮(bt_refresh)” 的intent
            Intent btIntent = new Intent().setAction(BT_REFRESH_ACTION);
            PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, btIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.widget_iv_refresh, btPendingIntent);

            // 设置 “GridView(gridview)” 的adapter。
            // (01) intent: 对应启动 ListWidgetService(RemoteViewsService) 的intent
            // (02) setRemoteAdapter: 设置 gridview的适配器
            //    通过setRemoteAdapter将gridview和GridWidgetService关联起来，
            //    以达到通过 ListWidgetService 更新 gridview 的目的
            Intent serviceIntent = new Intent(context, ListWidgetService.class);
            rv.setRemoteAdapter(R.id.list_view, serviceIntent);

            // 设置响应 “GridView(gridview)” 的intent模板
            // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素，如GridView包含很多格子。
            //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
            //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
            //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
            Intent gridIntent = new Intent();
            gridIntent.setAction(COLLECTION_VIEW_ACTION);
            gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // 设置intent模板
            rv.setPendingIntentTemplate(R.id.list_view, pendingIntent);
            // 调用集合管理器对集合进行更新
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        Log.d(TAG, "GridWidgetProvider onReceive : "+intent.getAction());
        if (action.equals(COLLECTION_VIEW_ACTION)) {
            // 接受“gridview”的点击事件的广播
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);

            Intent toIntent = new Intent(context,RichTextActivity.class);

            toIntent.putExtra(ContentActivity.KEY_TITLE, intent.getStringExtra(ContentActivity.KEY_TITLE));
            toIntent.putExtra(ContentActivity.KEY_URL, intent.getStringExtra(ContentActivity.KEY_URL));
            toIntent.putExtra(ContentActivity.KEY_INFORMATION_ID, intent.getStringExtra(ContentActivity.KEY_INFORMATION_ID));
            toIntent.putExtra("pubDate", intent.getStringExtra("pubDate"));
            toIntent.putExtra("whereFrom", intent.getStringExtra("whereFrom"));
            toIntent.putExtra("abstractContent",intent.getStringExtra("abstractContent"));
            toIntent.putExtra("clickGood", intent.getLongExtra("clickGood",0));
            toIntent.putExtra("clickNotGood",intent.getLongExtra("clickNotGood",0));
            toIntent.putExtra("id", intent.getStringExtra("id"));
            //创建一个pendingIntent。另外两个参数以后再讲。
            PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, toIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        } else if (action.equals(BT_REFRESH_ACTION)) {
            // 接受“bt_refresh”的点击事件的广播
            ComponentName cmpName = new ComponentName(context, WidgetProvider.class);
            int[] appIds = appWidgetManager.getAppWidgetIds(cmpName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.list_view);
            Toast.makeText(context, "Click Button", Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}
