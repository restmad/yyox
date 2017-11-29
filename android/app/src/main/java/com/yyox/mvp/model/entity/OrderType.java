package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderType implements Serializable {

    private int id;
    private String code;
    private String name;
    private int leadId;
    private boolean status;
    private boolean enable;

    public OrderType(){
        this.id = 0;
        this.code = "";
        this.name = "";
        this.leadId = 0;
        this.status = false;
        this.enable = false;
    }

    public OrderType(int id, String code, String name, int leadId, boolean status, boolean enable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.leadId = leadId;
        this.status = status;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isEnable() {
        return enable;
    }

}
