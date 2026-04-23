package com.busbooking.busbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.busbooking.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}