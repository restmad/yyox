package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class OrderPending implements Serializable {

    private String no;
    private int inventoryId;
    private String nickname;
    private int type;// 1包裹 2订单 3合箱订单
    private String productName;
    private String warehouseName;
    private String date;
    private int packageNum;
    private String orderStatus;
    private String statement;
    private int status;
    private String currency;
    private String money;
    private int addressId;

    public OrderPending() {
        this.no = "";
        this.inventoryId = 0;
        this.nickname = "";
        this.type = 0;
        this.productName = "";
        this.warehouseName = "";
        this.date = "";
        this.packageNum = 0;
        this.orderStatus = "";
        this.statement = "";
        this.status = 0;
        this.currency = "";
        this.money = "";
        this.addressId = 0;
    }

    public OrderPending(String no, int inventoryId, String nickname, int type, String productName, String warehouseName, String date, int packageNum,
                        String orderStatus, String statement, int status, String currency, String money, int addressId) {
        this.no = no;
        this.inventoryId = inventoryId;
        this.nickname = nickname;
        this.type = type;
        this.productName = productName;
        this.warehouseName = warehouseName;
        this.date = date;
        this.packageNum = packageNum;
        this.orderStatus = orderStatus;
        this.statement = statement;
        this.status = status;
        this.currency = currency;
        this.money = money;
        this.addressId = addressId;
    }

    public String getNo() {
        return no;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getType() {
        return type;
    }

    public String getProductName() {
        return productName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getDate() {
        return date;
    }

    public int getPackageNum() {
        return packageNum;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getStatement() {
        return statement;
    }

    public int getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMoney() {
        return money;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
