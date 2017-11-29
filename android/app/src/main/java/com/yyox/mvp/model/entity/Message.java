package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class Message {

    private int id;
    private int customerId;
    private String information;
    private String state;
    private String operateTime;
    private String otherNo;
    private String source_name;
    private String flightNo;
    private String destination;
    private String orderStatus;
    private String sourceType;
    private String customerName;
    private OrderCount initCustomer;

    public Message(int id, int customerId, String information, String state, String operateTime, String otherNo, String source_name, String flightNo, String destination, String orderStatus, String sourceType, String customerName, OrderCount initCustomer) {
        this.id = id;
        this.customerId = customerId;
        this.information = information;
        this.state = state;
        this.operateTime = operateTime;
        this.otherNo = otherNo;
        this.source_name = source_name;
        this.flightNo = flightNo;
        this.destination = destination;
        this.orderStatus = orderStatus;
        this.sourceType = sourceType;
        this.customerName = customerName;
        this.initCustomer = initCustomer;
    }

    public Message() {
        this.id = 0;
        this.customerId = 0;
        this.information = "";
        this.state = "";
        this.operateTime = "";
        this.otherNo = "";
        this.source_name = "";
        this.flightNo = "";
        this.destination = "";
        this.orderStatus = "";
        this.sourceType = "";
        this.customerName = "";
        this.initCustomer = new OrderCount();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOtherNo(String otherNo) {
        this.otherNo = otherNo;
    }

    public String getOtherNo() {
        return otherNo;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderCount getInitCustomer() {
        return initCustomer;
    }

    public void setInitCustomer(OrderCount initCustomer) {
        this.initCustomer = initCustomer;
    }
}
