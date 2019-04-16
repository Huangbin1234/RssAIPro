package com.hb.rssai.bean;

import java.io.Serializable;

/**
 * Created by Administrator
 * 2019/4/16 0016
 */
public class CommentEntity implements Serializable {
    private String content;
    private String type;
    private String typeName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
