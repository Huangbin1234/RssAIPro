package com.hb.rssai.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

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
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
    public static String filterImage(String url) {
        if (-1 != url.indexOf("image_uri")) {
            String temp = url.substring(url.indexOf("image_uri") + 10);
            return temp.substring(0, temp.indexOf("&#38"));
        } else {
            return url;
        }
    }

    /**
     * [0,1)
     * @param low
     * @param high
     * @return
     */
    public static int randomNumber (int low, int high) {
        return (int) (Math.random() * (high - low + 1) + low);
    }
}
