package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator
 * on 2019/4/15
 */
public class ResAdviceList {

    /**
     * retCode : 0
     * retMsg : 操作成功
     * retObj : {"total":1,"rows":[{"createTime":"2019-04-15 08:45:25","nickName":"usf","adviceTypeName":"BUG","id":"942b9253-c438-46a9-b79b-298d28ec4869","adviceType":0,"content":"添加rss源链接，不能粘贴","mark":"测试"}]}
     */

    private int retCode;
    private String retMsg;
    private RetObjBean retObj;

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

    public RetObjBean getRetObj() {
        return retObj;
    }

    public void setRetObj(RetObjBean retObj) {
        this.retObj = retObj;
    }

    public static class RetObjBean {
        /**
         * total : 1.0
         * rows : [{"createTime":"2019-04-15 08:45:25","nickName":"usf","adviceTypeName":"BUG","id":"942b9253-c438-46a9-b79b-298d28ec4869","adviceType":0,"content":"添加rss源链接，不能粘贴","mark":"测试"}]
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
             * createTime : 2019-04-15 08:45:25
             * nickName : usf
             * adviceTypeName : BUG
             * id : 942b9253-c438-46a9-b79b-298d28ec4869
             * adviceType : 0.0
             * content : 添加rss源链接，不能粘贴
             * mark : 测试
             */

            private String createTime;
            private String nickName;
            private String adviceTypeName;
            private String id;
            private int adviceType;
            private String content;
            private String mark;
            private String updateTime;
            private String avatar;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAdviceTypeName() {
                return adviceTypeName;
            }

            public void setAdviceTypeName(String adviceTypeName) {
                this.adviceTypeName = adviceTypeName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getAdviceType() {
                return adviceType;
            }

            public void setAdviceType(int adviceType) {
                this.adviceType = adviceType;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
