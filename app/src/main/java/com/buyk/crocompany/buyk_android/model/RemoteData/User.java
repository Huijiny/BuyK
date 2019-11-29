package com.buyk.crocompany.buyk_android.model.RemoteData;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String nickname;
    private String phone;
    private String profile_image_url;
    private String profile_thumbnail_image_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_thumbnail_image_url() {
        return profile_thumbnail_image_url;
    }

    public void setProfile_thumbnail_image_url(String profile_thumbnail_image_url) {
        this.profile_thumbnail_image_url = profile_thumbnail_image_url;
    }
}
