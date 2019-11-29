package com.buyk.crocompany.buyk_android.model.RemoteData;

/**
 * Created by ahdguialee on 2018. 8. 13..
 */

public class AuthorizeResponse {
    String external_id;
    String login_type;
    int id;
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
