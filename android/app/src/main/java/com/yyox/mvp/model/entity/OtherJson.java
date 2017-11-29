package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-25.
 */

public class OtherJson implements Serializable {

    private String orderCount;
    private int pageNo;
    private int totalCount;

    public OtherJson() {
        this.orderCount = "";
        this.pageNo = 0;
        this.totalCount = 0;
    }

    public OtherJson(String orderCount, int pageNo, int totalCount) {
        this.orderCount = orderCount;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

}
