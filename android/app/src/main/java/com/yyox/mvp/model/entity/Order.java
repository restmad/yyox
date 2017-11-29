package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class Order {

    private  int type;
    private  String orderNo;
    private  String orStatusDate;
    private  String orderStatus;
    private List<inventorybasic> inventorybasic;
    private String orderId;

    public Order(int type, String orderNo, String orStatusDate, String orderStatus, List<com.yyox.mvp.model.entity.inventorybasic> inventorybasic, String orderId) {
        this.type = type;
        this.orderNo = orderNo;
        this.orStatusDate = orStatusDate;
        this.orderStatus = orderStatus;
        this.inventorybasic = inventorybasic;
        this.orderId = orderId;
    }

    public Order() {
        this.type = 0;
        this.orderNo = "";
        this.orStatusDate = "";
        this.orderStatus = "";
        this.inventorybasic = new ArrayList<>();
        this.orderId = "";
    }

    public int getType() {
        return type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrStatusDate() {
        return orStatusDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<com.yyox.mvp.model.entity.inventorybasic> getInventorybasic() {
        return inventorybasic;
    }

    public void setInventorybasic(List<com.yyox.mvp.model.entity.inventorybasic> inventorybasic) {
        this.inventorybasic = inventorybasic;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrStatusDate(String orStatusDate) {
        this.orStatusDate = orStatusDate;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }
}
