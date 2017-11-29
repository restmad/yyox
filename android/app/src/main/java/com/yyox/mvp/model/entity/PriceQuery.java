package com.yyox.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2017/5/17.
 */
public class PriceQuery {
    private int id ;
    private String name;
    private String alias;
    private String code;
    private String country;
    private String countryName;
    private String currencyId;
    private Currency currency;
    private double cnyRate;
    private Config config;
    private String timezone;
    private String address;
    private String city;
    private String county;
    private String state;
    private String zipcode;
    private String telphone;
    private String manager;
    private boolean esong;
    private boolean eturbo;
    private boolean taomi;
    private int status;
    private List<OrderTypes> orderTypeList;

    public PriceQuery() {
        this.id = 0;
        this.name = "";
        this.alias = "";
        this.code = "";
        this.country = "";
        this.countryName = "";
        this.currencyId = "";
        this.currency = new Currency();
        this.cnyRate = 0.0;
        this.config = new Config();
        this.timezone = "";
        this.address = "";
        this.city = "";
        this.county = "";
        this.state = "";
        this.zipcode = "";
        this.telphone = "";
        this.manager = "";
        this.esong = false;
        this.eturbo = false;
        this.taomi = false;
        this.status = 0;
        this.orderTypeList = new ArrayList<>();
    }

    public PriceQuery(int id, String name, String alias, String code, String country, String countryName, String currencyId, Currency currency, double cnyRate, Config config, String timezone, String address, String city, String county, String state, String zipcode, String telphone, String manager, boolean esong, boolean eturbo, boolean taomi, int status, List<OrderTypes> orderTypeList) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.code = code;
        this.country = country;
        this.countryName = countryName;
        this.currencyId = currencyId;
        this.currency = currency;
        this.cnyRate = cnyRate;
        this.config = config;
        this.timezone = timezone;
        this.address = address;
        this.city = city;
        this.county = county;
        this.state = state;
        this.zipcode = zipcode;
        this.telphone = telphone;
        this.manager = manager;
        this.esong = esong;
        this.eturbo = eturbo;
        this.taomi = taomi;
        this.status = status;
        this.orderTypeList = orderTypeList;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(double cnyRate) {
        this.cnyRate = cnyRate;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public boolean isEsong() {
        return esong;
    }

    public void setEsong(boolean esong) {
        this.esong = esong;
    }

    public boolean isEturbo() {
        return eturbo;
    }

    public void setEturbo(boolean eturbo) {
        this.eturbo = eturbo;
    }

    public boolean isTaomi() {
        return taomi;
    }

    public void setTaomi(boolean taomi) {
        this.taomi = taomi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderTypes> getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(List<OrderTypes> orderTypeList) {
        this.orderTypeList = orderTypeList;
    }
}
