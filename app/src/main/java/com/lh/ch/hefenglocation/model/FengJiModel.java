package com.lh.ch.hefenglocation.model;

import java.util.List;

/**
 * Created by CH on 2017/6/8.
 */

public class FengJiModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 12
         * bumen : 17
         * fjbh : 2407
         * jingdu : 120.28452
         * weidu : 40.17096
         */

        private int id;
        private int bumen;
        private String fjbh;
        private String jingdu;
        private String weidu;

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
    }
}
