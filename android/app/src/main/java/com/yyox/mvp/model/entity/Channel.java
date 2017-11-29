package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-16.
 */

public class Channel implements Serializable {

    private int leadId;
    private String code;
    private String explain;
    private String priceWeight;
    private int warehouseId;
    private int orderTypeId;
    private String orderTypeCode;
    private double initialWeight;
    private double initialWeightPrice;
    private double unitWeight;
    private double unitWeightPrice;
    private double initialWeight2;
    private double initialWeightPrice2;
    private double unitWeight2;
    private double unitWeightPrice2;
    private boolean checked;

    public Channel() {
        this.leadId = 0;
        this.code = "";
        this.explain = "";
        this.priceWeight = "";
        this.warehouseId = 0;
        this.orderTypeId = 0;
        this.orderTypeCode = "";
        this.initialWeight = 0;
        this.initialWeightPrice = 0;
        this.unitWeight = 0;
        this.unitWeightPrice = 0;
        this.initialWeight2 = 0;
        this.initialWeightPrice2 = 0;
        this.unitWeight2 = 0;
        this.unitWeightPrice2 = 0;
        this.checked = false;
    }

    public Channel(int leadId, String code, String explain, String priceWeight, int warehouseId, int orderTypeId, String orderTypeCode, double initialWeight, double initialWeightPrice, double unitWeight, double unitWeightPrice, double initialWeight2, double initialWeightPrice2, double unitWeight2, double unitWeightPrice2, boolean checked) {
        this.leadId = leadId;
        this.code = code;
        this.explain = explain;
        this.priceWeight = priceWeight;
        this.warehouseId = warehouseId;
        this.orderTypeId = orderTypeId;
        this.orderTypeCode = orderTypeCode;
        this.initialWeight = initialWeight;
        this.initialWeightPrice = initialWeightPrice;
        this.unitWeight = unitWeight;
        this.unitWeightPrice = unitWeightPrice;
        this.initialWeight2 = initialWeight2;
        this.initialWeightPrice2 = initialWeightPrice2;
        this.unitWeight2 = unitWeight2;
        this.unitWeightPrice2 = unitWeightPrice2;
        this.checked = checked;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getPriceWeight() {
        return priceWeight;
    }

    public void setPriceWeight(String priceWeight) {
        this.priceWeight = priceWeight;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getInitialWeightPrice() {
        return initialWeightPrice;
    }

    public void setInitialWeightPrice(double initialWeightPrice) {
        this.initialWeightPrice = initialWeightPrice;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }

    public double getUnitWeightPrice() {
        return unitWeightPrice;
    }

    public void setUnitWeightPrice(double unitWeightPrice) {
        this.unitWeightPrice = unitWeightPrice;
    }

    public double getInitialWeight2() {
        return initialWeight2;
    }

    public void setInitialWeight2(double initialWeight2) {
        this.initialWeight2 = initialWeight2;
    }

    public double getInitialWeightPrice2() {
        return initialWeightPrice2;
    }

    public void setInitialWeightPrice2(double initialWeightPrice2) {
        this.initialWeightPrice2 = initialWeightPrice2;
    }

    public double getUnitWeight2() {
        return unitWeight2;
    }

    public void setUnitWeight2(double unitWeight2) {
        this.unitWeight2 = unitWeight2;
    }

    public double getUnitWeightPrice2() {
        return unitWeightPrice2;
    }

    public void setUnitWeightPrice2(double unitWeightPrice2) {
        this.unitWeightPrice2 = unitWeightPrice2;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
