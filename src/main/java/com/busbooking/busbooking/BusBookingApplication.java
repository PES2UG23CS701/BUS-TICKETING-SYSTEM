package com.busbooking.busbooking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.repository.BusRepository;

@SpringBootApplication
public class BusBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusBookingApplication.class, args);
    }

    @Bean
    public CommandLineRunner addTestBuses(BusRepository busRepo) {
        return args -> {
            // Only initialize test data if database is empty
            if (busRepo.count() == 0) {
                // Admin buses (operatorId = 0)
                busRepo.save(new Bus("Bangalore", "Mysore", 40, 40, 500, "NB01", "06:00 AM"));
                busRepo.save(new Bus("Bangalore", "Mysore", 40, 40, 600, "NB02", "09:00 AM"));
                busRepo.save(new Bus("Bangalore", "Mysore", 40, 40, 550, "NB03", "12:00 PM"));
                
                busRepo.save(new Bus("Bangalore", "Chennai", 50, 50, 800, "BC01", "10:00 AM"));
                busRepo.save(new Bus("Bangalore", "Chennai", 50, 50, 850, "BC02", "02:00 PM"));
                
                busRepo.save(new Bus("Bangalore", "Hyderabad", 45, 45, 700, "BH01", "11:00 AM"));
                busRepo.save(new Bus("Bangalore", "Hyderabad", 45, 45, 750, "BH02", "07:00 PM"));
                
                busRepo.save(new Bus("Bangalore", "Pune", 50, 50, 900, "BP01", "08:00 AM"));
                busRepo.save(new Bus("Bangalore", "Pune", 50, 50, 950, "BP02", "03:00 PM"));
            }
        };
    }
}