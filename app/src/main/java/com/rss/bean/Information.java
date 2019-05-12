package com.rss.bean;

/**
 * 资讯表
 * Created by Administrator on 2017/8/13 0013.
 */

public class Information {
    private String id;
    private long count;//阅读量
    private String title;
    private String abstractContent;
    private String content;
    private String link;
    private String imageUrls;//图片列表,号分割
    private String author;//作者
    private String whereFrom;//来自
    private String pubTime;//发布时间
    private String oprTime;//操作时间
    private int dataType;//数据类型
    private boolean deleteFlag;//删除标记
    private long clickGood;
    private long clickNotGood;
    private String subscribeId;//订阅ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(String whereFrom) {
        this.whereFrom = whereFrom;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getOprTime() {
        return oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public long getClickGood() {
        return clickGood;
    }

    public void setClickGood(long clickGood) {
        this.clickGood = clickGood;
    }

    public long getClickNotGood() {
        return clickNotGood;
    }

    public void setClickNotGood(long clickNotGood) {
        this.clickNotGood = clickNotGood;
    }

    public String getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }
}
