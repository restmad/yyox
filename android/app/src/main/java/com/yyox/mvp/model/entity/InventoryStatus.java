package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-02-07.
 */

public class InventoryStatus implements Serializable {

    private int id;
    private String code;
    private String status;
    private String description;
    private String customerAliasStatus;
    private int rank;

    public InventoryStatus(){
        this.id = 0;
        this.code = "";
        this.status = "";
        this.description = "";
        this.customerAliasStatus = "";
        this.rank = 0;
    }

    public InventoryStatus(int id, String code, String status, String description, String customerAliasStatus, int rank) {
        this.id = id;
        this.code = code;
        this.status = status;
        this.description = description;
        this.customerAliasStatus = customerAliasStatus;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getCustomerAliasStatus() {
        return customerAliasStatus;
    }

    public int getRank() {
        return rank;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCustomerAliasStatus(String customerAliasStatus) {
        this.customerAliasStatus = customerAliasStatus;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
