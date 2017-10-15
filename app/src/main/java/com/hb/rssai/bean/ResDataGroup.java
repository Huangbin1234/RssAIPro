package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class ResDataGroup {

    /**
     * retObj : {"total":7,"rows":[{"val":1,"deleteFlag":false,"name":"新闻","id":"783a53b2-836e-11e7-a746-206a8a32e7b2"},{"val":2,"deleteFlag":false,"name":"科技","id":"86a5bfb1-836e-11e7-a746-206a8a32e7b2"},{"val":3,"deleteFlag":false,"name":"探索","id":"8d9a822f-836e-11e7-a746-206a8a32e7b2"},{"val":4,"deleteFlag":false,"name":"军事","id":"95b777a3-836e-11e7-a746-206a8a32e7b2"},{"val":5,"deleteFlag":false,"name":"娱乐","id":"9bd0b519-836e-11e7-a746-206a8a32e7b2"},{"val":6,"deleteFlag":false,"name":"数码","id":"a2616aa0-836e-11e7-a746-206a8a32e7b2"},{"val":7,"deleteFlag":false,"name":"游戏","id":"a650262d-836e-11e7-a746-206a8a32e7b2"}]}
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
         * total : 7
         * rows : [{"val":1,"deleteFlag":false,"name":"新闻","id":"783a53b2-836e-11e7-a746-206a8a32e7b2"},{"val":2,"deleteFlag":false,"name":"科技","id":"86a5bfb1-836e-11e7-a746-206a8a32e7b2"},{"val":3,"deleteFlag":false,"name":"探索","id":"8d9a822f-836e-11e7-a746-206a8a32e7b2"},{"val":4,"deleteFlag":false,"name":"军事","id":"95b777a3-836e-11e7-a746-206a8a32e7b2"},{"val":5,"deleteFlag":false,"name":"娱乐","id":"9bd0b519-836e-11e7-a746-206a8a32e7b2"},{"val":6,"deleteFlag":false,"name":"数码","id":"a2616aa0-836e-11e7-a746-206a8a32e7b2"},{"val":7,"deleteFlag":false,"name":"游戏","id":"a650262d-836e-11e7-a746-206a8a32e7b2"}]
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
             * val : 1
             * deleteFlag : false
             * name : 新闻
             * id : 783a53b2-836e-11e7-a746-206a8a32e7b2
             */

            private int val;
            private boolean deleteFlag;
            private String name;
            private String id;
            private String url;
            private int groupType;
            private int maxVal;
            private int progressVal;
            public int getVal() {
                return val;
            }

            public void setVal(int val) {
                this.val = val;
            }

            public boolean isDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(boolean deleteFlag) {
                this.deleteFlag = deleteFlag;
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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getGroupType() {
                return groupType;
            }

            public void setGroupType(int groupType) {
                this.groupType = groupType;
            }

            public int getProgressVal() {
                return progressVal;
            }

            public void setProgressVal(int progressVal) {
                this.progressVal = progressVal;
            }

            public int getMaxVal() {
                return maxVal;
            }

            public void setMaxVal(int maxVal) {
                this.maxVal = maxVal;
            }
        }
    }
}
