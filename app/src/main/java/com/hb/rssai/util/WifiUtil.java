package com.hb.rssai.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator
 * on 2018/8/24
 */
public class WifiUtil {
    /**
     * 检测wifi
     * @param context
     * @return
     */
    public static boolean isWIFIkAvailable(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }
}
