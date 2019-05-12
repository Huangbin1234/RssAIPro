package com.rss.bean;


/**
 * 订阅表
 * Created by Administrator on 2017/8/13 0013.
 */
public class Subscription {
    private String id;
    private String name;//名称
    private String abstractContent;//摘要
    private String link;//链接
    private String img;//图片
    private long count;//订阅人数
    private String pubTime;//发布时间
    private String parentId;//父ID
    private boolean isRecommend;//是否推荐
    private boolean isTag;//false系统添加；true用户添加的源
    private long sort;//排序
    private boolean deleteFlag;
    private String lastTime;//最后一次更新资讯时间
    private int findCount;//发现新资讯数

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    private int dataType;//类型 关联 dataGroup

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public boolean isTag() {
        return isTag;
    }

    public void setTag(boolean tag) {
        isTag = tag;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getFindCount() {
        return findCount;
    }

    public void setFindCount(int findCount) {
        this.findCount = findCount;
    }
}
