package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class ResCollectionBean {

    /**
     * retObj : {"id":"12e30945-e7e8-449b-982a-94f9487c2bc5","title":"微软推出三款新机器学习工具：开发者可快速打造AI应用","link":"https://www.ithome.com/html/win10/327390.htm","createTime":"2017-09-25 22:33:47","img":"","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2","deleteFlag":false,"informationId":"c1cb37b0-87b8-4aa1-8138-1ac12dc9b8c1"}
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
         * id : 12e30945-e7e8-449b-982a-94f9487c2bc5
         * title : 微软推出三款新机器学习工具：开发者可快速打造AI应用
         * link : https://www.ithome.com/html/win10/327390.htm
         * createTime : 2017-09-25 22:33:47
         * img :
         * userId : ee19fa97-7fda-11e7-99c9-206a8a32e7b2
         * deleteFlag : false
         * informationId : c1cb37b0-87b8-4aa1-8138-1ac12dc9b8c1
         */

        private String id;
        private String title;
        private String link;
        private String createTime;
        private String img;
        private String userId;
        private boolean deleteFlag;
        private String informationId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(boolean deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getInformationId() {
            return informationId;
        }

        public void setInformationId(String informationId) {
            this.informationId = informationId;
        }
    }
}

