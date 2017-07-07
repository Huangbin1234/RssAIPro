package com.hb.rssai.app;

import android.app.Application;
import android.content.Context;

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
    }

    public static ProjectApplication getApplication() {
        return sApplication;
    }
}
