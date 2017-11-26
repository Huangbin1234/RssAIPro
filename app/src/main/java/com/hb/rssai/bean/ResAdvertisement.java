package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/11/26 0026.
 */

public class ResAdvertisement {

    /**
     * retObj : {"proposal":"22332","deleteFlag":0,"img":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2560378765,2884107603&fm=173&s=DF378D4406233ABCBA1F851B0300E0DA&w=218&h=146&img.JPEG","groupType":1,"createTime":"12","link":"www.baidu.com","id":"2c2ee22a-d1ed-11e7-a29a-206a8a32e7b2","title":"111","mark":"21"}
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
         * proposal : 22332
         * deleteFlag : 0
         * img : https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2560378765,2884107603&fm=173&s=DF378D4406233ABCBA1F851B0300E0DA&w=218&h=146&img.JPEG
         * groupType : 1
         * createTime : 12
         * link : www.baidu.com
         * id : 2c2ee22a-d1ed-11e7-a29a-206a8a32e7b2
         * title : 111
         * mark : 21
         */

        private String proposal;
        private int deleteFlag;
        private String img;
        private int groupType;
        private String createTime;
        private String link;
        private String id;
        private String title;
        private String mark;

        public String getProposal() {
            return proposal;
        }

        public void setProposal(String proposal) {
            this.proposal = proposal;
        }

        public int getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(int deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

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

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }
    }
}
