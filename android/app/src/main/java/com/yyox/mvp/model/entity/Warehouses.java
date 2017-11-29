package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by 95 on 2017/5/22.
 */
public class Warehouses implements Serializable {
    private String name;
    private String value;
    private int type;

    public Warehouses(String name, String value, int type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Warehouses() {
        this.name = "";
        this.value = "";
        this.type = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
