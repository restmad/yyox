package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class Site implements Serializable{

    private int id;
    private String name;
    private String logo;
    private String detail;
    private String url;
    private int status;
    private boolean edit;

    public Site(){
        this.id = 0;
        this.name = "";
        this.logo = "";
        this.detail = "";
        this.url = "";
        this.status = 0;
        this.edit = false;
    }

    public Site(int id, String name, String logo, String detail, String url, int status, boolean edit) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.detail = detail;
        this.url = url;
        this.status = status;
        this.edit = edit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
