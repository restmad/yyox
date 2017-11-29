package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderCount {

    private int notputNo;//待入库
    private int foroutboundNo;//待出库
    private int haveoutboundNo;//已出库
    private int clear;//清关
    private int delivering;//配送
    private int finish;
    private int waitForDispose;

    public OrderCount(int notputNo, int foroutboundNo, int haveoutboundNo, int clear, int delivering, int finish, int waitForDispose) {
        this.notputNo = notputNo;
        this.foroutboundNo = foroutboundNo;
        this.haveoutboundNo = haveoutboundNo;
        this.clear = clear;
        this.delivering = delivering;
        this.finish = finish;
        this.waitForDispose = waitForDispose;
    }

    public OrderCount() {
        this.notputNo = 0;
        this.foroutboundNo = 0;
        this.haveoutboundNo = 0;
        this.clear = 0;
        this.delivering = 0;
        this.finish = 0;
        this.waitForDispose = 0;
    }

    public void setNotputNo(int notputNo) {
        this.notputNo = notputNo;
    }

    public void setForoutboundNo(int foroutboundNo) {
        this.foroutboundNo = foroutboundNo;
    }

    public void setHaveoutboundNo(int haveoutboundNo) {
        this.haveoutboundNo = haveoutboundNo;
    }

    public void setClear(int clear) {
        this.clear = clear;
    }

    public void setDelivering(int delivering) {
        this.delivering = delivering;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public void setWaitForDispose(int waitForDispose) {
        this.waitForDispose = waitForDispose;
    }

    public int getNotputNo() {
        return notputNo;
    }

    public int getForoutboundNo() {
        return foroutboundNo;
    }

    public int getHaveoutboundNo() {
        return haveoutboundNo;
    }

    public int getClear() {
        return clear;
    }

    public int getDelivering() {
        return delivering;
    }

    public int getFinish() {
        return finish;
    }

    public int getWaitForDispose() {
        return waitForDispose;
    }
}
