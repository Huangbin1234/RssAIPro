package com.hb.rssai.app;

import android.app.Application;
import android.content.Context;

import com.hb.rssai.util.LiteOrmDBUtil;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ProjectApplication extends Application {
    public static Context mContext;
    private static ProjectApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sApplication = this;
        // TODO: 初始化数据库
        LiteOrmDBUtil.createDb(this);
    }

    public static ProjectApplication getApplication() {
        return sApplication;
    }
}
