package com.hb.rssai.util;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class StringUtil {
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content, Context context)
    {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}
