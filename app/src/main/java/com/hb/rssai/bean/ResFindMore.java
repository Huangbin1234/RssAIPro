package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/6 0006.
 */

public class ResFindMore {

    /**
     * retObj : {"total":11,"rows":[{"isRecommend":true,"img":"\"\"","pubTime":"2017-08-16 00:00:00","abstractContent":"\"\"","count":12,"link":"www.baidu.com","name":"测试1","id":"7dc6dc90-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":"\"\""},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"7f202c21-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"7fb2c3c8-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"8037004f-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"80bdc362-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"813cecf3-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":null,"link":"www.baidu.com","name":"测试1","id":"9b2e76d3-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a09f19dd-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a1304dee-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a1a7e24b-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a22ad9d1-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null}]}
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
         * total : 11
         * rows : [{"isRecommend":true,"img":"\"\"","pubTime":"2017-08-16 00:00:00","abstractContent":"\"\"","count":12,"link":"www.baidu.com","name":"测试1","id":"7dc6dc90-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":"\"\""},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"7f202c21-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"7fb2c3c8-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"8037004f-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"80bdc362-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":null,"count":null,"link":"www.baidu.com","name":"测试1","id":"813cecf3-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":null,"link":"www.baidu.com","name":"测试1","id":"9b2e76d3-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a09f19dd-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a1304dee-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a1a7e24b-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null},{"isRecommend":true,"img":null,"pubTime":"2017-08-16 00:00:00","abstractContent":"12sadxzasdsadasdasdasdasdsadsad","count":1245,"link":"www.baidu.com","name":"测试1","id":"a22ad9d1-816d-11e7-af84-b083fe8f693c","isTag":false,"parentId":null}]
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
             * isRecommend : true
             * img : ""
             * pubTime : 2017-08-16 00:00:00
             * abstractContent : ""
             * count : 12
             * link : www.baidu.com
             * name : 测试1
             * id : 7dc6dc90-816d-11e7-af84-b083fe8f693c
             * isTag : false
             * parentId : ""
             */

            private boolean deleteFlag;
            private boolean isRecommend;
            private String userId;
            private String img;
            private String pubTime;
            private String abstractContent;
            private int count;
            private String link;
            private String name;
            private String id;
            private boolean isTag;
            private String parentId;
            private long sort;
            private String usId;//用户订阅关系ID
            private int findCount;//发现新资讯数

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

            public String getPubTime() {
                return pubTime;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsTag() {
                return isTag;
            }

            public void setIsTag(boolean isTag) {
                this.isTag = isTag;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public long getSort() {
                return sort;
            }

            public void setSort(long sort) {
                this.sort = sort;
            }

            public String getUsId() {
                return usId;
            }

            public void setUsId(String usId) {
                this.usId = usId;
            }

            public int getFindCount() {
                return findCount;
            }

            public void setFindCount(int findCount) {
                this.findCount = findCount;
            }

            public boolean isDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(boolean deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
