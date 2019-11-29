package com.buyk.crocompany.buyk_android.model.RemoteData;

/**
 * Created by leeyun on 2018. 7. 15..
 */

public class JoinResponse {
    int id;
    String token;
    String recovery_token;
    String deadline;
    String message;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getRecovery_token() {
        return recovery_token;
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
