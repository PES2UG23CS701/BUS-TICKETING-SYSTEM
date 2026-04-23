package com.busbooking.busbooking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.service.BookingService;

@RestController
@RequestMapping("/booking")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.book(booking);
        
        if(savedBooking == null) {
            return ResponseEntity.status(400).body(Map.of("message", "Seat not available"));
        }
        
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getMyBookings(@PathVariable int userId) {
        return bookingService.getBookingsByUser(userId);
    }

    @GetMapping("/bus/{busId}")
    public List<Booking> getBookingsByBus(@PathVariable int busId) {
        return bookingService.getBookingsByBus(busId);
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable int bookingId) {
        boolean success = bookingService.cancelBooking(bookingId);
        
        if(success) {
            return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Unable to cancel booking"));
        }
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBookingPut(@PathVariable int bookingId) {
        boolean success = bookingService.cancelBooking(bookingId);
        
        if(success) {
            return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Unable to cancel booking"));
        }
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable int bookingId, @RequestBody Booking booking) {
        booking.setId(bookingId);
        Booking updated = bookingService.updateBooking(booking);
        
        if(updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "Unable to update booking"));
        }
    }

    @GetMapping("/available-seats/{busId}")
    public int getAvailableSeats(@PathVariable int busId) {
        return bookingService.getAvailableSeatsForBus(busId);
    }
}