package com.busbooking.busbooking.pattern.factory;

public class PaymentFactory {

    public static PaymentMethod getPayment(String type) {

        if(type.equalsIgnoreCase("UPI")) {
            return new UpiPayment();
        }
        else if(type.equalsIgnoreCase("CARD")) {
            return new CardPayment();
        }

        return null;
    }
}