package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class MessageItem {

    private List<Message> result;
    private OrderCount initCustomer;

    public MessageItem(List<Message> result, OrderCount initCustomer) {
        this.result = result;
        this.initCustomer = initCustomer;
    }

    public MessageItem() {
        this.result = new ArrayList<>();
        this.initCustomer = new OrderCount();
    }

    public List<Message> getResult() {
        return result;
    }

    public OrderCount getInitCustomer() {
        return initCustomer;
    }

    public void setResult(List<Message> result) {
        this.result = result;
    }

    public void setInitCustomer(OrderCount initCustomer) {
        this.initCustomer = initCustomer;
    }
}
