package com.yyox.mvp.model.entity;

/**
 * Created by 95 on 2017/5/17.
 */
public class Currency {
    private int id;
    private String currency;
    private String chineseName;
    private String englishName;

    public Currency() {
        this.id = 0;
        this.currency = "";
        this.chineseName = "";
        this.englishName= "";
    }

    public Currency(int id, String currency, String chineseName, String englishName) {
        this.id = id;
        this.currency = currency;
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
