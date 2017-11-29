package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-02-04.
 */

public class Region {

    private int id;
    private String regionname;
    private int child;

    public Region(){
        this.id = 0;
        this.regionname = "";
        this.child = 0;
    }

    public Region(int id, String regionname, int child) {
        this.id = id;
        this.regionname = regionname;
        this.child = child;
    }

    public int getId() {
        return id;
    }

    public String getRegionname() {
        return regionname;
    }

    public int getChild() {
        return child;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public void setChild(int child) {
        this.child = child;
    }
}
