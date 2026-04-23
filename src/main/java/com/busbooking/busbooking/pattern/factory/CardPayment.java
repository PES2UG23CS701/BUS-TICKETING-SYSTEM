package com.busbooking.busbooking.pattern.factory;

public class CardPayment implements PaymentMethod {

    public String pay(double amount) {
        return "Payment of " + amount + " done via Card";
    }

    public String refund(double amount) {
        return "Refund of " + amount + " processed to Card";
    }
}