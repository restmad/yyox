package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public class Warehouse implements Serializable{

    private int id;
    private String name;
    private String alias;
    private String code;
    private String country;
    private int currencyId;
    private int cnyRate;
    private String city;
    private boolean esong;
    private boolean eturbo;
    private boolean taomi;

    public Warehouse(){
        this.id = 0;
        this.name = "";
        this.alias = "";
        this.code = "";
        this.country = "";
        this.currencyId = 0;
        this.cnyRate = 0;
        this.city = "";
        this.esong = false;
        this.eturbo = false;
        this.taomi = false;
    }

    public Warehouse(int id, String name, String alias, String code, String country, int currencyId, int cnyRate, String city, boolean esong, boolean eturbo, boolean taomi) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.code = code;
        this.country = country;
        this.currencyId = currencyId;
        this.cnyRate = cnyRate;
        this.city = city;
        this.esong = esong;
        this.eturbo = eturbo;
        this.taomi = taomi;
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

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(int cnyRate) {
        this.cnyRate = cnyRate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
