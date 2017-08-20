package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ResCollection {

    /**
     * retObj : {"total":2,"rows":[{"img":"1221","createTime":"2017-02-02 11:00:00","link":"www.baidu.com","id":"f731e3dc-819b-11e7-af84-b083fe8f693c","title":"cesss","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2"},{"img":"1221","createTime":"2017-02-02 11:00:00","link":"www.baidu.com","id":"ff566c05-819b-11e7-af84-b083fe8f693c","title":"sasdfsdf","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2"}]}
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
         * rows : [{"img":"1221","createTime":"2017-02-02 11:00:00","link":"www.baidu.com","id":"f731e3dc-819b-11e7-af84-b083fe8f693c","title":"cesss","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2"},{"img":"1221","createTime":"2017-02-02 11:00:00","link":"www.baidu.com","id":"ff566c05-819b-11e7-af84-b083fe8f693c","title":"sasdfsdf","userId":"ee19fa97-7fda-11e7-99c9-206a8a32e7b2"}]
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
             * img : 1221
             * createTime : 2017-02-02 11:00:00
             * link : www.baidu.com
             * id : f731e3dc-819b-11e7-af84-b083fe8f693c
             * title : cesss
             * userId : ee19fa97-7fda-11e7-99c9-206a8a32e7b2
             */

            private String img;
            private String createTime;
            private String link;
            private String id;
            private String title;
            private String userId;
            private String informationId;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getInformationId() {
                return informationId;
            }

            public void setInformationId(String informationId) {
                this.informationId = informationId;
            }
        }
    }
}
