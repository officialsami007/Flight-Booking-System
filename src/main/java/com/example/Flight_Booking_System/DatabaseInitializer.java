package com.example.Flight_Booking_System;

import com.example.Flight_Booking_System.model.Flight;
import com.example.Flight_Booking_System.model.User;
import com.example.Flight_Booking_System.repository.FlightRepository;
import com.example.Flight_Booking_System.repository.UserRepository;
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
            flightRepository.save(new Flight(LocalDate.of(2024, 12, 19), 3));
            flightRepository.save(new Flight(LocalDate.of(2024, 12, 20), 8));
            flightRepository.save(new Flight(LocalDate.of(2024, 12, 21), 3));
            flightRepository.save(new Flight(LocalDate.of(2025, 01, 21), 10));
            flightRepository.save(new Flight(LocalDate.of(2025, 01, 28), 07));
            flightRepository.save(new Flight(LocalDate.of(2025, 02, 15), 15));

            System.out.println("Flights initialized in the database.");

            // Initialize a sample user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin123");
            adminUser.setEmail("admin@example.com");
            userRepository.save(adminUser);
            System.out.println("Admin user initialized.");
        };
    }
}
