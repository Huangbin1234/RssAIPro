package com.hb.rssai.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.hb.rssai.constants.Constant;
import com.hb.rssai.util.LiteOrmDBUtil;
import com.hb.rssai.util.SharedPreferencesUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ProjectApplication extends MultiDexApplication{
    public static Context mContext;
    private static ProjectApplication sApplication;
    public static long sys_night_mode_time = 0;
    {

        PlatformConfig.setWeixin("wx722beb7df39ae6c2", "0aa0a79641ca712815308a820cf63d9d");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
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
