package com.hb.rssai.event;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class HomeSourceEvent {
    private int message;

    public HomeSourceEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
