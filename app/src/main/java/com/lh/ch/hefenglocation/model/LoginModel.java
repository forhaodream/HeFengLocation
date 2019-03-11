package com.lh.ch.hefenglocation.model;

/**
 * Created by CH on 2017/6/1.
 */

public class LoginModel {


    /**
     * id : 674
     * xm : 韩强
     * username : hanq
     * bumen : 19
     * quanxian : 0
     * errorcode : OK
     */

    private int id;
    private String xm;
    private String username;
    private int bumen;
    private String quanxian;
    private String errorcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBumen() {
        return bumen;
    }

    public void setBumen(int bumen) {
        this.bumen = bumen;
    }

    public String getQuanxian() {
        return quanxian;
    }

    public void setQuanxian(String quanxian) {
        this.quanxian = quanxian;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}
