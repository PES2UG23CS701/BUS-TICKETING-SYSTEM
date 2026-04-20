package com.busbooking.busbooking.controller;

import java.util.ArrayList;
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
import com.busbooking.busbooking.model.Payment;
import com.busbooking.busbooking.model.User;
import com.busbooking.busbooking.service.BusService;
import com.busbooking.busbooking.service.BookingService;
import com.busbooking.busbooking.repository.PaymentRepository;
import com.busbooking.busbooking.repository.UserRepository;
import com.busbooking.busbooking.repository.BookingRepository;
import com.busbooking.busbooking.repository.BusRepository;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private BusService busService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private PaymentRepository paymentRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private BusRepository busRepository;

    // ==================== BUS MANAGEMENT ====================
    
    @PostMapping("/bus/add")
    public ResponseEntity<?> addBus(@RequestBody Bus bus) {
        try {
            Bus savedBus = busService.addBus(bus);
            return ResponseEntity.ok(Map.of("message", "Bus added successfully", "bus", savedBus));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/buses")
    public ResponseEntity<?> getAllBuses() {
        try {
            List<Bus> buses = busService.getAllBuses();
            return ResponseEntity.ok(buses);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<?> getBusDetails(@PathVariable int busId) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            return ResponseEntity.ok(bus);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/bus/{busId}")
    public ResponseEntity<?> updateBus(@PathVariable int busId, @RequestBody Bus busDetails) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
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

    @DeleteMapping("/bus/{busId}")
    public ResponseEntity<?> deleteBus(@PathVariable int busId) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            busService.deleteBus(busId);
            return ResponseEntity.ok(Map.of("message", "Bus deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/bus/{busId}/price")
    public ResponseEntity<?> updateBusPrice(@PathVariable int busId, @RequestBody Map<String, Double> payload) {
        try {
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Bus not found"));
            }
            
            Double newPrice = payload.get("price");
            if (newPrice != null && newPrice > 0) {
                bus.setPrice(newPrice);
                Bus updatedBus = busService.updateBus(bus);
                return ResponseEntity.ok(Map.of("message", "Price updated successfully", "newPrice", updatedBus.getPrice()));
            }
            return ResponseEntity.status(400).body(Map.of("error", "Invalid price"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== BOOKING MANAGEMENT ====================
    
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/bookings/route/{source}/{destination}")
    public ResponseEntity<?> getBookingsByRoute(@PathVariable String source, @PathVariable String destination) {
        try {
            return ResponseEntity.ok(Map.of("message", "Bookings for route retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== REVENUE MANAGEMENT ====================
    
    @GetMapping("/revenue")
    public ResponseEntity<?> getTotalRevenue() {
        try {
            List<Payment> allPayments = paymentRepo.findAll();
            double totalRevenue = allPayments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
            
            long totalBookings = allPayments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus()))
                .count();
            
            return ResponseEntity.ok(Map.of(
                "totalRevenue", totalRevenue,
                "totalBookings", totalBookings,
                "currency", "₹"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/revenue/route/{source}/{destination}")
    public ResponseEntity<?> getRevenueByRoute(@PathVariable String source, @PathVariable String destination) {
        try {
            List<Bus> buses = busService.searchBuses(source, destination);
            double routeRevenue = 0;
            long bookingCount = 0;
            
            for (Bus bus : buses) {
                List<Booking> bookings = bookingService.getBookingsByBus(bus.getId());
                for (Booking booking : bookings) {
                    if ("CONFIRMED".equals(booking.getStatus())) {
                        routeRevenue += booking.getPrice();
                        bookingCount++;
                    }
                }
            }
            
            return ResponseEntity.ok(Map.of(
                "route", source + " → " + destination,
                "routeRevenue", routeRevenue,
                "bookingCount", bookingCount,
                "currency", "₹"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/revenue/by-route")
    public ResponseEntity<?> getRevenueByAllRoutes() {
        try {
            Map<String, Map<String, Object>> routeRevenueMap = new java.util.LinkedHashMap<>();
            
            // Get all buses to collect routes
            List<Bus> allBuses = busService.getAllBuses();
            
            for (Bus bus : allBuses) {
                String routeKey = bus.getSource() + " → " + bus.getDestination();
                
                // Initialize route map if not exists
                if (!routeRevenueMap.containsKey(routeKey)) {
                    routeRevenueMap.put(routeKey, new java.util.HashMap<>(Map.of(
                        "bookings", 0L,
                        "revenue", 0.0
                    )));
                }
                
                // Get bookings for this bus
                List<Booking> bookings = bookingService.getBookingsByBus(bus.getId());
                Map<String, Object> routeData = routeRevenueMap.get(routeKey);
                
                for (Booking booking : bookings) {
                    if ("CONFIRMED".equals(booking.getStatus())) {
                        long currentBookings = (long) routeData.get("bookings");
                        double currentRevenue = (double) routeData.get("revenue");
                        
                        routeData.put("bookings", currentBookings + 1);
                        routeData.put("revenue", currentRevenue + booking.getPrice());
                    }
                }
            }
            
            // Convert map to list
            List<Map<String, Object>> routes = new java.util.ArrayList<>();
            routeRevenueMap.forEach((route, data) -> {
                routes.add(Map.of(
                    "route", route,
                    "bookings", data.get("bookings"),
                    "revenue", data.get("revenue")
                ));
            });
            
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== OPERATORS ENDPOINT ====================
    
    @GetMapping("/operators")
    public ResponseEntity<?> getAllOperators() {
        try {
            // Get all users with BUS_OPERATOR role
            List<User> allUsers = userRepo.findAll();
            List<Map<String, Object>> operatorsList = new ArrayList<>();
            
            for (User user : allUsers) {
                if (user.getRole() != null && user.getRole().toString().equals("BUS_OPERATOR")) {
                    // Get buses for this operator
                    List<Bus> operatorBuses = busRepository.findByOperatorId(user.getId());
                    
                    // Calculate stats
                    int totalBuses = operatorBuses.size();
                    int totalBookings = 0;
                    double totalRevenue = 0;
                    
                    for (Bus bus : operatorBuses) {
                        List<Booking> bookings = bookingRepository.findByBusId(bus.getId());
                        totalBookings += bookings.size();
                        totalRevenue += bookings.stream()
                            .filter(b -> "CONFIRMED".equals(b.getStatus()))
                            .mapToDouble(Booking::getPrice)
                            .sum();
                    }
                    
                    operatorsList.add(Map.of(
                        "operatorId", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "totalBuses", totalBuses,
                        "totalBookings", totalBookings,
                        "revenue", totalRevenue
                    ));
                }
            }
            
            return ResponseEntity.ok(operatorsList);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ==================== STATISTICS ====================
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userRepo.findAll();
            List<Map<String, Object>> usersList = new ArrayList<>();
            
            for (User user : allUsers) {
                // Get bookings for this user
                List<Booking> bookings = bookingRepository.findAll();
                long userBookings = bookings.stream()
                    .filter(b -> b.getUserId() == user.getId())
                    .count();
                
                usersList.add(Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole() != null ? user.getRole().toString() : "USER",
                    "bookingsMade", userBookings
                ));
            }
            
            return ResponseEntity.ok(usersList);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<?> getSystemStats() {
        try {
            return ResponseEntity.ok(Map.of(
                "totalBuses", busService.getAllBuses().size(),
                "totalPayments", paymentRepo.findAll().size(),
                "message", "System statistics retrieved"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
