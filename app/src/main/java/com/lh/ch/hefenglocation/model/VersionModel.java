package com.lh.ch.hefenglocation.model;

/**
 * Created by CH on 2017/6/10.
 */
public class VersionModel {

    /**
     * id : 1
     * banben : 10.0
     * url : http://122.114.57.74:806/wuzi/shengji/jianxiudingwei.apk
     * shuoming : app下载
     * wenjiandaxiao : 2.0
     */

    private int id;
    private String banben;
    private String url;
    private String shuoming;
    private double wenjiandaxiao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanben() {
        return banben;
    }

    public void setBanben(String banben) {
        this.banben = banben;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShuoming() {
        return shuoming;
    }

    public void setShuoming(String shuoming) {
        this.shuoming = shuoming;
    }

    public double getWenjiandaxiao() {
        return wenjiandaxiao;
    }

    public void setWenjiandaxiao(double wenjiandaxiao) {
        this.wenjiandaxiao = wenjiandaxiao;
    }
}
