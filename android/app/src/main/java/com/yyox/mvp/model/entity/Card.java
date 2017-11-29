package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-02-04.
 */

public class Card implements Serializable {

    private String cardType;
    private String cardNo;
    private String isValidated;
    private String cardImageFrontUrl;
    private String cardImageBackUrl;

    public Card(){
        this.cardType = "";
        this.cardNo = "";
        this.isValidated = "";
        this.cardImageFrontUrl = "";
        this.cardImageBackUrl = "";
    }

    public Card(String cardType, String cardNo, String isValidated, String cardImageFrontUrl, String cardImageBackUrl) {
        this.cardType = cardType;
        this.cardNo = cardNo;
        this.isValidated = isValidated;
        this.cardImageFrontUrl = cardImageFrontUrl;
        this.cardImageBackUrl = cardImageBackUrl;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getCardImageFrontUrl() {
        return cardImageFrontUrl;
    }

    public void setCardImageFrontUrl(String cardImageFrontUrl) {
        this.cardImageFrontUrl = cardImageFrontUrl;
    }

    public String getCardImageBackUrl() {
        return cardImageBackUrl;
    }

    public void setCardImageBackUrl(String cardImageBackUrl) {
        this.cardImageBackUrl = cardImageBackUrl;
    }
}
