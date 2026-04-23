package com.busbooking.busbooking.pattern.strategy;

/**
 * Strategy Pattern Implementation
 * 
 * Purpose: Defines a family of algorithms (discount strategies) and encapsulates each one.
 * Allows selecting discount algorithm at runtime based on passenger category.
 * 
 * Benefits:
 * - Easy to add new discount strategies without modifying existing code
 * - Runtime selection of algorithm
 * - Follows Open/Closed Principle
 * - Cleaner code compared to if-else chains
 */

// Strategy Interface
public interface DiscountStrategy {
    double applyDiscount(double originalPrice);
    String getStrategyName();
}

// Concrete Strategy 1: Student Discount
class StudentDiscountStrategy implements DiscountStrategy {
    
    private static final double DISCOUNT_PERCENTAGE = 15.0; // 15% discount
    
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * DISCOUNT_PERCENTAGE / 100);
    }
    
    @Override
    public String getStrategyName() {
        return "Student Discount (15%)";
    }
}

// Concrete Strategy 2: Senior Citizen Discount
class SeniorCitizenDiscountStrategy implements DiscountStrategy {
    
    private static final double DISCOUNT_PERCENTAGE = 20.0; // 20% discount
    
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * DISCOUNT_PERCENTAGE / 100);
    }
    
    @Override
    public String getStrategyName() {
        return "Senior Citizen Discount (20%)";
    }
}

// Concrete Strategy 3: Bulk Booking Discount
class BulkBookingDiscountStrategy implements DiscountStrategy {
    
    private int numberOfPassengers;
    
    public BulkBookingDiscountStrategy(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
    
    @Override
    public double applyDiscount(double originalPrice) {
        double discountPercentage = 0;
        
        if (numberOfPassengers >= 10) {
            discountPercentage = 25.0; // 25% for 10+ passengers
        } else if (numberOfPassengers >= 5) {
            discountPercentage = 15.0; // 15% for 5-9 passengers
        }
        
        return originalPrice - (originalPrice * discountPercentage / 100);
    }
    
    @Override
    public String getStrategyName() {
        return "Bulk Booking Discount (" + numberOfPassengers + " passengers)";
    }
}

// Concrete Strategy 4: No Discount (Default)
class NoDiscountStrategy implements DiscountStrategy {
    
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice;
    }
    
    @Override
    public String getStrategyName() {
        return "No Discount";
    }
}

// Context Class that uses the strategy
class PricingCalculator {
    
    private DiscountStrategy discountStrategy;
    
    // Constructor accepts any discount strategy
    public PricingCalculator(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }
    
    // Change strategy at runtime
    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }
    
    // Calculate final price using the strategy
    public double calculateFinalPrice(double basePrice) {
        return discountStrategy.applyDiscount(basePrice);
    }
    
    public String getAppliedDiscount() {
        return discountStrategy.getStrategyName();
    }
    
    // Display pricing details
    public void displayPricing(double basePrice) {
        double finalPrice = calculateFinalPrice(basePrice);
        double discount = basePrice - finalPrice;
        
        System.out.println("=== Pricing Breakdown ===");
        System.out.println("Base Price: ₹" + basePrice);
        System.out.println("Discount Applied: " + getAppliedDiscount());
        System.out.println("Discount Amount: ₹" + String.format("%.2f", discount));
        System.out.println("Final Price: ₹" + String.format("%.2f", finalPrice));
        System.out.println("========================");
    }
}
