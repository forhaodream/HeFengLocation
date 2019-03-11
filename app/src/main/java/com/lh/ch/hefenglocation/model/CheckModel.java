package com.lh.ch.hefenglocation.model;

import java.util.List;

/**
 * Created by CH on 2017/6/8.
 */

public class CheckModel {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 60
         * userid : 674
         * xm : 韩强
         * sj : 13840539193
         * jingdu : 123.454379
         * weidu : 41.72821
         * paizhaoshijian : 2017-06-07T17:08:11
         * pic : http://192.168.0.27:8052/upload/20170607050811.jpg
         * bumen : 19
         * bumenname : 芳山风电场
         * paizhaoshijian2 : 2017年06月07日 05时08分
         */

        private int id;
        private int userid;
        private String xm;
        private String sj;
        private String jingdu;
        private String weidu;
        private String paizhaoshijian;
        private String pic;
        private int bumen;
        private String bumenname;
        private String paizhaoshijian2;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getXm() {
            return xm;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getSj() {
            return sj;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public String getJingdu() {
            return jingdu;
        }

        public void setJingdu(String jingdu) {
            this.jingdu = jingdu;
        }

        public String getWeidu() {
            return weidu;
        }

        public void setWeidu(String weidu) {
            this.weidu = weidu;
        }

        public String getPaizhaoshijian() {
            return paizhaoshijian;
        }

        public void setPaizhaoshijian(String paizhaoshijian) {
            this.paizhaoshijian = paizhaoshijian;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getBumen() {
            return bumen;
        }

        public void setBumen(int bumen) {
            this.bumen = bumen;
        }

        public String getBumenname() {
            return bumenname;
        }

        public void setBumenname(String bumenname) {
            this.bumenname = bumenname;
        }

        public String getPaizhaoshijian2() {
            return paizhaoshijian2;
        }

        public void setPaizhaoshijian2(String paizhaoshijian2) {
            this.paizhaoshijian2 = paizhaoshijian2;
        }
    }
}
