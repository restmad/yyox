package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class Coupon implements Serializable{

    private  int id ;
    private  int companyId;
    private   String type;
    private   String  code;
    private   String  name;
    private  int maximumCouponCount;
    private  int  priority;
    private  double minimumOrderAmount;
    private  double discountAmount;
    private  int validPeriod;
    private  String validFrom;
    private String validTo;
    private String validPickupDate;
    private String createdDate;
    private String createdBy;
    private  int status;
    private String warehouseRange;
    private String ordertypeRange;
    private String comments;
    private String activityName;
    private  int discountAmountInteger;

    public Coupon() {
        this.id = 0;
        this.companyId = 0;
        this.type = "";
        this.code = "";
        this.name = "";
        this.maximumCouponCount = 0;
        this.priority = 0;
        this.minimumOrderAmount = 0;
        this.discountAmount = 0;
        this.validPeriod = 0;
        this.validFrom = "";
        this.status = 0;
        this.discountAmountInteger = 0;
        this.validTo = "";
        this.validPickupDate = "";
        this.createdDate = "";
        this.createdBy = "";
        this.warehouseRange = "";
        this.ordertypeRange = "";
        this.comments="";
        this.activityName = "";
    }

    public Coupon(int id, int companyId, String type, String code, String name, int maximumCouponCount, int priority, double minimumOrderAmount, double discountAmount, int validPeriod, String validFrom, String validTo, String validPickupDate, String createdDate, String createdBy, int status, String warehouseRange, String ordertypeRange, String comments, String activityName, int discountAmountInteger) {
        this.id = id;
        this.companyId = companyId;
        this.type = type;
        this.code = code;
        this.name = name;
        this.maximumCouponCount = maximumCouponCount;
        this.priority = priority;
        this.minimumOrderAmount = minimumOrderAmount;
        this.discountAmount = discountAmount;
        this.validPeriod = validPeriod;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.validPickupDate = validPickupDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.status = status;
        this.warehouseRange = warehouseRange;
        this.ordertypeRange = ordertypeRange;
        this.comments = comments;
        this.activityName = activityName;
        this.discountAmountInteger = discountAmountInteger;
    }

    public int getId() {
        return id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getMaximumCouponCount() {
        return maximumCouponCount;
    }

    public int getPriority() {
        return priority;
    }

    public int getValidPeriod() {
        return validPeriod;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public int getStatus() {
        return status;
    }

    public int getDiscountAmountInteger() {
        return discountAmountInteger;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaximumCouponCount(int maximumCouponCount) {
        this.maximumCouponCount = maximumCouponCount;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setMinimumOrderAmount(int minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setValidPeriod(int validPeriod) {
        this.validPeriod = validPeriod;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDiscountAmountInteger(int discountAmountInteger) {
        this.discountAmountInteger = discountAmountInteger;
    }

    public double getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(double minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getValidPickupDate() {
        return validPickupDate;
    }

    public void setValidPickupDate(String validPickupDate) {
        this.validPickupDate = validPickupDate;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getWarehouseRange() {
        return warehouseRange;
    }

    public void setWarehouseRange(String warehouseRange) {
        this.warehouseRange = warehouseRange;
    }

    public String getOrdertypeRange() {
        return ordertypeRange;
    }

    public void setOrdertypeRange(String ordertypeRange) {
        this.ordertypeRange = ordertypeRange;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
