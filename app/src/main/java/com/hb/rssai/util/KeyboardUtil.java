package com.hb.rssai.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by Administrator on 2017/3/14.
 */

public class KeyboardUtil {
    /**
     * 隐藏键盘
     */
    public static void hideSoftKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
