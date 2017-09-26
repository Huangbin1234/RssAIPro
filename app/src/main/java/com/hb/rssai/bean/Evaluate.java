package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/9/26.
 */

public class Evaluate {
    private String clickGood;//0未评价 1已点击 2已取消
    private String clickNotGood;//0未评价 1已点击 2已取消
    private String informationId;

    public String getClickGood() {
        return clickGood;
    }

    public void setClickGood(String clickGood) {
        this.clickGood = clickGood;
    }

    public String getClickNotGood() {
        return clickNotGood;
    }

    public void setClickNotGood(String clickNotGood) {
        this.clickNotGood = clickNotGood;
    }

    public String getInformationId() {
        return informationId;
    }

    public void setInformationId(String informationId) {
        this.informationId = informationId;
    }
}
