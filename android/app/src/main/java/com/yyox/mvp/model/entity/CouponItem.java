package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by 95 on 2017/3/9.
 */

public class CouponItem implements Serializable{

    private  String code;
    private  String name;
    private  String validFrom;
    private  String validTo;
    private  String status;
    private  String formatValidTo;
    private  String formatUseDate;
    private  String limitUse;
    private  String formatValidFrom;
    private  Coupon coupon;
    private boolean usable;

    public CouponItem() {
        this.code = "";
        this.name = "";
        this.validFrom = "";
        this.validTo = "";
        this.status = "";
        this.formatValidTo = "";
        this.formatUseDate = "";
        this.limitUse = "";
        this.formatValidFrom = "";
        this.coupon = new Coupon();
        this.usable = false;

    }

    public CouponItem(String code, String name, String validFrom, String validTo, String status, String formatValidTo, String formatUseDate, String limitUse, String formatValidFrom, Coupon mcoupon ,boolean usable) {
        this.code = code;
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = status;
        this.formatValidTo = formatValidTo;
        this.formatUseDate = formatUseDate;
        this.limitUse = limitUse;
        this.formatValidFrom = formatValidFrom;
        this.coupon = mcoupon;
        this.usable = usable;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getStatus() {
        return status;
    }

    public String getFormatValidTo() {
        return formatValidTo;
    }

    public String getFormatUseDate() {
        return formatUseDate;
    }

    public String getLimitUse() {
        return limitUse;
    }

    public String getFormatValidFrom() {
        return formatValidFrom;
    }

    public Coupon getMcoupon() {
        return coupon;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFormatValidTo(String formatValidTo) {
        this.formatValidTo = formatValidTo;
    }

    public void setFormatUseDate(String formatUseDate) {
        this.formatUseDate = formatUseDate;
    }

    public void setLimitUse(String limitUse) {
        this.limitUse = limitUse;
    }

    public void setFormatValidFrom(String formatValidFrom) {
        this.formatValidFrom = formatValidFrom;

    }

    public boolean getUsable() {
        return usable;
    }

    public void setUsable(boolean dated) {
        this.usable = dated;
    }

    public void setMcoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
