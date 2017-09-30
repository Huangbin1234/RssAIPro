package com.hb.rssai.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/1 0001.
 */

public class ResBDJson {

    /**
     * status : {"code":"0","msg":"success"}
     * data : {"simi":{"xiangshi_info":{"os":["4259585040,4133174883","1491586810,350029505","1712401602,2425567267","4115998470,3131812613","3014491913,315956468","1879082725,1396671012","345317952,1987808167","2876128374,1841023669","3639178556,3244366108"],"srctype":[0,0,0,0,0,0,0,0,0],"cs":["1683870437,1842320346","768347613,2500523387","3978081972,3730906682","4079959370,884215225","3440028800,1639567298","4168025584,4102999094","554947376,2487846219","2643897120,3634549198","3373404604,411803731"],"simi_value":[0.811625,0.811664,0.8459,0.85614,0.858173,0.867414,0.874143,0.884785,0.894691],"simi_source_from":[0,0,0,0,0,0,0,0,0],"pn":[0,1,2,3,4,5,6,7,8],"di":["62820875700","1204517600","62689556600","125572682290","15420406320","157045188520","61455002840","183716144040","133294597480"],"url":["https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1683870437,1842320346&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=768347613,2500523387&fm=27&gp=0.jpg","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3978081972,3730906682&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4079959370,884215225&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3440028800,1639567298&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4168025584,4102999094&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=554947376,2487846219&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2643897120,3634549198&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3373404604,411803731&fm=27&gp=0.jpg"]},"simi":"simi( s_act=0 s_ach=1 s_sact=0 s_sach=0 s_sacsuc=0 s_kvt=0 s_dxt=4 s_dxh=0 s_sit=0 s_det=0 s_dit=2 s_dih=0 s_prt=0 s_ttt=8 s_pn=0 s_rn=9 s_ret_num=9 s_total_num=20 s_skvn=0 s_acn=200 s_sacn=0 s_idln=0 type=card ftype=-2 query=人工智能 s_query= s_qtype=-1 cs=3869479591,4270070203 simid=20131210,18043083477515805593 srctype=0 mola_res=1 mola_hit=0 https=1 )","sucflg":0,"cache":2},"flag":0,"is_business_query":0}
     */

