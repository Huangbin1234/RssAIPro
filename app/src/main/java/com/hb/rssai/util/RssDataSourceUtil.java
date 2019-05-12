package com.hb.rssai.util;

import android.app.Activity;

import com.hb.rssai.bean.RssSource;
import com.rss.bean.Website;
import com.rss.util.Dom4jUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class RssDataSourceUtil {

    /**
     * 从XML中读取数据源
     *
     * @param act
     * @return
     */
    public static List<Website> readFromAsset(Activity act) {
        InputStream in = null;
        List<Website> websiteList = null;
        try {
            in = act.getResources().getAssets().open("website.xml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (in == null) {
            System.out.println("xml文件不存在");
            return null;
        }
        websiteList = new Dom4jUtil().parserXml(in);
        return websiteList;
    }

    /**
     * 从DB中读取数据源
     *
     * @return
     */
    public static List<Website> readFromDb() {
        List<RssSource> dbList = LiteOrmDBUtil.getQueryAll(RssSource.class);
        if (dbList == null || dbList.size() <= 0) {
            return null;
        }
        List<Website> websiteList = new ArrayList<>();
        for (RssSource rssSource : dbList) {
            Website website = new Website();
            website.setUrl(rssSource.getLink());
            website.setName(rssSource.getName());
            website.setOpen("true");
            website.setEncoding("UTF-8");
            website.setStartTag("");
            website.setEndTag("");
            website.setFid("" + rssSource.getId());
            websiteList.add(website);
        }
        return websiteList;
    }

    /**
     * 可以选择插入到数据库
     *
     * @param website
     */
//    public static List<RSSItemBean> getRssData(Website website, int count) {
//        if (!website.getOpen().equals("true")) {
//            return null;
//        }
//        List<RSSItemBean> rssList = new ArrayList<>();
//        try {
//            List<RSSItemBean> rssTempList = new FeedReader().getContent(website).getRSSItemBeen();                   //获取有内容的 rssItemBean
//            if (rssTempList != null) {
//                if (rssTempList.size() > count && count != -1) {
//                    rssList.addAll(rssTempList.subList(0, 4));
//                } else {
//                    rssList.addAll(rssTempList);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rssList;
//    }
}
