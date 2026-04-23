package com.busbooking.busbooking.pattern.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton Pattern Implementation
 * 
 * Purpose: Ensures only one instance of BookingLogger exists throughout the application.
 * This centralized logger tracks all booking operations.
 * 
 * Benefits:
 * - Single point of logging for all booking transactions
 * - Thread-safe instance creation
 * - Prevents multiple logging instances which would waste resources
 */
public class BookingLogger {
    
    private static BookingLogger instance;
    private static final Object lock = new Object();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Private constructor prevents instantiation from outside
    private BookingLogger() {
    }
    
    // Thread-safe getInstance method (Double-checked locking)
    public static BookingLogger getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new BookingLogger();
                }
            }
        }
        return instance;
    }
    
    /**
     * Logs booking creation
     */
    public void logBookingCreated(int bookingId, int userId, int busId, int seats) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] BOOKING CREATED - ID: " + bookingId + 
                           ", User: " + userId + ", Bus: " + busId + ", Seats: " + seats);
    }
    
    /**
     * Logs booking cancellation
     */
    public void logBookingCancelled(int bookingId) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] BOOKING CANCELLED - ID: " + bookingId);
    }
    
    /**
     * Logs payment processing
     */
    public void logPaymentProcessed(int paymentId, double amount, String method) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] PAYMENT PROCESSED - ID: " + paymentId + 
                           ", Amount: " + amount + ", Method: " + method);
    }
    
    /**
     * Logs search operation
     */
    public void logBusSearch(String source, String destination) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] BUS SEARCH - From: " + source + " To: " + destination);
    }
}
