package com.yyox.mvp.model.entity;

/**
 * Created by 95 on 2017/3/10.
 */

public class UserDetail {

    private int difBalanceUsdNum;
    private String avatarUrl;
    private String difBalanceCny;
    private int couponCount;
    private String balanceCny;
    private String balanceCnyFormat2;
    private boolean balanceUsdSmaller;
    private String estimatedBalanceCny;
    private String difBalanceUsd;
    private String freezeBalanceCny;
    private String balanceUsd;
    private String estimatedBalanceUsd;
    private String freezeBalanceUsd;
    private String member;
    private String name;
    private double difBalanceCnyNum;
    private boolean balanceCnySmaller;

    public UserDetail() {
        this.difBalanceUsdNum = 0;
        this.avatarUrl = "";
        this.difBalanceCny = "";
        this.couponCount = 0;
        this.balanceCny = "";
        this.balanceCnyFormat2 = "";
        this.balanceUsdSmaller = false;
        this.estimatedBalanceCny = "";
        this.difBalanceUsd = "";
        this.freezeBalanceCny = "";
        this.balanceUsd = "";
        this.estimatedBalanceUsd = "";
        this.freezeBalanceUsd = "";
        this.member = "";
        this.name = "";
        this.difBalanceCnyNum = 0.0;
        this.balanceCnySmaller = false;
    }

    public UserDetail(int difBalanceUsdNum, String avatarUrl, String difBalanceCny, int couponCount, String balanceCny, String balanceCnyFormat2, boolean balanceUsdSmaller, String estimatedBalanceCny, String difBalanceUsd, String freezeBalanceCny, String balanceUsd, String estimatedBalanceUsd, String freezeBalanceUsd, String member, String name, double difBalanceCnyNum, boolean balanceCnySmaller) {
        this.difBalanceUsdNum = difBalanceUsdNum;
        this.avatarUrl = avatarUrl;
        this.difBalanceCny = difBalanceCny;
        this.couponCount = couponCount;
        this.balanceCny = balanceCny;
        this.balanceCnyFormat2 = balanceCnyFormat2;
        this.balanceUsdSmaller = balanceUsdSmaller;
        this.estimatedBalanceCny = estimatedBalanceCny;
        this.difBalanceUsd = difBalanceUsd;
        this.freezeBalanceCny = freezeBalanceCny;
        this.balanceUsd = balanceUsd;
        this.estimatedBalanceUsd = estimatedBalanceUsd;
        this.freezeBalanceUsd = freezeBalanceUsd;
        this.member = member;
        this.name = name;
        this.difBalanceCnyNum = difBalanceCnyNum;
        this.balanceCnySmaller = balanceCnySmaller;
    }

    public int getDifBalanceUsdNum() {
        return difBalanceUsdNum;
    }

    public void setDifBalanceUsdNum(int difBalanceUsdNum) {
        this.difBalanceUsdNum = difBalanceUsdNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDifBalanceCny() {
        return difBalanceCny;
    }

    public void setDifBalanceCny(String difBalanceCny) {
        this.difBalanceCny = difBalanceCny;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getBalanceCny() {
        return balanceCny;
    }

    public void setBalanceCny(String balanceCny) {
        this.balanceCny = balanceCny;
    }

    public String getBalanceCnyFormat2() {
        return balanceCnyFormat2;
    }

    public void setBalanceCnyFormat2(String balanceCnyFormat2) {
        this.balanceCnyFormat2 = balanceCnyFormat2;
    }

    public boolean isBalanceUsdSmaller() {
        return balanceUsdSmaller;
    }

    public void setBalanceUsdSmaller(boolean balanceUsdSmaller) {
        this.balanceUsdSmaller = balanceUsdSmaller;
    }

    public String getEstimatedBalanceCny() {
        return estimatedBalanceCny;
    }

    public void setEstimatedBalanceCny(String estimatedBalanceCny) {
        this.estimatedBalanceCny = estimatedBalanceCny;
    }

    public String getDifBalanceUsd() {
        return difBalanceUsd;
    }

    public void setDifBalanceUsd(String difBalanceUsd) {
        this.difBalanceUsd = difBalanceUsd;
    }

    public String getFreezeBalanceCny() {
        return freezeBalanceCny;
    }

    public void setFreezeBalanceCny(String freezeBalanceCny) {
        this.freezeBalanceCny = freezeBalanceCny;
    }

    public String getBalanceUsd() {
        return balanceUsd;
    }

    public void setBalanceUsd(String balanceUsd) {
        this.balanceUsd = balanceUsd;
    }

    public String getEstimatedBalanceUsd() {
        return estimatedBalanceUsd;
    }

    public void setEstimatedBalanceUsd(String estimatedBalanceUsd) {
        this.estimatedBalanceUsd = estimatedBalanceUsd;
    }

    public String getFreezeBalanceUsd() {
        return freezeBalanceUsd;
    }

    public void setFreezeBalanceUsd(String freezeBalanceUsd) {
        this.freezeBalanceUsd = freezeBalanceUsd;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDifBalanceCnyNum() {
        return difBalanceCnyNum;
    }

    public void setDifBalanceCnyNum(double difBalanceCnyNum) {
        this.difBalanceCnyNum = difBalanceCnyNum;
    }

    public boolean isBalanceCnySmaller() {
        return balanceCnySmaller;
    }

    public void setBalanceCnySmaller(boolean balanceCnySmaller) {
        this.balanceCnySmaller = balanceCnySmaller;
    }

}
