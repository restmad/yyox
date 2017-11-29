package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public class OrderPackageGoods implements Serializable {

    private boolean put;
    private int id;
    private boolean needDelStock;
    private int unitsNumber;
    private String size;
    private String brand;
    private String units;
    private int inventoryId;
    private int parentId;
    private String parentName;
    private String upc;
    private String sku;
    private String categoryName;
    private int dutyPaidValue;
    private double taxRate;
    private int declaredValue;
    private int syncFlag;
    private int stock;
    private String productName;
    private double amountCNYUnitPrice;
    private double amountUnitPrice;
    private String currencyName;
    private int currencyId;
    private double amount;
    private double amountCNY;
    private double tax;
    private boolean goolsType;
    private String productNameCNY;
    private String brandCNY;
    private int count;
    private boolean checked;
    private String appProductNameCNY;
    private String appBrandCNY;
    private String taxNo;
    private String upcFlag;
    private String productNameENG;
    private String classifyInfo;


    public OrderPackageGoods(){
        this.put = false;
        this.id = 0;
        this.needDelStock = false;
        this.unitsNumber = 0;
        this.size = "";
        this.brand = "";
        this.units = "";
        this.inventoryId = 0;
        this.parentId = 0;
        this.parentName = "";
        this.upc = "";
        this.sku = "";
        this.categoryName = "";
        this.dutyPaidValue = 0;
        this.taxRate = 0;
        this.declaredValue = 0;
        this.syncFlag = 0;
        this.stock = 1;
        this.productName = "";
        this.amountCNYUnitPrice = 0;
        this.amountUnitPrice = 0.00;
        this.currencyName = "";
        this.currencyId = 0;
        this.amount = 0;
        this.amountCNY = 0;
        this.tax = 0;
        this.goolsType = true;
        this.productNameCNY = "";
        this.brandCNY = "";
        this.count = 0;
        this.checked = false;
        this.appProductNameCNY = "";
        this.appBrandCNY = "";
    }

    public OrderPackageGoods(boolean put, int id, boolean needDelStock, int unitsNumber, String size, String brand, String units, int inventoryId, int parentId, String parentName, String upc, String sku, String categoryName, int dutyPaidValue, double taxRate, int declaredValue, int syncFlag, int stock, String productName, double amountCNYUnitPrice, double amountUnitPrice, String currencyName, int currencyId, double amount, double amountCNY, double tax, boolean goolsType, String productNameCNY, String brandCNY, int count, boolean checked, String appProductNameCNY, String appBrandCNY) {
        this.put = put;
        this.id = id;
        this.needDelStock = needDelStock;
        this.unitsNumber = unitsNumber;
        this.size = size;
        this.brand = brand;
        this.units = units;
        this.inventoryId = inventoryId;
        this.parentId = parentId;
        this.parentName = parentName;
        this.upc = upc;
        this.sku = sku;
        this.categoryName = categoryName;
        this.dutyPaidValue = dutyPaidValue;
        this.taxRate = taxRate;
        this.declaredValue = declaredValue;
        this.syncFlag = syncFlag;
        this.stock = stock;
        this.productName = productName;
        this.amountCNYUnitPrice = amountCNYUnitPrice;
        this.amountUnitPrice = amountUnitPrice;
        this.currencyName = currencyName;
        this.currencyId = currencyId;
        this.amount = amount;
        this.amountCNY = amountCNY;
        this.tax = tax;
        this.goolsType = goolsType;
        this.productNameCNY = productNameCNY;
        this.brandCNY = brandCNY;
        this.count = count;
        this.checked = checked;
        this.appProductNameCNY = appProductNameCNY;
        this.appBrandCNY = appBrandCNY;
    }

    public boolean isPut() {
        return put;
    }

    public void setPut(boolean put) {
        this.put = put;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNeedDelStock() {
        return needDelStock;
    }

    public void setNeedDelStock(boolean needDelStock) {
        this.needDelStock = needDelStock;
    }

    public int getUnitsNumber() {
        return unitsNumber;
    }

    public void setUnitsNumber(int unitsNumber) {
        this.unitsNumber = unitsNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getDutyPaidValue() {
        return dutyPaidValue;
    }

    public void setDutyPaidValue(int dutyPaidValue) {
        this.dutyPaidValue = dutyPaidValue;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public int getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(int declaredValue) {
        this.declaredValue = declaredValue;
    }

    public int getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(int syncFlag) {
        this.syncFlag = syncFlag;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getAmountUnitPrice() {
        return amountUnitPrice;
    }

    public void setAmountUnitPrice(double amountUnitPrice) {
        this.amountUnitPrice = amountUnitPrice;
    }

    public double getAmountCNYUnitPrice() {
        return amountCNYUnitPrice;
    }

    public void setAmountCNYUnitPrice(double amountCNYUnitPrice) {
        this.amountCNYUnitPrice = amountCNYUnitPrice;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountCNY() {
        return amountCNY;
    }

    public void setAmountCNY(double amountCNY) {
        this.amountCNY = amountCNY;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public boolean isGoolsType() {
        return goolsType;
    }

    public String getProductNameCNY() {
        return productNameCNY;
    }

    public void setProductNameCNY(String productNameCNY) {
        this.productNameCNY = productNameCNY;
    }

    public String getBrandCNY() {
        return brandCNY;
    }

    public void setBrandCNY(String brandCNY) {
        this.brandCNY = brandCNY;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAppProductNameCNY() {
        return appProductNameCNY;
    }

    public void setAppProductNameCNY(String appProductNameCNY) {
        this.appProductNameCNY = appProductNameCNY;
    }

    public String getAppBrandCNY() {
        return appBrandCNY;
    }

    public void setAppBrandCNY(String appBrandCNY) {
        this.appBrandCNY = appBrandCNY;
    }
}
