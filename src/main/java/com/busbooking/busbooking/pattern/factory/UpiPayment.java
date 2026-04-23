package com.busbooking.busbooking.pattern.factory;

public class UpiPayment implements PaymentMethod {

    public String pay(double amount) {
        return "Payment of " + amount + " done via UPI";
    }

    public String refund(double amount) {
        return "Refund of " + amount + " processed via UPI";
    }
}