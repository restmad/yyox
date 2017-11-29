package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-03-09.
 */

public class Money{

    private double originalCost;
    private double valueAddedCost;
    private double memberDiscount;
    private double couponDiscount;
    private double additionalFee;
    private double realCost;
    private double originalTax;
    private double totalAmount;

    public Money() {
        this.originalCost = 0;
        this.valueAddedCost = 0;
        this.memberDiscount = 0;
        this.couponDiscount = 0;
        this.additionalFee = 0;
        this.realCost = 0;
        this.originalTax = 0;
        this.totalAmount = 0;
    }

    public double getOriginalCost() {
        return originalCost;
    }

    public double getValueAddedCost() {
        return valueAddedCost;
    }

    public double getMemberDiscount() {
        return memberDiscount;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public double getAdditionalFee() {
        return additionalFee;
    }

    public double getRealCost() {
        return realCost;
    }

    public double getOriginalTax() {
        return originalTax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
