package com.busbooking.busbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.repository.BookingRepository;
import com.busbooking.busbooking.repository.BusRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private BusRepository busRepo;
    
    @Autowired
    private PaymentService paymentService;

    public Booking book(Booking booking) {
        // Check if seat is available
        Booking existingBooking = bookingRepo.findByBusIdAndSeatNumber(booking.getBusId(), booking.getSeatNumber());
        if(existingBooking != null) {
            return null; // Seat already booked
        }
        
        booking.setStatus("CONFIRMED");
        booking.setBookingDate(LocalDateTime.now());
        
        // Reduce available seats
        Bus bus = busRepo.findById(booking.getBusId());
        if(bus != null && bus.getAvailableSeats() > 0) {
            bus.setAvailableSeats(bus.getAvailableSeats() - 1);
            busRepo.save(bus);
            booking.setPrice(bus.getPrice());
        }
        
        return bookingRepo.save(booking);
    }

    public List<Booking> getBookingsByUser(int userId) {
        return bookingRepo.findByUserId(userId);
    }

    public List<Booking> getBookingsByBus(int busId) {
        return bookingRepo.findByBusId(busId);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        
        if(booking == null || !booking.getStatus().equals("CONFIRMED")) {
            return false;
        }
        
        // Process refund if payment exists
        if(booking.getPaymentId() > 0) {
            String refundResult = paymentService.refundPayment(booking.getPaymentId());
            System.out.println("Refund processed: " + refundResult);
        }
        
        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);
        
        // Increase available seats
        Bus bus = busRepo.findById(booking.getBusId());
        if(bus != null) {
            bus.setAvailableSeats(bus.getAvailableSeats() + 1);
            busRepo.save(bus);
        }
        
        return true;
    }

    public Booking updateBooking(Booking booking) {
        Booking existing = bookingRepo.findById(booking.getId()).orElse(null);
        if(existing == null) {
            return null;
        }
        
        if(booking.getStatus() != null) {
            existing.setStatus(booking.getStatus());
        }
        
        if(booking.getPaymentId() > 0) {
            existing.setPaymentId(booking.getPaymentId());
        }
        
        return bookingRepo.save(existing);
    }

    public int getAvailableSeatsForBus(int busId) {
        Bus bus = busRepo.findById(busId);
        return bus != null ? bus.getAvailableSeats() : 0;
    }

    /**
     * Get booking by ID - used by Facade pattern
     */
    public Booking getBookingById(int bookingId) {
        return bookingRepo.findById(bookingId).orElse(null);
    }
}