package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-23.
 */

public class OrderHistoryItem {

    private String searchNo;
    private String orderStatus;
    private String summary ;
    private String status;
    private String url;
    private String companyCode;
    private List<OrderHistrory> list;
    private String title;
    private int customerId;

    public OrderHistoryItem() {
        this.searchNo = "";
        this.orderStatus ="";
        this.summary = "";
        this.status = "";
        this.url ="";
        this.companyCode = "";
        this.list = new ArrayList<>();
        this.title = "";
        this.customerId = 0;
    }

    public OrderHistoryItem(String searchNo, String orderStatus, String summary, String status, String url, String companyCode, List<OrderHistrory> list, String title,int customerId) {
        this.searchNo = searchNo;
        this.orderStatus = orderStatus;
        this.summary = summary;
        this.status = status;
        this.url = url;
        this.companyCode = companyCode;
        this.list = list;
        this.title = title;
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSummary() {
        return summary;
    }

    public String getSearchNo() {
        return searchNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderHistrory> getList() {
        return list;
    }

    public String getsummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setSearchNo(String searchNo) {
        this.searchNo = searchNo;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setList(List<OrderHistrory> list) {
        this.list = list;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
