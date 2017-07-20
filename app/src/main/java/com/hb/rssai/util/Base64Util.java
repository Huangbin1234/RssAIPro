package com.hb.rssai.util;

import android.util.Base64;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class Base64Util {
    /**
     * Base64编码
     * @param str
     * @return 编码完成的字符串码
     */
    public static String getEncodeStr(String str){
        byte byteArr[]= Base64.encode(str.getBytes(), Base64.DEFAULT);
        return new String(byteArr);
    }
    /**
     * Base64解码
     * @param encodeStr
     * @return 解码完成，输出原本字符串
     */
    public static String getDecodeStr(String encodeStr){
        byte byteArr[]=Base64.decode(encodeStr, Base64.DEFAULT);
        return new String (byteArr);
    }
}