package com.buyk.crocompany.buyk_android.model.RemoteData;

/**
 * Created by leeyun on 2018. 8. 5..
 */

public class KaKaoAuthorize {
    String external_id;
    String login_type;
    String access_token;


    public String getExternal_id() {
        return external_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
