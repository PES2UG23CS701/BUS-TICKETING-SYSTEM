package com.busbooking.busbooking.pattern.factory;

public interface PaymentMethod {
    String pay(double amount);
    String refund(double amount);
}