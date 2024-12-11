package com.example.Flight_Booking_System_4.repository;

import com.example.Flight_Booking_System_4.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Optional<Passenger> findByPassportNumber(String passportNumber);
    List<Passenger> findByFlightNumber(int flightNumber);
    Optional<Passenger> findByPassportNumberAndFlightNumber(String passportNumber, int flightNumber);
    List<Passenger> findByStatus(String status);
    List<Passenger> findByUsername(String username);


}

