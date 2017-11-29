package com.yyox.mvp.model.entity;

import com.yyox.mvp.model.api.Api;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-01-18.
 */

public class BaseJson<T> implements Serializable {

    private T data;
    private int status;
    private String msgs;
    private OtherJson other;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsgs() {
        return msgs;
    }

    public void setMsgs(String msgs) {
        this.msgs = msgs;
    }

    public OtherJson getOtherJson() {
        return other;
    }

    public void setOtherJson(OtherJson otherJson) {
        this.other = otherJson;
    }

    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess() {
        if (String.valueOf(status).equals(Api.RequestSuccess)) {
            return true;
        } else {
            return false;
        }
    }

}
