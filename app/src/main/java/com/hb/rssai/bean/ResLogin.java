package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class ResLogin {

    /**
     * retObj : null
     * retMsg : 操作成功
     * retCode : 0
     */


    private RetObjectBean retObj;
    private String retMsg;
    private int retCode;

    public RetObjectBean getRetObj() {
        return retObj;
    }

    public void setRetObj(RetObjectBean retObj) {
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

    public class RetObjectBean {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        private String token;
    }
}
