package com.yyox.mvp.model.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderPackage implements Serializable {

    private int parcelId;
    private String inboundLable;
    private String warehouse;
    private String remark;
    private double weight;
    private String nickname;
    private String carrierNo;
    private String inBoundDate;
    private String orderNo;
    private String customerAliasStatus;
    private List<OrderPackageGoods> merchandiseList;
    private List<OrderPackageGoods> goodList;
    private int type;
    private String bikeName;
    private String bikeUPS;
    private String warehouseName;
    private int warehouseId;
    private int id;
    private List<String> orderSreenshot;
    private int showtype;//页面显示类型 0包裹 1包裹中商品头 2包裹中商品项 3包裹中商品脚
    private OrderPackageGoods orderPackageGoods;
    private List<OrderPackageGoods> noDeclareGoodsList;
    private double realWeight;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;

    public OrderPackage() {
        this.parcelId = 0;
        this.inboundLable = "";
        this.warehouse = "";
        this.remark = "";
        this.weight = 0;
        this.nickname = "";
        this.carrierNo = "";
        this.inBoundDate = "";
        this.orderNo = "";
        this.customerAliasStatus = "";
        this.merchandiseList = new ArrayList<>();
        this.goodList = new ArrayList<>();
        this.type = 0;
        this.bikeName = "";
        this.bikeUPS = "";
        this.warehouseName = "";
        this.warehouseId = 0;
        this.id = 0;
        this.orderSreenshot = new ArrayList<>();
        this.showtype = 0;
        this.orderPackageGoods = new OrderPackageGoods();
        this.noDeclareGoodsList = new ArrayList<>();
        this.realWeight = 0;
        this.bitmap1 = null;
        this.bitmap2 = null;
        this.bitmap3 = null;
    }

    public OrderPackage(int parcelId, String inboundLable, String warehouse, String remark, double weight, String nickname, String carrierNo, String inBoundDate,
                        String orderNo, String customerAliasStatus, List<OrderPackageGoods> list,List<OrderPackageGoods> goodList,int type,String bikeName, String bikeUPS,
                        String warehouseName,int warehouseId,int id,List<String> OrderSreenshot,int showtype, OrderPackageGoods orderPackageGoods,
                        List<OrderPackageGoods> noDeclareGoodsList,double realWeight,Bitmap bitmap1,Bitmap bitmap2,Bitmap bitmap3) {
        this.parcelId = parcelId;
        this.inboundLable = inboundLable;
        this.warehouse = warehouse;
        this.remark = remark;
        this.weight = weight;
        this.nickname = nickname;
        this.carrierNo = carrierNo;
        this.inBoundDate = inBoundDate;
        this.orderNo = orderNo;
        this.customerAliasStatus = customerAliasStatus;
        this.merchandiseList = list;
        this.goodList = goodList;
        this.type = type;
        this.bikeName = bikeName;
        this.bikeUPS = bikeUPS;
        this.warehouseName = warehouseName;
        this.warehouseId = warehouseId;
        this.id = id;
        this.orderSreenshot = OrderSreenshot;
        this.showtype = showtype;
        this.orderPackageGoods = orderPackageGoods;
        this.noDeclareGoodsList = noDeclareGoodsList;
        this.realWeight = realWeight;
        this.bitmap1 = bitmap1;
        this.bitmap2 = bitmap2;
        this.bitmap3 = bitmap3;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public Bitmap getBitmap3() {
        return bitmap3;
    }

    public void setBitmap3(Bitmap bitmap3) {
        this.bitmap3 = bitmap3;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public int getParcelId() {
        return parcelId;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public String getInboundLable() {
        return inboundLable;
    }

    public void setInboundLable(String inboundLable) {
        this.inboundLable = inboundLable;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public void setCarrierNo(String carrierNo) {
        this.carrierNo = carrierNo;
    }

    public String getInBoundDate() {
        return inBoundDate;
    }

    public void setInBoundDate(String inBoundDate) {
        this.inBoundDate = inBoundDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerAliasStatus() {
        return customerAliasStatus;
    }

    public void setCustomerAliasStatus(String customerAliasStatus) {
        this.customerAliasStatus = customerAliasStatus;
    }

    public List<OrderPackageGoods> getGoodList() {
        if(goodList == null)
            return  merchandiseList;
        else
            return goodList;
    }

    public void setGoodList(List<OrderPackageGoods> goodList) {
        this.goodList = goodList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getBikeUPS() {
        return bikeUPS;
    }

    public void setBikeUPS(String bikeUPS) {
        this.bikeUPS = bikeUPS;
    }

    public List<OrderPackageGoods> getMerchandiseList() {
        return merchandiseList;
    }

    public void setMerchandiseList(List<OrderPackageGoods> merchandiseList) {
        this.merchandiseList = merchandiseList;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getOrderSreenshot() {
        return orderSreenshot;
    }

    public void setOrderSreenshot(List<String> orderSreenshot) {
        this.orderSreenshot = orderSreenshot;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public OrderPackageGoods getOrderPackageGoods() {
        return orderPackageGoods;
    }

    public void setOrderPackageGoods(OrderPackageGoods orderPackageGoods) {
        this.orderPackageGoods = orderPackageGoods;
    }

    public List<OrderPackageGoods> getNoDeclareGoodsList() {
        return noDeclareGoodsList;
    }

    public void setNoDeclareGoodsList(List<OrderPackageGoods> noDeclareGoodsList) {
        this.noDeclareGoodsList = noDeclareGoodsList;
    }

    public double getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(double realWeight) {
        this.realWeight = realWeight;
    }
}
