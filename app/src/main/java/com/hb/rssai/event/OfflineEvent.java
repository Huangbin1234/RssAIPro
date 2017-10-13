package com.hb.rssai.event;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class OfflineEvent {
    private int message;
    private String content;

    public OfflineEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
