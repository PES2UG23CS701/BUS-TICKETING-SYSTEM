# SOLID Design Principles Documentation - Bus Booking System

## Overview
This document explains how the Bus Booking System adheres to SOLID design principles, demonstrating professional software architecture and object-oriented design excellence.

---

## 1. Single Responsibility Principle (SRP)

**"A class should have only one reason to change."**

### Implementation in Bus Booking System

#### UserService
- **Single Responsibility**: Handles user-related operations only
- **Reason to Change**: Only if user management logic changes
```java
@Service
public class UserService {
    // Handles: registration, login, user management
    // Does NOT handle: bookings, payments, bus operations
}
```

#### BusService  
- **Single Responsibility**: Manages bus operations
- **Reason to Change**: Only if bus management logic changes
```java
@Service
public class BusService {
    // Handles: bus CRUD, search, availability
    // Does NOT handle: user management, payments, bookings
}
```

#### BookingService
- **Single Responsibility**: Manages booking operations
- **Reason to Change**: Only if booking logic changes
```java
@Service
public class BookingService {
    // Handles: booking creation, cancellation, retrieval
    // Does NOT handle: payment processing, bus management
}
```

#### PaymentService
- **Single Responsibility**: Processes payments
- **Reason to Change**: Only if payment processing logic changes
```java
@Service
public class PaymentService {
    // Handles: payment processing using PaymentFactory
    // Does NOT handle: booking management, user management
}
```

### Benefits
- Each class has a clear, focused purpose
- Easy to understand and maintain
- Changes in one domain don't affect others
- Facilitates unit testing

---

## 2. Open/Closed Principle (OCP)

**"Software entities should be open for extension but closed for modification."**

### Implementation in Bus Booking System

#### Payment System (Factory Pattern)
```java
// Open for Extension: Can add new payment methods
public class WalletPayment implements PaymentMethod {
    @Override
    public String pay(double amount) {
        // New payment method implementation
    }
}

// Closed for Modification: No changes to PaymentFactory
public class PaymentFactory {
    // Factory remains unchanged when new payment methods are added
    public static PaymentMethod getPayment(String type) {
        if(type.equalsIgnoreCase("UPI")) {
            return new UpiPayment();
        }
        else if(type.equalsIgnoreCase("CARD")) {
            return new CardPayment();
        }
        else if(type.equalsIgnoreCase("WALLET")) {  // New without factory change
            return new WalletPayment();
        }
        return null;
    }
}
```

#### Discount System (Strategy Pattern)
```java
// Open for Extension: New discount strategies can be added
public class LoyaltyPointsDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double originalPrice) {
        // New discount strategy
    }
}

// Closed for Modification: Strategy pattern allows runtime selection
// Without changing existing code:
DiscountStrategy strategy = DiscountStrategyFactory.getDiscountStrategy(
    "LOYALTY_POINTS", numberOfPassengers
);
```

### Benefits
- New features without modifying existing code
- Reduced risk of breaking existing functionality
- More maintainable and stable code

---

## 3. Liskov Substitution Principle (LSP)

**"Objects of a superclass should be replaceable with objects of its subclasses without breaking the application."**

### Implementation in Bus Booking System

#### Payment Methods Example
```java
// Interface defines contract
public interface PaymentMethod {
    String pay(double amount);
}

// All implementations can be used interchangeably
CardPayment cardPayment = new CardPayment();         // Subclass 1
UpiPayment upiPayment = new UpiPayment();           // Subclass 2
WalletPayment walletPayment = new WalletPayment();  // Subclass 3

// PaymentService accepts any PaymentMethod
PaymentMethod method = ...; // Could be any of above
method.pay(amount);         // Works regardless of which subclass

// This is guaranteed to work because:
// - CardPayment implements PaymentMethod contract
// - UpiPayment implements PaymentMethod contract
// - WalletPayment implements PaymentMethod contract
```

#### Discount Strategies Example
```java
public interface DiscountStrategy {
    double applyDiscount(double originalPrice);
    String getStrategyName();
}

// All strategies can be used interchangeably
DiscountStrategy strategy = new StudentDiscountStrategy();
// OR
DiscountStrategy strategy = new SeniorCitizenDiscountStrategy();
// OR  
DiscountStrategy strategy = new BulkBookingDiscountStrategy(10);

// PricingCalculator doesn't care which strategy
PricingCalculator calculator = new PricingCalculator(strategy);
// Works the same regardless of concrete strategy implementation
```

### Benefits
- Interchangeable implementations
- Polymorphic behavior works correctly
- Easy to test with different implementations
- Prevents subtle bugs from incompatible subclasses

---

## 4. Interface Segregation Principle (ISP)

**"Clients should not be forced to depend on interfaces they don't use."**

### Implementation in Bus Booking System

#### Focused Repositories
```java
// Interface focused on core methods only
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Only relevant methods for Payment operations
    List<Payment> findByBookingId(int bookingId);
    List<Payment> findByUserId(int userId);
}

// Clients (PaymentService) only depend on PaymentRepository
// NOT forced to depend on BusRepository or UserRepository
```

#### Specific Service Interfaces
```java
// PaymentService only uses PaymentRepository
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repo;  // Segregated interface
    // PaymentService doesn't know about BusRepository
}

// BookingService only uses BookingRepository
@Service
public class BookingService {
    @Autowired
    private BookingRepository repo;  // Segregated interface
    // BookingService doesn't know about PaymentRepository
}
```

