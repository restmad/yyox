package com.yyox.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by 95 on 2017/5/8.
 */

public class User implements Serializable{

    private String identifier;
    private String level;
    private String mobile;
    private String mail;
    private String nickname;
    private String qq;
    private String phone;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public User() {
        this.identifier = "";
        this.level = "";
        this.mobile = "";
        this.mail = "";
        this.nickname = "";
        this.qq = "";
        this.phone = "";
        this.email = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
    }

    public User(String identifier,String level,String mobile,String mail,String nickname,String qq,String phone, String email, String password,String firstName,String lastName) {
        this.identifier = identifier;
        this.level = level;
        this.mobile = mobile;
        this.mail = mail;
        this.nickname = nickname;
        this.qq = qq;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
