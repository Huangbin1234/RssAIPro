package com.hb.rssai.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class ResMessageList {

    /**
     * retObj : {"total":3,"rows":[{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"76fe8d1c-83e4-11e7-841b-b083fe8f693c","title":"121122","userId":null,"content":"neirong","url":null,"mType":null},{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"7c98c8ce-83e4-11e7-841b-b083fe8f693c","title":"sdsddsd","userId":null,"content":"sdfdsfdsf","url":null,"mType":null},{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"927b1362-83e4-11e7-841b-b083fe8f693c","title":"sdsddsd","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2","content":"sdfdsfdsf","url":null,"mType":null}]}
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
         * rows : [{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"76fe8d1c-83e4-11e7-841b-b083fe8f693c","title":"121122","userId":null,"content":"neirong","url":null,"mType":null},{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"7c98c8ce-83e4-11e7-841b-b083fe8f693c","title":"sdsddsd","userId":null,"content":"sdfdsfdsf","url":null,"mType":null},{"deleteFlag":false,"img":null,"isClosed":null,"pubTime":null,"count":null,"id":"927b1362-83e4-11e7-841b-b083fe8f693c","title":"sdsddsd","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2","content":"sdfdsfdsf","url":null,"mType":null}]
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

        public static class RowsBean implements Serializable{
            /**
             * deleteFlag : false
             * img : null
             * isClosed : null
             * pubTime : null
             * count : null
             * id : 76fe8d1c-83e4-11e7-841b-b083fe8f693c
             * title : 121122
             * userId : null
             * content : neirong
             * url : null
             * mType : null
             */

            private boolean deleteFlag;
            private String img;
            private boolean isClosed;
            private String pubTime;
            private long count;
            private String id;
            private String title;
            private String userId;
            private String content;
            private String url;
            private int mType;

            public boolean isDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(boolean deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public boolean getIsClosed() {
                return isClosed;
            }

            public void setIsClosed(boolean isClosed) {
                this.isClosed = isClosed;
            }

            public String getPubTime() {
                return pubTime;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
            }

            public long getCount() {
                return count;
            }

            public void setCount(long count) {
                this.count = count;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getMType() {
                return mType;
            }

            public void setMType(int mType) {
                this.mType = mType;
            }
        }
    }
}
