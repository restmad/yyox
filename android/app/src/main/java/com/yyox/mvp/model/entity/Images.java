package com.yyox.mvp.model.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dadaniu on 2017-02-04.
 */

public class Images implements Serializable {

    private String path;
    private String type;
    private Bitmap bitmap;

    public Images(){
        this.path = "";
        this.type = "";
        this.bitmap = null;
    }

    public Images(String path, String type, Bitmap bitmap) {
        this.path = path;
        this.type = type;
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
