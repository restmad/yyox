package com.yyox.mvp.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-23.
 */

public class OrderDetail implements Serializable{

    private int orderID;
    private String orderNo;
    private String channel;
    private String alias;
    private double originalCost;
    private double realcost;
    private double couponDiscount;
    private int type;
    private String YouBi;
    private Address address;
    private List<OrderPackage> merchandiseList;
    private Channel orderchannel;
    private double realWeight;
    private OrderType orderType;
    private double taxRepay;
    private CouponItem couponList;
    private List<String> valueAddedlist;
    private String inWeight;
    public OrderDetail(){
        this.orderID = 0;
        this.orderNo = "";
        this.channel = "";
        this.alias = "";
        this.originalCost = 0;
        this.realcost = 0;
        this.couponDiscount = 0;
        this.type = 0;
        this.YouBi = "";
        this.address = new Address();
        this.merchandiseList = new ArrayList<>();
        this.orderchannel = new Channel();
        this.realWeight = 0;
        this.orderType = new OrderType();
        this.taxRepay = 0;
        this.couponList = new CouponItem();
        this.valueAddedlist = new ArrayList<>();
        this.inWeight = "";
    }

    public int getOrderID() {
        return orderID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public double getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(double originalCost) {
        this.originalCost = originalCost;
    }

    public String getYoubi() {
        return YouBi;
    }

    public void setYouBi(String youBi) {
        YouBi = youBi;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<OrderPackage> getMerchandiseList() {
        return merchandiseList;
    }

    public void setMerchandiseList(List<OrderPackage> merchandiseList) {
        this.merchandiseList = merchandiseList;
    }

    public double getRealcost() {
        return realcost;
    }

    public void setRealcost(double realcost) {
        this.realcost = realcost;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Channel getOrderchannel() {
        return orderchannel;
    }

    public void setOrderchannel(Channel orderchannel) {
        this.orderchannel = orderchannel;
    }

    public void setRealWeight(double realWeight) {
        this.realWeight = realWeight;
    }

    public double getRealWeight() {
        return realWeight;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public double getTaxRepay() {
        return taxRepay;
    }

    public void setTaxRepay(double taxRepay) {
        this.taxRepay = taxRepay;
    }

    public CouponItem getCouponList() {
        return couponList;
    }

    public void setCouponList(CouponItem couponList) {
        this.couponList = couponList;
    }

    public List<String> getValueAddedlist() {
        return valueAddedlist;
    }

    public void setValueAddedlist(List<String> valueAddedlist) {
        this.valueAddedlist = valueAddedlist;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getYouBi() {
        return YouBi;
    }

    public String getInWeight() {
        return inWeight;
    }

    public void setInWeight(String inWeight) {
        this.inWeight = inWeight;
    }
}
