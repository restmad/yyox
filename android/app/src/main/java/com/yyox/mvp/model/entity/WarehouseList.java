package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by 95 on 2017/5/22.
 */
public class WarehouseList implements Serializable{
    private String name;
    private int type;
    private String value;

    public WarehouseList(String name, int type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public WarehouseList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
