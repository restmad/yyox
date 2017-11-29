package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-03-09.
 */

public class Fee {

    private double weight;
    private int couponNumber;
    private Money money;
    private List<String> valueAddedlist;

    public Fee(){
        this.couponNumber = 0;
        this.weight = 0;
        this.money = new Money();
        this.valueAddedlist = new ArrayList<>();
    }

    public Fee(double weight, int couponNumber, Money money, List<String> valueAddedlist) {
        this.weight = weight;
        this.couponNumber = couponNumber;
        this.money = money;
        this.valueAddedlist = valueAddedlist;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setValueAddedlist(List<String> valueAddedlist) {
        this.valueAddedlist = valueAddedlist;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public int getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(int couponNumber) {
        this.couponNumber = couponNumber;
    }

    public double getWeight() {
        return weight;
    }

    public Money getMoney() {
        return money;
    }

    public List<String> getValueAddedlist() {
        return valueAddedlist;
    }
}
