package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class ResInformation {

    /**
     * retObj : {"total":2,"rows":[{"author":"张三","pubTime":"2017-08-17 21:22:00","dataType":1,"abstractContent":"摘要","count":12,"link":"122121","whereFrom":"来自网络","title":"测试标题","content":"内容测试","deleteFlag":false,"imageUrls":"12221","id":"5c82ce29-8354-11e7-a746-206a8a32e7b2","oprTime":"2017-08-17 21:22:00"},{"author":"张三","pubTime":"2017-08-17 21:22:00","dataType":1,"abstractContent":"摘要","count":12,"link":"122121","whereFrom":"来自网络","title":"测试标题","content":"内容测试","deleteFlag":false,"imageUrls":"12221","id":"5ef198b4-8354-11e7-a746-206a8a32e7b2","oprTime":"2017-08-17 21:22:00"}]}
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
         * total : 2
         * rows : [{"author":"张三","pubTime":"2017-08-17 21:22:00","dataType":1,"abstractContent":"摘要","count":12,"link":"122121","whereFrom":"来自网络","title":"测试标题","content":"内容测试","deleteFlag":false,"imageUrls":"12221","id":"5c82ce29-8354-11e7-a746-206a8a32e7b2","oprTime":"2017-08-17 21:22:00"},{"author":"张三","pubTime":"2017-08-17 21:22:00","dataType":1,"abstractContent":"摘要","count":12,"link":"122121","whereFrom":"来自网络","title":"测试标题","content":"内容测试","deleteFlag":false,"imageUrls":"12221","id":"5ef198b4-8354-11e7-a746-206a8a32e7b2","oprTime":"2017-08-17 21:22:00"}]
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * author : 张三
             * pubTime : 2017-08-17 21:22:00
             * dataType : 1
             * abstractContent : 摘要
             * count : 12
             * link : 122121
             * whereFrom : 来自网络
             * title : 测试标题
             * content : 内容测试
             * deleteFlag : false
             * imageUrls : 12221
             * id : 5c82ce29-8354-11e7-a746-206a8a32e7b2
             * oprTime : 2017-08-17 21:22:00
             */

            private String author;
            private String pubTime;
            private int dataType;
            private String abstractContent;
            private int count;
            private String link;
            private String whereFrom;
            private String title;
            private String content;
            private boolean deleteFlag;
            private String imageUrls;
            private String id;
            private String oprTime;
            private long clickGood;
            private long clickNotGood;
            private String subscribeImg;

            public int getViewType() {
                return viewType;
            }

            public void setViewType(int viewType) {
                this.viewType = viewType;
            }

            private int viewType;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getPubTime() {
                return pubTime;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
            }

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
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

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getWhereFrom() {
                return whereFrom;
            }

            public void setWhereFrom(String whereFrom) {
                this.whereFrom = whereFrom;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(boolean deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public String getImageUrls() {
                return imageUrls;
            }

            public void setImageUrls(String imageUrls) {
                this.imageUrls = imageUrls;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOprTime() {
                return oprTime;
            }

            public void setOprTime(String oprTime) {
                this.oprTime = oprTime;
            }

            public long getClickNotGood() {
                return clickNotGood;
            }

            public void setClickNotGood(long clickNotGood) {
                this.clickNotGood = clickNotGood;
            }

            public long getClickGood() {
                return clickGood;
            }

            public void setClickGood(long clickGood) {
                this.clickGood = clickGood;
            }

            public String getSubscribeImg() {
                return subscribeImg;
            }

            public void setSubscribeImg(String subscribeImg) {
                this.subscribeImg = subscribeImg;
            }
        }
    }
}
