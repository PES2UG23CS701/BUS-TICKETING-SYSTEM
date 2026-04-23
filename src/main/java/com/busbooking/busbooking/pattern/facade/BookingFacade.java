package com.busbooking.busbooking.pattern.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.model.Payment;
import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.service.BookingService;
import com.busbooking.busbooking.service.PaymentService;
import com.busbooking.busbooking.service.BusService;
import com.busbooking.busbooking.pattern.singleton.BookingLogger;

/**
 * Facade Pattern Implementation
 * 
 * Purpose: Provides a unified, simplified interface for complex subsystems.
 * Instead of client interacting with multiple services (Booking, Payment, Bus),
 * the facade handles complex interactions.
 * 
 * Benefits:
 * - Simplifies client code
 * - Decouples client from complex subsystems
 * - Provides single entry point for complete booking workflow
 * - Makes code more maintainable
 */
@Component
public class BookingFacade {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private BusService busService;
    
    private BookingLogger logger = BookingLogger.getInstance();
    
    /**
     * Complete booking workflow in one method
     * Handles: Search -> Validate -> Book -> Process Payment
     */
    public BookingResponse completeBooking(int userId, int busId, int seats, 
                                           double amount, String paymentMethod) {
        
        BookingResponse response = new BookingResponse();
        
        try {
            // Step 1: Check bus availability
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                response.setSuccess(false);
                response.setMessage("Bus not found");
                return response;
            }
            
            // Step 2: Validate seats
            if (bus.getAvailableSeats() < seats) {
                response.setSuccess(false);
                response.setMessage("Not enough available seats");
                logger.logBookingCreated(-1, userId, busId, seats);
                return response;
            }
            
            // Step 3: Create booking
            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setBusId(busId);
            booking.setSeatNumber(seats);  // Fixed: Use setSeatNumber instead of setNumberOfSeats
            booking.setStatus("PENDING");
            
            Booking createdBooking = bookingService.book(booking);
            
            if (createdBooking == null) {
                response.setSuccess(false);
                response.setMessage("Booking failed");
                return response;
            }
            
            logger.logBookingCreated(createdBooking.getId(), userId, busId, seats);
            
            // Step 4: Process payment
            Payment payment = new Payment();
            payment.setBookingId(createdBooking.getId());
            payment.setAmount(amount);
            payment.setMethod(paymentMethod);
            payment.setStatus("PENDING");
            
            Payment paymentResult = paymentService.processPayment(payment);
            logger.logPaymentProcessed(paymentResult.getId(), amount, paymentMethod);
            
            // Step 5: Update booking status based on payment
            if (paymentResult != null && paymentResult.getStatus().equals("SUCCESS")) {
                createdBooking.setStatus("CONFIRMED");
                createdBooking.setPaymentId(paymentResult.getId());
                bookingService.updateBooking(createdBooking);
                
                response.setSuccess(true);
                response.setMessage("Booking confirmed successfully");
                response.setBookingId(createdBooking.getId());
                response.setPaymentId(payment.getId());
            } else {
                createdBooking.setStatus("CANCELLED");
                bookingService.updateBooking(createdBooking);
                
                response.setSuccess(false);
                response.setMessage("Payment failed: " + paymentResult);
            }
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error during booking: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Cancel booking with refund processing
     */
    public BookingResponse cancelBooking(int bookingId) {
        BookingResponse response = new BookingResponse();
        
        try {
            Booking booking = bookingService.getBookingById(bookingId);
            
            if (booking == null) {
                response.setSuccess(false);
                response.setMessage("Booking not found");
                return response;
            }
            
            // Update booking status
            booking.setStatus("CANCELLED");
            bookingService.updateBooking(booking);
            
            logger.logBookingCancelled(bookingId);
            
            response.setSuccess(true);
            response.setMessage("Booking cancelled successfully");
            response.setBookingId(bookingId);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error cancelling booking: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Get complete booking details with bus and payment info
     */
    public CompleteBookingDetails getCompleteBookingDetails(int bookingId) {
        CompleteBookingDetails details = new CompleteBookingDetails();
        
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking != null) {
            Bus bus = busService.getBusById(booking.getBusId());
            details.setBooking(booking);
            details.setBus(bus);
        }
        
        return details;
    }
    
    // Inner classes for response
    public static class BookingResponse {
        private boolean success;
        private String message;
        private int bookingId;
        private int paymentId;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }
        
        public int getPaymentId() { return paymentId; }
        public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    }
    
    public static class CompleteBookingDetails {
        private Booking booking;
        private Bus bus;
        
        public Booking getBooking() { return booking; }
        public void setBooking(Booking booking) { this.booking = booking; }
        
        public Bus getBus() { return bus; }
        public void setBus(Bus bus) { this.bus = bus; }
    }
}
