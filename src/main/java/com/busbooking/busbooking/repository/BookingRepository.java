package com.busbooking.busbooking.repository;

import com.busbooking.busbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(int userId);
    List<Booking> findByBusId(int busId);
    Booking findByBusIdAndSeatNumber(int busId, int seatNumber);
}