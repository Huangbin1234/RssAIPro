package com.hb.rssai.event;

/**
 * Created by Administrator on 2017/7/9 0009.
 */
public class OfflineEvent {
    private int progressVal;
    private int  maxVal;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public int getProgressVal() {
        return progressVal;
    }

    public void setProgressVal(int progressVal) {
        this.progressVal = progressVal;
    }
}
