package com.rss.bean;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

/**
 * 详细订阅信息
 * @author Longxuan
 *
 */
public class RssBean {

    /**
     * 显示名称
     */
    private String text;

    /**
     * 标题
     */
    private String title;

    /**
     * rss订阅地址
     */
    private String htmlUrl;

    /**
     * rss订阅地址
     */
    private String xmlUrl;

    /**
     * 版本
     */
    private String version;

    /**
     * 类型
     */
    private String type;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getHtmlUrl() {
        return htmlUrl;
    }
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
    public String getXmlUrl() {
        return xmlUrl;
    }
    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}