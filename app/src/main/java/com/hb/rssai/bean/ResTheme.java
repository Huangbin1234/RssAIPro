package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ResTheme {

    /**
     * retObj : {"total":3,"rows":[{"img":"","groupType":1,"createTime":"","name":"A","action":"one","id":"4648e0a2-d0be-11e7-9ee0-b083fe8f693c","sort":1,"url":"","mark":""},{"img":null,"groupType":1,"createTime":null,"name":"B","action":"two","id":"46515b8d-d0be-11e7-9ee0-b083fe8f693c","sort":null,"url":null,"mark":null},{"img":null,"groupType":1,"createTime":null,"name":"C","action":"threee","id":"465c79e8-d0be-11e7-9ee0-b083fe8f693c","sort":null,"url":null,"mark":null}]}
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
         * total : 3
         * rows : [{"img":"","groupType":1,"createTime":"","name":"A","action":"one","id":"4648e0a2-d0be-11e7-9ee0-b083fe8f693c","sort":1,"url":"","mark":""},{"img":null,"groupType":1,"createTime":null,"name":"B","action":"two","id":"46515b8d-d0be-11e7-9ee0-b083fe8f693c","sort":null,"url":null,"mark":null},{"img":null,"groupType":1,"createTime":null,"name":"C","action":"threee","id":"465c79e8-d0be-11e7-9ee0-b083fe8f693c","sort":null,"url":null,"mark":null}]
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
             * img :
             * groupType : 1
             * createTime :
             * name : A
             * action : one
             * id : 4648e0a2-d0be-11e7-9ee0-b083fe8f693c
             * sort : 1
             * url :
             * mark :
             */

            private String img;
            private int groupType;
            private String createTime;
            private String name;
            private String action;
            private String id;
            private int sort;
            private String url;
            private String mark;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }
        }
    }
}
