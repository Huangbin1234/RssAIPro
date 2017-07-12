package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

import com.rss.bean.RSSItemBean;

import java.util.Comparator;

/**
 * 时间排序
 */
public class RssSort implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        RSSItemBean c1 = (RSSItemBean) o1;
        RSSItemBean c2 = (RSSItemBean) o2;
        int flag = c2.getPubDate().compareTo(c1.getPubDate());
        return flag;
    }
}
