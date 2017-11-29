package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class Address implements Serializable{

    private int id;
    private String name;
    private String mobile;
    private String country;
    private String province;
    private String city;
    private String district;
    private String region;
    private String detailaddress;
    private String zipcode;
    private String idcard;
    private boolean isdefault;
    private Card customerCard;
    private boolean ischecked;

    public Address(){
        this.id = 0;
        this.name = "";
        this.mobile = "";
        this.country = "";
        this.province = "";
        this.city = "";
        this.district = "";
        this.region = "";
        this.detailaddress = "";
        this.zipcode = "";
        this.idcard = "";
        this.isdefault = false;
        this.customerCard = new Card("","","","","");
        this.ischecked = false;
    }

    public Address(int id, String name, String mobile, String country, String province, String city, String district, String region, String detailaddress, String zipcode, String idcard, boolean isdefault, Card customerCard,boolean ischecked) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.region = region;
        this.detailaddress = detailaddress;
        this.zipcode = zipcode;
        this.idcard = idcard;
        this.isdefault = isdefault;
        this.customerCard = customerCard;
        this.ischecked = ischecked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public boolean getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Card getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(Card customerCard) {
        this.customerCard = customerCard;
    }

    public boolean getIschecked(){
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
