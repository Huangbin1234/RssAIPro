package com.rss.bean;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

import java.util.List;

/**
 * rss分组信息
 * @author Longxuan
 *
 */
public class RssTeamBean {

    /**
     * 分组标题
     */
    private String title;

    /**
     * 分组名称
     */
    private String text;

    private List<RssBean> rssBeanList ;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public List<RssBean> getRssBeanList() {
        return rssBeanList;
    }
    public void setRssBeanList(List<RssBean> rssBeanList) {
        this.rssBeanList = rssBeanList;
    }

}
