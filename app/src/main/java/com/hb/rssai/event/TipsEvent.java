package com.hb.rssai.event;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class TipsEvent {
    private int message;
    private int newsCount;
    public TipsEvent(int message,int newsCount) {
        this.message = message;
        this.newsCount = newsCount;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }
}
