package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class ResBase {

    /**
     * retObj : null
     * retMsg : 操作成功
     * retCode : 0
     */

    private Object retObj;
    private String retMsg;
    private int retCode;

    public Object getRetObj() {
        return retObj;
    }

    public void setRetObj(Object retObj) {
        this.retObj = retObj;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
