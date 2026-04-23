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

import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.service.BusService;
import com.busbooking.busbooking.service.BookingService;
import com.busbooking.busbooking.repository.BusRepository;
import com.busbooking.busbooking.repository.BookingRepository;

@RestController
@RequestMapping("/operator")
@CrossOrigin("*")
public class BusOperatorController {

    @Autowired
    private BusService busService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private BusRepository busRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    // ==================== BUS MANAGEMENT ====================
    
    @PostMapping("/{operatorId}/bus/add")
    public ResponseEntity<?> addBus(@PathVariable int operatorId, @RequestBody Bus bus) {
        try {
            bus.setOperatorId(operatorId);
            Bus savedBus = busService.addBus(bus);
            return ResponseEntity.ok(Map.of("message", "Bus added successfully", "bus", savedBus));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{operatorId}/buses")
    public ResponseEntity<?> getOperatorBuses(@PathVariable int operatorId) {
        try {
            List<Bus> buses = busRepository.findByOperatorIdOrderByDepartureTime(operatorId);
            return ResponseEntity.ok(buses);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{operatorId}/bus/{busId}")
    public ResponseEntity<?> getBusDetails(@PathVariable int operatorId, @PathVariable int busId) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized - You don't own this bus"));
            }
            return ResponseEntity.ok(bus);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{operatorId}/bus/{busId}")
    public ResponseEntity<?> updateBus(@PathVariable int operatorId, @PathVariable int busId, @RequestBody Bus busDetails) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized - You don't own this bus"));
            }
            
            if (busDetails.getBusName() != null) bus.setBusName(busDetails.getBusName());
            if (busDetails.getSource() != null) bus.setSource(busDetails.getSource());
            if (busDetails.getDestination() != null) bus.setDestination(busDetails.getDestination());
            if (busDetails.getTotalSeats() > 0) bus.setTotalSeats(busDetails.getTotalSeats());
            if (busDetails.getPrice() > 0) bus.setPrice(busDetails.getPrice());
            if (busDetails.getDepartureTime() != null) bus.setDepartureTime(busDetails.getDepartureTime());
            if (busDetails.getAvailableSeats() >= 0) bus.setAvailableSeats(busDetails.getAvailableSeats());
            
            Bus updatedBus = busService.updateBus(bus);
            return ResponseEntity.ok(Map.of("message", "Bus updated successfully", "bus", updatedBus));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{operatorId}/bus/{busId}")
    public ResponseEntity<?> deleteBus(@PathVariable int operatorId, @PathVariable int busId) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized - You don't own this bus"));
            }
            busService.deleteBus(busId);
            return ResponseEntity.ok(Map.of("message", "Bus deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== SEAT AVAILABILITY MANAGEMENT ====================
    
    @PutMapping("/{operatorId}/bus/{busId}/availability")
    public ResponseEntity<?> updateSeatAvailability(@PathVariable int operatorId, @PathVariable int busId, 
                                                     @RequestBody Map<String, Integer> payload) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized - You don't own this bus"));
            }
            
            Integer availableSeats = payload.get("availableSeats");
            if (availableSeats != null && availableSeats >= 0 && availableSeats <= bus.getTotalSeats()) {
                bus.setAvailableSeats(availableSeats);
                Bus updatedBus = busService.updateBus(bus);
                return ResponseEntity.ok(Map.of("message", "Availability updated", "availableSeats", updatedBus.getAvailableSeats()));
            }
            return ResponseEntity.status(400).body(Map.of("error", "Invalid availability value"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{operatorId}/bus/{busId}/schedule")
    public ResponseEntity<?> updateSchedule(@PathVariable int operatorId, @PathVariable int busId, 
                                           @RequestBody Map<String, String> payload) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized - You don't own this bus"));
            }
            
            String departureTime = payload.get("departureTime");
            if (departureTime != null && !departureTime.isEmpty()) {
                bus.setDepartureTime(departureTime);
                Bus updatedBus = busService.updateBus(bus);
                return ResponseEntity.ok(Map.of("message", "Schedule updated", "departureTime", updatedBus.getDepartureTime()));
            }
            return ResponseEntity.status(400).body(Map.of("error", "Invalid departure time"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== BOOKING MANAGEMENT ====================
    
    @GetMapping("/{operatorId}/bookings")
    public ResponseEntity<?> getOperatorBookings(@PathVariable int operatorId) {
        try {
            List<Bus> buses = busRepository.findByOperatorId(operatorId);
            int totalBookings = 0;
            double totalRevenue = 0;
            
            for (Bus bus : buses) {
                List<Booking> bookings = bookingService.getBookingsByBus(bus.getId());
                totalBookings += bookings.size();
                for (Booking booking : bookings) {
                    if ("CONFIRMED".equals(booking.getStatus())) {
                        totalRevenue += booking.getPrice();
                    }
                }
            }
            
            return ResponseEntity.ok(Map.of(
                "totalBuses", buses.size(),
                "totalBookings", totalBookings,
                "totalRevenue", totalRevenue,
                "currency", "₹"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{operatorId}/bookings/{busId}")
    public ResponseEntity<?> getBusBookings(@PathVariable int operatorId, @PathVariable int busId) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            if (bus.getOperatorId() != operatorId) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
            }
            
            List<Booking> bookings = bookingService.getBookingsByBus(busId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== STATISTICS ====================
    
    @GetMapping("/{operatorId}/stats")
    public ResponseEntity<?> getOperatorStats(@PathVariable int operatorId) {
        try {
            List<Bus> buses = busRepository.findByOperatorId(operatorId);
            int totalSeats = buses.stream().mapToInt(Bus::getTotalSeats).sum();
            int availableSeats = buses.stream().mapToInt(Bus::getAvailableSeats).sum();
            int bookedSeats = totalSeats - availableSeats;
            
            // Calculate total bookings and revenue from confirmed bookings
            int totalBookings = 0;
            double totalRevenue = 0;
            
            for (Bus bus : buses) {
                List<Booking> bookings = bookingRepository.findByBusId(bus.getId());
                totalBookings += bookings.size();
                totalRevenue += bookings.stream()
                    .filter(b -> "CONFIRMED".equals(b.getStatus()))
                    .mapToDouble(Booking::getPrice)
                    .sum();
            }
            
            return ResponseEntity.ok(Map.of(
                "totalBuses", buses.size(),
                "totalSeats", totalSeats,
                "bookedSeats", bookedSeats,
                "availableSeats", availableSeats,
                "occupancyRate", totalSeats > 0 ? (bookedSeats * 100.0 / totalSeats) : 0,
                "totalBookings", totalBookings,
                "totalRevenue", totalRevenue
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
