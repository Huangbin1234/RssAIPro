package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class ResUser {

    /**
     * retObj : {"id":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2","userName":"admin","password":"e10adc3949ba59abbe56e057f20f883e","loginTime":"2017-08-20 13:33:09","registerTime":"","email":"","avatar":"","nickName":"","description":"","readCount":0,"subscribeCount":5}
     * retCode : 0
     * retMsg : 操作成功
     */

    private RetObjBean retObj;
    private int retCode;
    private String retMsg;

    public RetObjBean getRetObj() {
        return retObj;
    }

    public void setRetObj(RetObjBean retObj) {
        this.retObj = retObj;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public static class RetObjBean {
        /**
         * id : ee19fa97-7fda-11e7-99c9-206a8a32e7b2
         * userName : admin
         * password : e10adc3949ba59abbe56e057f20f883e
         * loginTime : 2017-08-20 13:33:09
         * registerTime :
         * email :
         * avatar :
         * nickName :
         * description :
         * readCount : 0
         * subscribeCount : 5
         */

        private String id;
        private String userName;
        private String password;
        private String loginTime;
        private String registerTime;
        private String email;
        private String avatar;
        private String nickName;
        private String description;
        private int readCount;
        private int subscribeCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getReadCount() {
            return readCount;
        }

        public void setReadCount(int readCount) {
            this.readCount = readCount;
        }

        public int getSubscribeCount() {
            return subscribeCount;
        }

        public void setSubscribeCount(int subscribeCount) {
            this.subscribeCount = subscribeCount;
        }
    }
}
