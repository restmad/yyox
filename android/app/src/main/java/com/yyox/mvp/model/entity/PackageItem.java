package com.yyox.mvp.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageItem implements Serializable {

    private int id;
    private int customerId;
    private int warehouseId;
    private String warehouseName;
    private int inStockDays;
    private double realWeight;
    private String currencyName;
    private String carrierNo;
    private String nickname;
    private boolean hasPhoto;
    private boolean couldModify;
    private boolean isShipback;
    private InventoryStatus inventoryStatus;
    private int type;
    private int status;
    private List<String> orderSreenshot;
    private boolean checked;
    private String actionDate;

    public PackageItem() {
        this.id = 0;
        this.actionDate = "";
        this.customerId = 0;
        this.warehouseId = 0;
        this.warehouseName = "";
        this.inStockDays = 0;
        this.realWeight = 0.0;
        this.currencyName = "";
        this.carrierNo = "";
        this.nickname = "";
        this.hasPhoto = false;
        this.couldModify = false;
        this.isShipback = false;
        this.inventoryStatus = new InventoryStatus();
        this.type = 0;
        this.status = 0;
        this.orderSreenshot = new ArrayList<>();
        this.checked = false;
    }

    public PackageItem(int id, String actionDate, int customerId, int warehouseId, String warehouseName, int inStockDays, double realWeight, String currencyName, String carrierNo, String nickname, boolean hasPhoto, boolean couldModify, boolean isShipback, InventoryStatus inventoryStatus, int type, int status, List<String> orderSreenshot, boolean checked) {
        this.id = id;
        this.actionDate = actionDate;
        this.customerId = customerId;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.inStockDays = inStockDays;
        this.realWeight = realWeight;
        this.currencyName = currencyName;
        this.carrierNo = carrierNo;
        this.nickname = nickname;
        this.hasPhoto = hasPhoto;
        this.couldModify = couldModify;
        this.isShipback = isShipback;
        this.inventoryStatus = inventoryStatus;
        this.type = type;
        this.status = status;
        this.orderSreenshot = orderSreenshot;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getInStockDays() {
        return inStockDays;
    }

    public void setInStockDays(int inStockDays) {
        this.inStockDays = inStockDays;
    }

    public double getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(double realWeight) {
        this.realWeight = realWeight;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public void setCarrierNo(String carrierNo) {
        this.carrierNo = carrierNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isHasPhoto() {
        return hasPhoto;
    }

    public boolean isCouldModify() {
        return couldModify;
    }

    public boolean isShipback() {
        return isShipback;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public List<String> getOrderSreenshot() {
        return orderSreenshot;
    }

    public void setOrderSreenshot(List<String> orderSreenshot) {
        this.orderSreenshot = orderSreenshot;
    }
}
