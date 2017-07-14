package com.hb.rssai.event;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class RssSourceEvent {
    private int message;

    public RssSourceEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
