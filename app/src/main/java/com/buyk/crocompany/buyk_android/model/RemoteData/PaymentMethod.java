package com.buyk.crocompany.buyk_android.model.RemoteData;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
   private boolean payment_method_card;
   private boolean  payment_method_cash;
   private boolean  payment_method_loan;
   private boolean  payment_method_trade;
   private boolean  payment_method_lease;

    public boolean isPayment_method_card() {
        return payment_method_card;
    }

    public void setPayment_method_card(boolean payment_method_card) {
        this.payment_method_card = payment_method_card;
    }

    public boolean isPayment_method_cash() {
        return payment_method_cash;
    }

    public void setPayment_method_cash(boolean payment_method_cash) {
        this.payment_method_cash = payment_method_cash;
    }

    public boolean isPayment_method_loan() {
        return payment_method_loan;
    }

    public void setPayment_method_loan(boolean payment_method_loan) {
        this.payment_method_loan = payment_method_loan;
    }

    public boolean isPayment_method_trade() {
        return payment_method_trade;
    }

    public void setPayment_method_trade(boolean payment_method_trade) {
        this.payment_method_trade = payment_method_trade;
    }

    public boolean isPayment_method_lease() {
        return payment_method_lease;
    }

    public void setPayment_method_lease(boolean payment_method_lease) {
        this.payment_method_lease = payment_method_lease;
    }

}
