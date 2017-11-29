package com.yyox.mvp.model.entity;

/**
 * Created by 95 on 2017/5/17.
 */
public class OrderTypes {

    private int id;
    private String code;
    private String name;
    private String description;
    private int leadId;
    private int maxSingleValue;
    private boolean status;
    private boolean enable;

    public OrderTypes() {
        this.id = 0;
        this.code = "";
        this.name = "";
        this.description = "";
        this.leadId = 0;
        this.maxSingleValue = 0;
        this.status = false;
        this.enable = false;
    }

    public OrderTypes(int id, String code, String name, String description, int leadId, int maxSingleValue, boolean status, boolean enable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.leadId = leadId;
        this.maxSingleValue = maxSingleValue;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public int getMaxSingleValue() {
        return maxSingleValue;
    }

    public void setMaxSingleValue(int maxSingleValue) {
        this.maxSingleValue = maxSingleValue;
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

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
