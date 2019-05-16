package com.hb.rssai.bean;

/**
 * Created by Administrator
 * 2019/5/5 0005
 */
public class SubParams {
    private String id;
    private String name;
    private String abstractContent;
    private String link;
    private String img;
    private boolean isRecommend;
    private String sort;
    private boolean isTag;
    private int dataType;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}
