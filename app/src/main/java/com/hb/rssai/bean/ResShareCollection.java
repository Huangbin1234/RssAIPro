package com.hb.rssai.bean;

/**
 * Created by Administrator on 2017/9/30.
 */

public class ResShareCollection {

    /**
     * retObj : {"id":"f00ab816-8ce6-4763-a55b-b808764a83ba","count":3,"title":"Sprint成功将无人机变成迷你信号塔，明年上线使用","abstractContent":"\n                        <p>还记得《刀剑神域：序列之争》中一个场景吗？都市的空中遍布着无人机，而这些无人机正为大家佩戴的AR设备提供信号。现在，似乎美国第四大运营商Sprint已经成功将无人机变成迷你信号塔了。<\/p><p><img src=\"http://img.ithome.com/newsuploadfiles/2017/9/20170928133645_9617.jpg\" w=\"600\" h=\"398\"/><\/p><p>美国第四大运营商Sprint在本周宣布，他们已经成功在无人机上部署Magic Box信号增强技术，能够为最大覆盖范围10平方英里的小区提供临时信号覆盖。上周，公司已经在达拉斯郊外30英里的地方进行了轻量型迷你信号塔的的测试。<\/p><p>Sprint的首席运营官Günther Ottendorfer表示，装备了信号放大器的无人机能够改善该地区的网络覆盖。由于无人机能够在400英尺高空飞行因此具备更大的网络覆盖范围。他补充道，目前还需要大量的工作来减轻Magic Box的重量，预估会在明年上线使用。<\/p><p>Sprint在今年早些时候就推出了Magic Box，这款盒子和家庭中常见的鞋盒一般大小，它主要通过Sprint的2.5GHz频谱来改善室内的4G LTE网络服务。不过和其他信号增强器不同，Magic Box并不需要宽带连接，它是专门使用专门的蜂窝通道连接到附近的Sprint信号塔。<\/p><p>如果说以后我们的城市的上空中，到处都飞着无人机，这些无人机可以为我们提供更稳定更快速的网络覆盖，你觉得如何？<\/p>\n                    ","content":"","link":"https://www.ithome.com/html/it/327939.htm","imageUrls":"http://img.ithome.com/newsuploadfiles/2017/9/20170928133645_9617.jpg","author":"","whereFrom":"IT之家","pubTime":"2017-09-28 13:38:08","oprTime":"2017-09-28 13:45:15","dataType":2,"deleteFlag":false,"clickGood":0,"clickNotGood":0}
     * retMsg : 操作成功
     * retCode : 0
     */

    private RetObjBean retObj;
    private String retMsg;
    private int retCode;

    public RetObjBean getRetObj() {
        return retObj;
    }

    public void setRetObj(RetObjBean retObj) {
        this.retObj = retObj;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public static class RetObjBean {
        /**
         * id : f00ab816-8ce6-4763-a55b-b808764a83ba
         * count : 3
         * title : Sprint成功将无人机变成迷你信号塔，明年上线使用
         * abstractContent :
         <p>还记得《刀剑神域：序列之争》中一个场景吗？都市的空中遍布着无人机，而这些无人机正为大家佩戴的AR设备提供信号。现在，似乎美国第四大运营商Sprint已经成功将无人机变成迷你信号塔了。</p><p><img src="http://img.ithome.com/newsuploadfiles/2017/9/20170928133645_9617.jpg" w="600" h="398"/></p><p>美国第四大运营商Sprint在本周宣布，他们已经成功在无人机上部署Magic Box信号增强技术，能够为最大覆盖范围10平方英里的小区提供临时信号覆盖。上周，公司已经在达拉斯郊外30英里的地方进行了轻量型迷你信号塔的的测试。</p><p>Sprint的首席运营官Günther Ottendorfer表示，装备了信号放大器的无人机能够改善该地区的网络覆盖。由于无人机能够在400英尺高空飞行因此具备更大的网络覆盖范围。他补充道，目前还需要大量的工作来减轻Magic Box的重量，预估会在明年上线使用。</p><p>Sprint在今年早些时候就推出了Magic Box，这款盒子和家庭中常见的鞋盒一般大小，它主要通过Sprint的2.5GHz频谱来改善室内的4G LTE网络服务。不过和其他信号增强器不同，Magic Box并不需要宽带连接，它是专门使用专门的蜂窝通道连接到附近的Sprint信号塔。</p><p>如果说以后我们的城市的上空中，到处都飞着无人机，这些无人机可以为我们提供更稳定更快速的网络覆盖，你觉得如何？</p>

         * content :
         * link : https://www.ithome.com/html/it/327939.htm
         * imageUrls : http://img.ithome.com/newsuploadfiles/2017/9/20170928133645_9617.jpg
         * author :
         * whereFrom : IT之家
         * pubTime : 2017-09-28 13:38:08
         * oprTime : 2017-09-28 13:45:15
         * dataType : 2
         * deleteFlag : false
         * clickGood : 0
         * clickNotGood : 0
         */

        private String id;
        private int count;
        private String title;
        private String abstractContent;
        private String content;
        private String link;
        private String imageUrls;
        private String author;
        private String whereFrom;
        private String pubTime;
        private String oprTime;
        private int dataType;
        private boolean deleteFlag;
        private int clickGood;
        private int clickNotGood;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbstractContent() {
            return abstractContent;
        }

        public void setAbstractContent(String abstractContent) {
            this.abstractContent = abstractContent;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(String imageUrls) {
            this.imageUrls = imageUrls;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getWhereFrom() {
            return whereFrom;
        }

        public void setWhereFrom(String whereFrom) {
            this.whereFrom = whereFrom;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getOprTime() {
            return oprTime;
        }

        public void setOprTime(String oprTime) {
            this.oprTime = oprTime;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public boolean isDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(boolean deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public int getClickGood() {
            return clickGood;
        }

        public void setClickGood(int clickGood) {
            this.clickGood = clickGood;
        }

        public int getClickNotGood() {
            return clickNotGood;
        }

        public void setClickNotGood(int clickNotGood) {
            this.clickNotGood = clickNotGood;
        }
    }
}
