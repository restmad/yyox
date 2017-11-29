package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2017/5/22.
 */

public class WarehousJson {
    private String msgs;
    private String other;
    private List<WarehouseList>data;
    private int status;

    public WarehousJson(String msgs, String other, List<WarehouseList> data, int status) {
        this.msgs = msgs;
        this.other = other;
        this.data = data;
        this.status = status;
    }

    public WarehousJson() {
        this.msgs = "";
        this.other = "";
        this.data = new ArrayList<>();
        this.status = 0;
    }

    public String getMsgs() {
        return msgs;
    }

    public void setMsgs(String msgs) {
        this.msgs = msgs;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public List<WarehouseList> getData() {
        return data;
    }

    public void setData(List<WarehouseList> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