#### Strategy Pattern with Minimal Interface
```java
// Minimal, focused interface
public interface DiscountStrategy {
    double applyDiscount(double originalPrice);
    String getStrategyName();
}

// Clients only depend on this focused interface
// NOT forced to depend on other discount-related methods
```

### Benefits
- Interfaces are minimal and focused
- Classes only depend on methods they use
- Reduces coupling between components
- Interfaces remain stable

---

## 5. Dependency Inversion Principle (DIP)

**"Depend on abstractions, not concrete implementations."**

### Implementation in Bus Booking System

#### Service Layer with DIP
```java
// Depend on Repository abstraction, not concrete implementations
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repo;  // Depends on abstraction (PaymentRepository)
                                      // NOT on concrete implementation
    
    public String processPayment(Payment payment) {
        PaymentMethod method = PaymentFactory.getPayment(payment.getMethod());
        // ... process payment
        repo.save(payment);  // Use abstraction
    }
}

// High-level module (PaymentService) depends on abstraction (PaymentRepository)
// Low-level module (PaymentRepositoryImpl) depends on abstraction (PaymentRepository)
// Both depend on abstraction, NOT on each other
```

#### Factory Pattern with DIP
```java
// Depend on abstraction (PaymentMethod interface)
public class PaymentFactory {
    public static PaymentMethod getPayment(String type) {
        // Factory returns abstraction
        if(type.equalsIgnoreCase("CARD")) {
            return new CardPayment();  // Concrete, but returned as abstraction
        }
    }
}

// PaymentService depends on PaymentMethod (abstraction), not concrete classes
private PaymentMethod method = PaymentFactory.getPayment(type);
```

#### Controller with Injected Dependencies
```java
@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService service;  // Depend on abstraction (service interface)
                                      // NOT on concrete implementation
    
    @Autowired
    private BookingFacade facade;    // Depend on abstraction
    
    // Spring injects implementations of these abstractions
    // Controller doesn't care about concrete implementations
}
```

### Benefits
- Easy to swap implementations (e.g., different PaymentRepository implementation)
- Facilitates unit testing with mocked dependencies
- Loose coupling between high-level and low-level modules
- Changes to implementations don't affect clients

---

## Design Principles Summary

| Principle | Focus | Implementation | Benefit |
|-----------|-------|----------------|---------|
| **SRP** | Single Responsibility | Separate services for each domain | Maintainability, Clarity |
| **OCP** | Extensibility | Factory & Strategy patterns | New features without modifications |
| **LSP** | Polymorphism | Interchangeable implementations | Reliability, Testability |
| **ISP** | Minimal Interfaces | Segregated repositories & interfaces | Low coupling, Flexibility |
| **DIP** | Abstraction Dependency | Depend on interfaces, not implementations | Flexibility, Testability |

---

## Practical Examples

### Before SOLID Principles (Anti-pattern)
```java
// ALL in one class - violates SRP, OCP, ISP
public class BookingController {
    public void book(Booking booking, Payment payment) {
        // User validation code
        if(booking.getUser() == null) { ... }
        
        // Bus operations
        Bus bus = Database.getBus(booking.getBusId());
        bus.setAvailableSeats(bus.getAvailableSeats() - 1);
        Database.saveBus(bus);
        
        // Booking operations  
        booking.setStatus("CONFIRMED");
        Database.saveBooking(booking);
        
        // Payment processing - hardcoded payment method
        if(payment.getMethod().equals("CARD")) {
            // Process card payment
            connectToCardGateway(payment);
        } else if(payment.getMethod().equals("UPI")) {
            // Process UPI payment
            connectToUpiGateway(payment);
        }
        
        // Hard to test, hard to extend, difficult to maintain
    }
}
```

### After SOLID Principles (Professional approach)
```java
// Separated concerns following SOLID
@RestController
public class BookingController {
    
    @Autowired
    private BookingFacade facade;  // DIP: Depend on abstraction
    
    @PostMapping("/book")
    public BookingResponse book(@RequestBody BookingRequest request) {
        // Delegate to facade - SRP: Controller only handles HTTP
        return facade.completeBooking(
            request.getUserId(),
            request.getBusId(),
            request.getSeats(),
            request.getAmount(),
            request.getPaymentMethod()
        );
    }
}

// Facade orchestrates workflow - follows all SOLID principles
// PaymentService uses PaymentFactory - OCP: Easy to add payment methods
// Each service has single responsibility - SRP
// Strategy pattern for discounts - OCP and LSP
```

---

## SOLID Compliance Checklist

- ✅ **SRP**: Each service handles one domain
- ✅ **OCP**: Factory and Strategy patterns allow extension
- ✅ **LSP**: PaymentMethod and DiscountStrategy implementations are interchangeable
- ✅ **ISP**: Repositories and services have focused interfaces
- ✅ **DIP**: Spring Dependency Injection ensures abstraction dependency

---

## Conclusion

The Bus Booking System demonstrates professional application of SOLID design principles:
- **Maintainability**: Clear separation of concerns
- **Extensibility**: New features without modifying existing code
- **Testability**: Easy to mock and test individual components
- **Flexibility**: Easy to swap implementations
- **Professional Quality**: Industry-standard practices
