package com.yyox.mvp.model.realm;

import io.realm.RealmObject;

/**
 * Created by dadaniu on 2017-01-17.
 */

public class RealmUser extends RealmObject {

    private String name;
    private String pwd;
    private String email;

    public String getName() { return name; }
    public void   setName(String name) { this.name = name; }

    public String getPwd() { return pwd; }
    public void   setPwd(String pwd) { this.pwd = pwd; }

    public String getEmail() {
        return email;
    }
    public void   setEmail(String email) {
        this.email = email;
    }


}
