package com.busbooking.busbooking.pattern.strategy;

/**
 * Factory for selecting appropriate discount strategies
 * Utility class for creating discount strategies based on passenger type
 */
public class DiscountStrategyFactory {
    
    public static DiscountStrategy getDiscountStrategy(String passengerType, int numberOfPassengers) {
        
        if (passengerType.equalsIgnoreCase("STUDENT")) {
            return new StudentDiscountStrategy();
        } 
        else if (passengerType.equalsIgnoreCase("SENIOR_CITIZEN")) {
            return new SeniorCitizenDiscountStrategy();
        }
        else if (passengerType.equalsIgnoreCase("BULK")) {
            return new BulkBookingDiscountStrategy(numberOfPassengers);
        }
        else {
            return new NoDiscountStrategy();
        }
    }
    
    // Utility method for booking system
    public static double calculateDiscountedPrice(String passengerType, double basePrice, int numberOfPassengers) {
        DiscountStrategy strategy = getDiscountStrategy(passengerType, numberOfPassengers);
        PricingCalculator calculator = new PricingCalculator(strategy);
        return calculator.calculateFinalPrice(basePrice);
    }
    
    // Get discount details
    public static String getDiscountInfo(String passengerType, int numberOfPassengers) {
        DiscountStrategy strategy = getDiscountStrategy(passengerType, numberOfPassengers);
        return strategy.getStrategyName();
    }
}
