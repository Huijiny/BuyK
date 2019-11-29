package com.buyk.crocompany.buyk_android.model.RemoteData;

import java.io.Serializable;
import java.util.List;

public class ResponseModelList implements Serializable {
    private int id;
    private String model;
    private String deal_status;
    private PaymentResponseMethod payment_method;
    private List<Image>images;
    private String deal_area;
    private int driven_distance;
    private String document_status;
    private String repair_history;
    private String tuning_history;
    private String detail_information;
    private String crawled_url;
    private String created_at;
    private String updated_at;
    private int model_year;
    private int price;
    private boolean liked;
    private int user_id;
    User user;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }

    public PaymentResponseMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentResponseMethod payment_method) {
        this.payment_method = payment_method;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getDeal_area() {
        return deal_area;
    }

    public void setDeal_area(String deal_area) {
        this.deal_area = deal_area;
    }

    public int getDriven_distance() {
        return driven_distance;
    }

    public void setDriven_distance(int driven_distance) {
        this.driven_distance = driven_distance;
    }

    public String getDocument_status() {
        return document_status;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public String getRepair_history() {
        return repair_history;
    }

    public void setRepair_history(String repair_history) {
        this.repair_history = repair_history;
    }

    public String getTuning_history() {
        return tuning_history;
    }

    public void setTuning_history(String tuning_history) {
        this.tuning_history = tuning_history;
    }

    public String getDetail_information() {
        return detail_information;
    }

    public void setDetail_information(String detail_information) {
        this.detail_information = detail_information;
    }

    public String getCrawled_url() {
        return crawled_url;
    }

    public void setCrawled_url(String crawled_url) {
        this.crawled_url = crawled_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getModel_year() {
        return model_year;
    }

    public void setModel_year(int model_year) {
        this.model_year = model_year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
