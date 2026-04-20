package com.busbooking.busbooking.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.busbooking.model.Payment;
import com.busbooking.busbooking.service.PaymentService;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody Payment payment) {
        try {
            Payment savedPayment = service.processPayment(payment);
            
            if(savedPayment != null && savedPayment.getStatus().equals("SUCCESS")) {
                return ResponseEntity.ok(Map.of("message", "Payment successful", "paymentId", savedPayment.getId(), "payment", savedPayment));
            } else {
                return ResponseEntity.status(400).body(Map.of("message", "Payment processing failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Payment processing error: " + e.getMessage()));
        }
    }

    @GetMapping("/{paymentId}")
    public Payment getPaymentDetails(@PathVariable int paymentId) {
        return service.getPaymentDetails(paymentId);
    }

    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<?> refundPayment(@PathVariable int paymentId) {
        String result = service.refundPayment(paymentId);
        
        if(result.contains("Refund") && !result.contains("not found") && !result.contains("Cannot")) {
            return ResponseEntity.ok(Map.of("message", result, "paymentId", paymentId));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", result));
        }
    }
}
