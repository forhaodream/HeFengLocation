package com.lh.ch.hefenglocation.model;

import java.util.List;

/**
 * Created by CH on 2017/6/1.
 */

public class TelModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * username : 111
         * password : 111
         * xm : aaaa
         * sj : 111
         */

        private String username;
        private String password;
        private String xm;
        private String sj;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
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
    }
}
