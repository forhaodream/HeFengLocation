package com.lh.ch.hefenglocation.model;

import java.util.List;

/**
 * Created by CH on 2017/9/8.
 */

public class CjListModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 259
         * bumen : 29
         * fjbh : 1C02
         * jingdu : 121.796385
         * weidu : 42.902198
         * addtime : 2017-09-08T09:39:11
         * userid : 1015
         * xm : 刘日初
         * bumenname : 敖伦风电场
         */

        private int id;
        private int bumen;
        private String fjbh;
        private String jingdu;
        private String weidu;
        private String addtime;
        private int userid;
        private String xm;
        private String bumenname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBumen() {
            return bumen;
        }

        public void setBumen(int bumen) {
            this.bumen = bumen;
        }

        public String getFjbh() {
            return fjbh;
        }

        public void setFjbh(String fjbh) {
            this.fjbh = fjbh;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getBumenname() {
            return bumenname;
        }

        public void setBumenname(String bumenname) {
            this.bumenname = bumenname;
        }
    }
}
