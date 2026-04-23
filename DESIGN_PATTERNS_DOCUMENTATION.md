# Design Patterns Documentation - Bus Booking System

## Overview
This document describes the 4 Design Patterns implemented in the Bus Booking System project, demonstrating compliance with OOAD principles and mini-project requirements.

---

## Pattern 1: Factory Pattern (Creational) ✅

### Location
`src/main/java/com/busbooking/busbooking/pattern/factory/`

### Files
- `PaymentFactory.java` - Factory class
- `PaymentMethod.java` - Strategy interface
- `CardPayment.java` - Concrete implementation
- `UpiPayment.java` - Concrete implementation

### Description
The Factory Pattern is used to create different payment method instances dynamically based on user input. This eliminates the need for multiple if-else statements in the PaymentService.

### Implementation
```java
// Factory creates payment objects dynamically
PaymentMethod method = PaymentFactory.getPayment(payment.getMethod());
String result = method.pay(payment.getAmount());
```

### Benefits
- Encapsulates object creation logic
- Easy to add new payment methods (Card, UPI, Wallet, etc.)
- Decouples payment processing from payment method creation
- Follows Open/Closed Principle - open for extension, closed for modification

---

## Pattern 2: Singleton Pattern (Creational) ✅

### Location
`src/main/java/com/busbooking/busbooking/pattern/singleton/BookingLogger.java`

### Description
Provides a centralized, single instance of the BookingLogger throughout the application. This ensures all booking operations are logged consistently from a single point.

### Implementation
```java
// Get singleton instance (thread-safe)
BookingLogger logger = BookingLogger.getInstance();

// Log booking operations
logger.logBookingCreated(bookingId, userId, busId, seats);
logger.logPaymentProcessed(paymentId, amount, paymentMethod);
logger.logBookingCancelled(bookingId);
```

### Key Features
- Thread-safe getInstance() using double-checked locking
- Private constructor prevents multiple instantiation
- Centralized logging for audit trail
- Consistency across application

### Benefits
- Single point of logging
- Prevents multiple logger instances wasting resources
- Maintains audit trail of all transactions
- Easy to extend with more logging methods

---

## Pattern 3: Facade Pattern (Structural) ✅

### Location
`src/main/java/com/busbooking/busbooking/pattern/facade/BookingFacade.java`

### Description
Provides a simplified, unified interface to complex subsystems. Instead of clients interacting with multiple services (BookingService, PaymentService, BusService), the Facade handles the complete workflow.

### Implementation
```java
// Simplified complete booking in one method call
BookingResponse response = bookingFacade.completeBooking(
    userId, busId, seats, amount, paymentMethod
);

// Cancel booking with all associated operations
BookingResponse cancelResponse = bookingFacade.cancelBooking(bookingId);

// Get complete booking details
CompleteBookingDetails details = bookingFacade.getCompleteBookingDetails(bookingId);
```

### Workflow
1. Validate bus availability
2. Check seat availability
3. Create booking
4. Process payment
5. Update booking status

### Benefits
- Simplifies client code
- Decouples client from internal subsystems
- Single entry point for complex operations
- Reduces code complexity
- Easy maintenance of workflow changes

---

## Pattern 4: Strategy Pattern (Behavioral) ✅

### Location
`src/main/java/com/busbooking/busbooking/pattern/strategy/DiscountStrategy.java`

### Description
Defines a family of discount algorithms and encapsulates each one, allowing selection at runtime. Different passenger types (Student, Senior Citizen, Bulk Booking) have different discount strategies.

### Implementation
```java
// Select strategy based on passenger type
DiscountStrategy strategy = DiscountStrategyFactory.getDiscountStrategy(
    "STUDENT", numberOfPassengers
);

// Calculate discounted price
PricingCalculator calculator = new PricingCalculator(strategy);
double finalPrice = calculator.calculateFinalPrice(basePrice);

// Change strategy at runtime
calculator.setDiscountStrategy(new SeniorCitizenDiscountStrategy());
```

### Concrete Strategies
1. **StudentDiscountStrategy** - 15% discount
2. **SeniorCitizenDiscountStrategy** - 20% discount
3. **BulkBookingDiscountStrategy** - 15-25% based on passenger count
4. **NoDiscountStrategy** - No discount (default)

### Benefits
- Easy to add new discount strategies without modifying existing code
- Runtime algorithm selection
- Follows Open/Closed Principle
- Cleaner than if-else chains
- Each strategy is independent and testable

---

## Relationship Between Patterns

```
┌─────────────────────────────────────────────────────────┐
│           Bus Booking System Architecture               │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  BookingFacade (Facade Pattern)                        │
│  ├─ Orchestrates booking workflow                       │
│  ├─ Uses: BookingService, PaymentService, BusService   │
│  ├─ Logs with: BookingLogger (Singleton)               │
│  └─ Calculates price with: DiscountStrategy (Strategy) │
│                                                          │
│  PaymentService                                         │
│  └─ Creates payment objects using                       │
│     PaymentFactory (Factory Pattern)                    │
│                                                          │
│  BookingLogger (Singleton)                             │
│  └─ Centralized audit trail for all operations         │
│                                                          │
│  DiscountStrategyFactory                               │
│  └─ Creates appropriate discount strategy              │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

---

## Design Pattern Summary Matrix

| Pattern | Type | Purpose | Location |
|---------|------|---------|----------|
| Factory | Creational | Create payment methods dynamically | `pattern/factory/` |
| Singleton | Creational | Centralized logging | `pattern/singleton/BookingLogger.java` |
| Facade | Structural | Simplify complex workflows | `pattern/facade/BookingFacade.java` |
| Strategy | Behavioral | Dynamic discount calculation | `pattern/strategy/DiscountStrategy.java` |

---

## How to Use the Patterns in Your Code

### Using Factory Pattern
```java
// In PaymentService
PaymentMethod method = PaymentFactory.getPayment(paymentType);
method.pay(amount);
```

### Using Singleton Pattern
```java
// Anywhere in the application
BookingLogger logger = BookingLogger.getInstance();
logger.logBookingCreated(id, userId, busId, seats);
```

### Using Facade Pattern
```java
// In BookingController
@Autowired
private BookingFacade bookingFacade;

BookingFacade.BookingResponse response = bookingFacade.completeBooking(
    userId, busId, seats, amount, paymentMethod
);
```

### Using Strategy Pattern
```java
// Calculate discounted price
double finalPrice = DiscountStrategyFactory.calculateDiscountedPrice(
    "STUDENT", basePrice, numberOfPassengers
);
```

---

## Compliance with Requirements

✅ **4 Design Patterns Implemented**
- 2 Creational (Factory, Singleton)
- 1 Structural (Facade)
- 1 Behavioral (Strategy)

✅ **Meets OOAD Mini-Project Requirement**
- Pattern per team member (4 patterns for 4-member team)
- Mix of Creational, Structural, and Behavioral patterns
- Real-world applicable to bus booking domain

---

## Extension Points

### Adding New Patterns
1. **Observer Pattern** - For real-time seat availability updates
2. **Command Pattern** - For transaction undo/redo functionality
3. **Adapter Pattern** - For integrating third-party payment gateways

---

## Conclusion
These 4 Design Patterns demonstrate:
- Professional software architecture
- SOLID principles adherence
- Scalability and maintainability
- Real-world design thinking
