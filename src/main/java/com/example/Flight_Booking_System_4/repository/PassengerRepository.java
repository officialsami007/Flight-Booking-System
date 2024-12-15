package com.example.Flight_Booking_System_4.repository;

import com.example.Flight_Booking_System_4.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.Iterable;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByPassportNumber(String passportNumber);

    Optional<Passenger> findByPassportNumberAndFlightNumber(String passportNumber, int flightNumber);

    Iterable<Passenger> findByFlightNumber(int flightNumber);

    Iterable<Passenger> findByStatus(String status);

    Iterable<Passenger> findByUsername(String username);
}
