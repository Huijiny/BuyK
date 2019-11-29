package com.buyk.crocompany.buyk_android.model.LocalData;

import io.realm.RealmObject;

/**
 * Created by leeyun on 2018. 7. 15..
 */

public class LocalUserData extends RealmObject {
    int id;
    String token;
    String recovery_token;
    String deadline;
    String user_uuid;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getRecovery_token() {
        return recovery_token;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRecovery_token(String recovery_token) {
        this.recovery_token = recovery_token;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }
}
