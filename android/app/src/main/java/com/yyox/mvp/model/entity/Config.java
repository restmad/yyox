package com.yyox.mvp.model.entity;

/**
 * Created by 95 on 2017/5/17.
 */
public class Config {
    private int id;
    private int warehouseId;
    private boolean inboundUseCarrierNo;
    private boolean inboundWithPutaway;
    private boolean pickupOrderSinglePdf;
    private boolean outboundSkipWithdraw;
    private boolean inboundExceptionNotConfirm;
    private boolean outboundUseCustomerRefNo;
    private boolean inboundAddbox;

    public Config() {
        this.id = 0;
        this.warehouseId = 0;
        this.inboundUseCarrierNo = false;
        this.inboundWithPutaway = false;
        this.pickupOrderSinglePdf = false;
        this.outboundSkipWithdraw =false;
        this.inboundExceptionNotConfirm =false;
        this.outboundUseCustomerRefNo = false;
        this.inboundAddbox =false;
    }

    public Config(int id, int warehouseId, boolean inboundUseCarrierNo, boolean inboundWithPutaway, boolean pickupOrderSinglePdf, boolean outboundSkipWithdraw, boolean inboundExceptionNotConfirm, boolean outboundUseCustomerRefNo, boolean inboundAddbox) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.inboundUseCarrierNo = inboundUseCarrierNo;
        this.inboundWithPutaway = inboundWithPutaway;
        this.pickupOrderSinglePdf = pickupOrderSinglePdf;
        this.outboundSkipWithdraw = outboundSkipWithdraw;
        this.inboundExceptionNotConfirm = inboundExceptionNotConfirm;
        this.outboundUseCustomerRefNo = outboundUseCustomerRefNo;
        this.inboundAddbox = inboundAddbox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean isInboundUseCarrierNo() {
        return inboundUseCarrierNo;
    }

    public void setInboundUseCarrierNo(boolean inboundUseCarrierNo) {
        this.inboundUseCarrierNo = inboundUseCarrierNo;
    }

    public boolean isInboundWithPutaway() {
        return inboundWithPutaway;
    }

    public void setInboundWithPutaway(boolean inboundWithPutaway) {
        this.inboundWithPutaway = inboundWithPutaway;
    }

    public boolean isPickupOrderSinglePdf() {
        return pickupOrderSinglePdf;
    }

    public void setPickupOrderSinglePdf(boolean pickupOrderSinglePdf) {
        this.pickupOrderSinglePdf = pickupOrderSinglePdf;
    }

    public boolean isOutboundSkipWithdraw() {
        return outboundSkipWithdraw;
    }

    public void setOutboundSkipWithdraw(boolean outboundSkipWithdraw) {
        this.outboundSkipWithdraw = outboundSkipWithdraw;
    }

    public boolean isInboundExceptionNotConfirm() {
        return inboundExceptionNotConfirm;
    }

    public void setInboundExceptionNotConfirm(boolean inboundExceptionNotConfirm) {
        this.inboundExceptionNotConfirm = inboundExceptionNotConfirm;
    }

    public boolean isOutboundUseCustomerRefNo() {
        return outboundUseCustomerRefNo;
    }

    public void setOutboundUseCustomerRefNo(boolean outboundUseCustomerRefNo) {
        this.outboundUseCustomerRefNo = outboundUseCustomerRefNo;
    }

    public boolean isInboundAddbox() {
        return inboundAddbox;
    }

    public void setInboundAddbox(boolean inboundAddbox) {
        this.inboundAddbox = inboundAddbox;
    }
}
