package com.yyox.mvp.model.entity;

/**
 * Created by 95 on 2017/4/11.
 */
public class inventorybasic {
    private String nickname;
    private String OrderNo;
    private int id ;

    public inventorybasic(String nickname, String orderNo, int id) {
        this.nickname = nickname;
        this.OrderNo = orderNo;
        this.id = id;
    }

    public inventorybasic() {
        this.nickname = "";
        this.OrderNo = "";
        this.id = 0;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

}
