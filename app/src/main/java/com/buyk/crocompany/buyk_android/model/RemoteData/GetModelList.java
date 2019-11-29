package com.buyk.crocompany.buyk_android.model.RemoteData;

import java.util.List;

public class GetModelList {
    int model;
    String deal_status;
    String deal_area;
    int model_year_get;
    int model_year_lte;
    int price_gte;
    int price_lte;
    int distance_lte;
    int distance_gte;
    PaymentMethod payment_method;
    List<Image>images;
    User user;
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }



    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }

    public String getDeal_area() {
        return deal_area;
    }

    public void setDeal_area(String deal_area) {
        this.deal_area = deal_area;
    }

    public int getModel_year_get() {
        return model_year_get;
    }

    public void setModel_year_get(int model_year_get) {
        this.model_year_get = model_year_get;
    }

    public int getModel_year_lte() {
        return model_year_lte;
    }

    public void setModel_year_lte(int model_year_lte) {
        this.model_year_lte = model_year_lte;
    }

    public int getPrice_gte() {
        return price_gte;
    }

    public void setPrice_gte(int price_gte) {
        this.price_gte = price_gte;
    }

    public int getPrice_lte() {
        return price_lte;
    }

    public void setPrice_lte(int price_lte) {
        this.price_lte = price_lte;
    }

    public int getDistance_lte() {
        return distance_lte;
    }

    public void setDistance_lte(int distance_lte) {
        this.distance_lte = distance_lte;
    }

    public int getDistance_gte() {
        return distance_gte;
    }

    public void setDistance_gte(int distance_gte) {
        this.distance_gte = distance_gte;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
