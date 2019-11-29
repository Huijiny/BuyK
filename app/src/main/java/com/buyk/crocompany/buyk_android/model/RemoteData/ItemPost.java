package com.buyk.crocompany.buyk_android.model.RemoteData;

public class ItemPost {
    private int model;
    private String deal_area=null;
    private int model_year;
    private int driven_distance;
    private String document_status=null;
    private String repair_history=null;
    private int price;
    private String tuning_history;
    private String detail_information=null;
    private PaymentResponseMethod payment_method;
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {

        return price;
    }

    public void setModel_id(int model_id) {
        this.model = model_id;
    }

    public void setDeal_area(String deal_area) {
        this.deal_area = deal_area;
    }

    public void setModel_year(int model_year) {
        this.model_year = model_year;
    }

    public void setDriven_distance(int driven_distance) {
        this.driven_distance = driven_distance;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public void setRepair_history(String repair_history) {
        this.repair_history = repair_history;
    }

    public void setTuning_history(String tuning_history) {
        this.tuning_history = tuning_history;
    }

    public void setDetail_information(String detail_information) {
        this.detail_information = detail_information;
    }

    public int getModel_id() {

        return model;
    }

    public String getDeal_area() {
        return deal_area;
    }

    public int getModel_year() {
        return model_year;
    }

    public int getDriven_distance() {
        return driven_distance;
    }

    public String getDocument_status() {
        return document_status;
    }

    public String getRepair_history() {
        return repair_history;
    }

    public String getTuning_history() {
        return tuning_history;
    }

    public String getDetail_information() {
        return detail_information;
    }

   /* public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }*/

    public PaymentResponseMethod getPayment_method() {

        return payment_method;
    }

    public void setPayment_method(PaymentResponseMethod payment_method) {
        this.payment_method = payment_method;
    }
}
