package com.hb.rssai.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ProjectApplication extends Application {
    public static Context mContext;
    private static ProjectApplication sApplication;
    public static long sys_night_mode_time = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        sys_night_mode_time = SharedPreferencesUtil.getLong(this, Constant.KEY_SYS_NIGHT_MODE_TIME, 0);
        if (sys_night_mode_time != 0) {
            if (SharedPreferencesUtil.getBoolean(this, Constant.KEY_SYS_NIGHT_MODE, false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        mContext = getApplicationContext();
        sApplication = this;
        // TODO: 初始化数据库
        LiteOrmDBUtil.createDb(this);
    }

    public static ProjectApplication getApplication() {
        return sApplication;
    }
}
