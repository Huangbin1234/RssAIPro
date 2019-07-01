package com.rss.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator
 * on 2019/5/17
 */
public class StringUtil {
    /**
     * 提取，号分隔的列表
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 提取字符串内所有的img标签下的src
     *
     * @param content
     * @return
     */
    public static List<String> getRegexImages(String content) {
        String regex;
        String groupStr;
        List<String> list = new ArrayList<String>();
        //提取字符串中的img标签
        regex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(content);
        while (ma.find()) {
            //提取字符串中的src路径
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(ma.group());
            while (m.find()) {
                groupStr = m.group(1);
                if (null != groupStr && !"".equals(groupStr)) {
                    if ("http".equals(groupStr.substring(0, 4)) && !"http://simg.sinajs.cn/blog7style/images/special/1265.gif".equals(groupStr)) {//只提取http开头的图片地址
                        if (list.size() >= 3) {
                            break;
                        }
                        list.add(groupStr);
                    } else if (!"http".equals(groupStr.substring(0, 4)) && (
                            groupStr.endsWith(".jpg")
                                    || groupStr.endsWith(".JPEG")
                                    || groupStr.endsWith(".JPG")
                                    || groupStr.endsWith(".png")
                                    || groupStr.endsWith(".PNG")
                                    || groupStr.endsWith(".GIF")
                                    || groupStr.endsWith(".gif")
                                    || groupStr.endsWith(".BMP")
                                    || groupStr.endsWith(".bmp")
                                    || groupStr.endsWith(".BMP")
                                    || groupStr.endsWith(".bmp"))) {
                        if (list.size() >= 3) {
                            break;
                        }
                        if (groupStr.startsWith("//")) {
                            list.add("http:" + groupStr);
                        } else {
                            list.add("http://" + groupStr);
                        }
                    }
                }
            }
        }
        return list;
    }
    /**
     * 提取字符串内所有的img标签下的src
     *
     * @param content
     * @return
     */
    public static List<String> getAllRegexImages(String content) {
        String regex;
        String groupStr;
        List<String> list = new ArrayList<String>();
        //提取字符串中的img标签
        regex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(content);
        while (ma.find()) {
            //提取字符串中的src路径
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(ma.group());
            while (m.find()) {
                groupStr = m.group(1);
                if (null != groupStr && !"".equals(groupStr)) {
                    if ("http".equals(groupStr.substring(0, 4)) && !"http://simg.sinajs.cn/blog7style/images/special/1265.gif".equals(groupStr)) {//只提取http开头的图片地址

                        list.add(groupStr);
                    } else if (!"http".equals(groupStr.substring(0, 4)) && (
                            groupStr.endsWith(".jpg")
                                    || groupStr.endsWith(".JPEG")
                                    || groupStr.endsWith(".JPG")
                                    || groupStr.endsWith(".png")
                                    || groupStr.endsWith(".PNG")
                                    || groupStr.endsWith(".GIF")
                                    || groupStr.endsWith(".gif")
                                    || groupStr.endsWith(".BMP")
                                    || groupStr.endsWith(".bmp")
                                    || groupStr.endsWith(".BMP")
                                    || groupStr.endsWith(".bmp"))) {

                        if (groupStr.startsWith("//")) {
                            list.add("http:" + groupStr);
                        } else {
                            list.add("http://" + groupStr);
                        }
                    }
                }
            }
        }
        return list;
    }
    /**
     * 格式化字符串
     * @param s
     * @return
     */
     public static String formatStr(String s) {
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return m.group(1);
        }
        String res = s.replace("\r", "");
        res = res.replace("\t", "");
        res = res.replace("\n", "");
        return res;
    }


    public static String filter(String s) {
        return s.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
    }


    public static InputStream streamToString(InputStream inputStream) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = inputStream.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        String res = StringUtil.filter(out.toString());
        return new ByteArrayInputStream(res.getBytes());
    }
}
