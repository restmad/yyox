package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-01-23.
 */

public class OrderHistrory {

    private int id;
    private String actionDate;
    private String actionBy;
    private int orderId;
    private String history;
    private boolean inform;
    private String actionDateWithFormat;

    public OrderHistrory() {
        this.id = 0;
        this.actionDate ="";
        this.actionBy = "";
        this.orderId = 0;
        this.history= "";
        this.inform = false;
        this.actionDateWithFormat = "";
    }

    public OrderHistrory(int id, String actionDate, String actionBy, int orderId, String history, boolean inform, String actionDateWithFormat) {
        this.id = id;
        this.actionDate = actionDate;
        this.actionBy = actionBy;
        this.orderId = orderId;
        this.history = history;
        this.inform = inform;
        this.actionDateWithFormat = actionDateWithFormat;
    }

    public int getId() {
        return id;
    }

    public String getActionDate() {
        return actionDate;
    }

    public String getActionBy() {
        return actionBy;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getHistory() {
        return history;
    }

    public boolean isInform() {
        return inform;
    }

    public String getActionDateWithFormat() {
        return actionDateWithFormat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setInform(boolean inform) {
        this.inform = inform;
    }

    public void setActionDateWithFormat(String actionDateWithFormat) {
        this.actionDateWithFormat = actionDateWithFormat;
    }
}
