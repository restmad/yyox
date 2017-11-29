package com.yyox.mvp.model.entity;

/**
 * Created by dadaniu on 2017-01-11.
 */

public class Record {

    private String ORDER_NO;
    private int CUSTOMER_ID;
    private String AMOUNT;
    private String STATEMENT_TYPE_NAME;
    private String FREEZE_MONEY_DATE;
    private String CHINESE_NAME;
    private String CREATED_BY;
    private int CURRENCY;
    private String FREEZE;
    private int ID;
    private String SERIAL_MONEY;
    private String FIXTURE_DATE;
    private int IN_OR_OUT;
    private String SUBMIT_DATE;
    private int TYPE;
    private String ENGLISH_NAME;

    public Record() {
        this.ORDER_NO = "";
        this.CUSTOMER_ID = 0;
        this.AMOUNT = "";
        this.STATEMENT_TYPE_NAME = "";
        this.FREEZE_MONEY_DATE = "";
        this.CHINESE_NAME = "";
        this.CREATED_BY = "";
        this.CURRENCY = 0;
        this.FREEZE = "";
        this.ID = 0;
        this.SERIAL_MONEY = "";
        this.FIXTURE_DATE = "";
        this.IN_OR_OUT = 0;
        this.SUBMIT_DATE = "";
        this.TYPE = 0;
        this.ENGLISH_NAME = "";
    }

    public Record(String ORDER_NO, int CUSTOMER_ID, String AMOUNT, String STATEMENT_TYPE_NAME, String FREEZE_MONEY_DATE, String CHINESE_NAME, String CREATED_BY, int CURRENCY, String FREEZE, int ID, String SERIAL_MONEY, String FIXTURE_DATE, int IN_OR_OUT, String SUBMIT_DATE, int TYPE, String ENGLISH_NAME) {
        this.ORDER_NO = ORDER_NO;
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.AMOUNT = AMOUNT;
        this.STATEMENT_TYPE_NAME = STATEMENT_TYPE_NAME;
        this.FREEZE_MONEY_DATE = FREEZE_MONEY_DATE;
        this.CHINESE_NAME = CHINESE_NAME;
        this.CREATED_BY = CREATED_BY;
        this.CURRENCY = CURRENCY;
        this.FREEZE = FREEZE;
        this.ID = ID;
        this.SERIAL_MONEY = SERIAL_MONEY;
        this.FIXTURE_DATE = FIXTURE_DATE;
        this.IN_OR_OUT = IN_OR_OUT;
        this.SUBMIT_DATE = SUBMIT_DATE;
        this.TYPE = TYPE;
        this.ENGLISH_NAME = ENGLISH_NAME;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public String getSTATEMENT_TYPE_NAME() {
        return STATEMENT_TYPE_NAME;
    }

    public String getFREEZE_MONEY_DATE() {
        return FREEZE_MONEY_DATE;
    }

    public String getCHINESE_NAME() {
        return CHINESE_NAME;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public int getCURRENCY() {
        return CURRENCY;
    }

    public String getFREEZE() {
        return FREEZE;
    }

    public int getID() {
        return ID;
    }

    public String getSERIAL_MONEY() {
        return SERIAL_MONEY;
    }

    public String getFIXTURE_DATE() {
        return FIXTURE_DATE;
    }

    public int getIN_OR_OUT() {
        return IN_OR_OUT;
    }

    public String getSUBMIT_DATE() {
        return SUBMIT_DATE;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getENGLISH_NAME() {
        return ENGLISH_NAME;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public void setCUSTOMER_ID(int CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public void setSTATEMENT_TYPE_NAME(String STATEMENT_TYPE_NAME) {
        this.STATEMENT_TYPE_NAME = STATEMENT_TYPE_NAME;
    }

    public void setFREEZE_MONEY_DATE(String FREEZE_MONEY_DATE) {
        this.FREEZE_MONEY_DATE = FREEZE_MONEY_DATE;
    }

    public void setCHINESE_NAME(String CHINESE_NAME) {
        this.CHINESE_NAME = CHINESE_NAME;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public void setCURRENCY(int CURRENCY) {
        this.CURRENCY = CURRENCY;
    }

    public void setFREEZE(String FREEZE) {
        this.FREEZE = FREEZE;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSERIAL_MONEY(String SERIAL_MONEY) {
        this.SERIAL_MONEY = SERIAL_MONEY;
    }

    public void setFIXTURE_DATE(String FIXTURE_DATE) {
        this.FIXTURE_DATE = FIXTURE_DATE;
    }

    public void setIN_OR_OUT(int IN_OR_OUT) {
        this.IN_OR_OUT = IN_OR_OUT;
    }

    public void setSUBMIT_DATE(String SUBMIT_DATE) {
        this.SUBMIT_DATE = SUBMIT_DATE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setENGLISH_NAME(String ENGLISH_NAME) {
        this.ENGLISH_NAME = ENGLISH_NAME;
    }


}
