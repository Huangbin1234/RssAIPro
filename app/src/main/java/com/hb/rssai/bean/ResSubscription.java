package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ResSubscription {

    /**
     * retObj : {"deleteFlag":false,"isRecommend":true,"img":"http://songshuhui.net/wp-content/themes/isongshu/images/team.jpg","link":"http://songshuhui.net/feed","name":"科学松鼠会","abstractContent":"剥开科学的坚果，让科学流行起来","count":4,"id":"78588067-81bb-11e7-804f-206a8a32e7b2","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2"}
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
         * deleteFlag : false
         * isRecommend : true
         * img : http://songshuhui.net/wp-content/themes/isongshu/images/team.jpg
         * link : http://songshuhui.net/feed
         * name : 科学松鼠会
         * abstractContent : 剥开科学的坚果，让科学流行起来
         * count : 4
         * id : 78588067-81bb-11e7-804f-206a8a32e7b2
         * userId : ee19fa97-7fda-11e7-99c9-206a8a32e7b2
         */
        private boolean isCheck;
        private boolean deleteFlag;
        private boolean isRecommend;
        private String img;
        private String link;
        private String name;
        private String abstractContent;
        private int count;
        private String id;
        private String userId;

        public boolean isDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(boolean deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public boolean isIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(boolean isRecommend) {
            this.isRecommend = isRecommend;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAbstractContent() {
            return abstractContent;
        }

        public void setAbstractContent(String abstractContent) {
            this.abstractContent = abstractContent;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
