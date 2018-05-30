package com.hb.util;


/**
 * Created by rimi on 2016/5/21.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 */
public class StringUtils {
    /**
     * is null or its length is 0 or it is made by space
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }
    /**
     * 正则提取字符串中的数字 去掉小数点
     *
     * @param str
     * @return
     */
    public static String getNumbers(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);
        return matcher.replaceAll("").trim();
    }
    /**
     * TODO:
     *
     * @param activity
     * @param qq
     */
    public static void openQQ(Activity activity, String qq) {
        String QQ = "699547875";
        if (!TextUtils.isEmpty(qq)) {
            QQ = qq;
        }
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + QQ;
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
    /****************
     *
     * 发起添加群流程。群号：Zr(699547875) 的 key 为： VyA7mXrlsAOQGFbuqX_0CL35MbSEPX3u
     * 调用 joinQQGroup(VyA7mXrlsAOQGFbuqX_0CL35MbSEPX3u) 即可发起手Q客户端申请加群 Zr(699547875)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Activity activity,String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}