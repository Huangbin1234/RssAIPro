package com.hb.rssai.bean;

import com.rss.bean.RSSItemBean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class RssChannel {
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private ImageBean image;
    private Date pubDate;

    public static class ImageBean {
        private String url;
        private String title;
        private String link;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    private List<RSSItemBean> mRSSItemBeen;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public List<RSSItemBean> getRSSItemBeen() {
        return mRSSItemBeen;
    }

    public void setRSSItemBeen(List<RSSItemBean> RSSItemBeen) {
        mRSSItemBeen = RSSItemBeen;
    }
}
