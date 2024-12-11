package com.example.Flight_Booking_System_4;

import com.example.Flight_Booking_System_4.model.Flight;
import com.example.Flight_Booking_System_4.model.User;
import com.example.Flight_Booking_System_4.repository.FlightRepository;
import com.example.Flight_Booking_System_4.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(FlightRepository flightRepository, UserRepository userRepository) {
        return args -> {
            // Initialize flights
            flightRepository.save(new Flight(LocalDate.of(2024, 11, 19), 3));
            flightRepository.save(new Flight(LocalDate.of(2024, 11, 20), 3));
            flightRepository.save(new Flight(LocalDate.of(2024, 11, 21), 3));
            System.out.println("Flights initialized in the database.");

            // Initialize a sample user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin123"); // Storing raw password
            adminUser.setEmail("admin@example.com");
            userRepository.save(adminUser);
            System.out.println("Admin user initialized.");
        };
    }
}
