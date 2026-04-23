package com.busbooking.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.busbooking.model.Payment;
import com.busbooking.busbooking.pattern.factory.PaymentFactory;
import com.busbooking.busbooking.pattern.factory.PaymentMethod;
import com.busbooking.busbooking.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repo;

    public Payment processPayment(Payment payment) {

        PaymentMethod method = PaymentFactory.getPayment(payment.getMethod());

        if(method == null) {
            payment.setStatus("FAILED");
            return payment;
        }

        method.pay(payment.getAmount());

        payment.setStatus("SUCCESS");
        Payment savedPayment = repo.save(payment);
        
        return savedPayment;
    }
    
    public String processPaymentString(Payment payment) {
        PaymentMethod method = PaymentFactory.getPayment(payment.getMethod());

        if(method == null) {
            return "Invalid Payment Method";
        }

        String result = method.pay(payment.getAmount());

        payment.setStatus("SUCCESS");
        repo.save(payment);

        return result;
    }

    public Payment getPaymentDetails(int paymentId) {
        return repo.findById(paymentId).orElse(null);
    }

    /**
     * Process refund for cancelled booking
     */
    public String refundPayment(int paymentId) {
        Payment payment = repo.findById(paymentId).orElse(null);
        
        if(payment == null) {
            return "Payment not found";
        }
        
        if(!payment.getStatus().equals("SUCCESS")) {
            return "Only successful payments can be refunded";
        }
        
        PaymentMethod method = PaymentFactory.getPayment(payment.getMethod());
        
        if(method == null) {
            return "Invalid Payment Method";
        }
        
        // Process refund using the payment method
        String result = method.refund(payment.getAmount());
        
        // Update payment status to REFUNDED
        payment.setStatus("REFUNDED");
        repo.save(payment);
        
        return result;
    }
}