    private StatusBean status;
    private DataBean data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class StatusBean {
        /**
         * code : 0
         * msg : success
         */

        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class DataBean {
        /**
         * simi : {"xiangshi_info":{"os":["4259585040,4133174883","1491586810,350029505","1712401602,2425567267","4115998470,3131812613","3014491913,315956468","1879082725,1396671012","345317952,1987808167","2876128374,1841023669","3639178556,3244366108"],"srctype":[0,0,0,0,0,0,0,0,0],"cs":["1683870437,1842320346","768347613,2500523387","3978081972,3730906682","4079959370,884215225","3440028800,1639567298","4168025584,4102999094","554947376,2487846219","2643897120,3634549198","3373404604,411803731"],"simi_value":[0.811625,0.811664,0.8459,0.85614,0.858173,0.867414,0.874143,0.884785,0.894691],"simi_source_from":[0,0,0,0,0,0,0,0,0],"pn":[0,1,2,3,4,5,6,7,8],"di":["62820875700","1204517600","62689556600","125572682290","15420406320","157045188520","61455002840","183716144040","133294597480"],"url":["https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1683870437,1842320346&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=768347613,2500523387&fm=27&gp=0.jpg","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3978081972,3730906682&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4079959370,884215225&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3440028800,1639567298&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4168025584,4102999094&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=554947376,2487846219&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2643897120,3634549198&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3373404604,411803731&fm=27&gp=0.jpg"]},"simi":"simi( s_act=0 s_ach=1 s_sact=0 s_sach=0 s_sacsuc=0 s_kvt=0 s_dxt=4 s_dxh=0 s_sit=0 s_det=0 s_dit=2 s_dih=0 s_prt=0 s_ttt=8 s_pn=0 s_rn=9 s_ret_num=9 s_total_num=20 s_skvn=0 s_acn=200 s_sacn=0 s_idln=0 type=card ftype=-2 query=人工智能 s_query= s_qtype=-1 cs=3869479591,4270070203 simid=20131210,18043083477515805593 srctype=0 mola_res=1 mola_hit=0 https=1 )","sucflg":0,"cache":2}
         * flag : 0
         * is_business_query : 0
         */

        private SimiBean simi;
        private int flag;
        private int is_business_query;

        public SimiBean getSimi() {
            return simi;
        }

        public void setSimi(SimiBean simi) {
            this.simi = simi;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getIs_business_query() {
            return is_business_query;
        }

        public void setIs_business_query(int is_business_query) {
            this.is_business_query = is_business_query;
        }

        public static class SimiBean {
            /**
             * xiangshi_info : {"os":["4259585040,4133174883","1491586810,350029505","1712401602,2425567267","4115998470,3131812613","3014491913,315956468","1879082725,1396671012","345317952,1987808167","2876128374,1841023669","3639178556,3244366108"],"srctype":[0,0,0,0,0,0,0,0,0],"cs":["1683870437,1842320346","768347613,2500523387","3978081972,3730906682","4079959370,884215225","3440028800,1639567298","4168025584,4102999094","554947376,2487846219","2643897120,3634549198","3373404604,411803731"],"simi_value":[0.811625,0.811664,0.8459,0.85614,0.858173,0.867414,0.874143,0.884785,0.894691],"simi_source_from":[0,0,0,0,0,0,0,0,0],"pn":[0,1,2,3,4,5,6,7,8],"di":["62820875700","1204517600","62689556600","125572682290","15420406320","157045188520","61455002840","183716144040","133294597480"],"url":["https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1683870437,1842320346&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=768347613,2500523387&fm=27&gp=0.jpg","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3978081972,3730906682&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4079959370,884215225&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3440028800,1639567298&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4168025584,4102999094&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=554947376,2487846219&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2643897120,3634549198&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3373404604,411803731&fm=27&gp=0.jpg"]}
             * simi : simi( s_act=0 s_ach=1 s_sact=0 s_sach=0 s_sacsuc=0 s_kvt=0 s_dxt=4 s_dxh=0 s_sit=0 s_det=0 s_dit=2 s_dih=0 s_prt=0 s_ttt=8 s_pn=0 s_rn=9 s_ret_num=9 s_total_num=20 s_skvn=0 s_acn=200 s_sacn=0 s_idln=0 type=card ftype=-2 query=人工智能 s_query= s_qtype=-1 cs=3869479591,4270070203 simid=20131210,18043083477515805593 srctype=0 mola_res=1 mola_hit=0 https=1 )
             * sucflg : 0
             * cache : 2
             */

            private XiangshiInfoBean xiangshi_info;
            private String simi;
            private int sucflg;
            private int cache;

            public XiangshiInfoBean getXiangshi_info() {
                return xiangshi_info;
            }

            public void setXiangshi_info(XiangshiInfoBean xiangshi_info) {
                this.xiangshi_info = xiangshi_info;
            }

            public String getSimi() {
                return simi;
            }

            public void setSimi(String simi) {
                this.simi = simi;
            }

            public int getSucflg() {
                return sucflg;
            }

            public void setSucflg(int sucflg) {
                this.sucflg = sucflg;
            }

            public int getCache() {
                return cache;
            }

            public void setCache(int cache) {
                this.cache = cache;
            }

            public static class XiangshiInfoBean {
                private List<String> os;
                private List<Integer> srctype;
                private List<String> cs;
                private List<Double> simi_value;
                private List<Integer> simi_source_from;
                private List<Integer> pn;
                private List<String> di;
                private List<String> url;

                public List<String> getOs() {
                    return os;
                }

                public void setOs(List<String> os) {
                    this.os = os;
                }

                public List<Integer> getSrctype() {
                    return srctype;
                }

                public void setSrctype(List<Integer> srctype) {
                    this.srctype = srctype;
                }

                public List<String> getCs() {
                    return cs;
                }

                public void setCs(List<String> cs) {
                    this.cs = cs;
                }

                public List<Double> getSimi_value() {
                    return simi_value;
                }

                public void setSimi_value(List<Double> simi_value) {
                    this.simi_value = simi_value;
                }

                public List<Integer> getSimi_source_from() {
                    return simi_source_from;
                }

                public void setSimi_source_from(List<Integer> simi_source_from) {
                    this.simi_source_from = simi_source_from;
                }

                public List<Integer> getPn() {
                    return pn;
                }

                public void setPn(List<Integer> pn) {
                    this.pn = pn;
                }

                public List<String> getDi() {
                    return di;
                }

                public void setDi(List<String> di) {
                    this.di = di;
                }

                public List<String> getUrl() {
                    return url;
                }

                public void setUrl(List<String> url) {
                    this.url = url;
                }
            }
        }
    }
}
