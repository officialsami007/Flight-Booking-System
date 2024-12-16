package com.example.Flight_Booking_System.repository;

import com.example.Flight_Booking_System.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.Iterable;

import java.time.LocalDate;

public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Iterable<Flight> findByDepartureDateBetween(LocalDate startDate, LocalDate endDate);
}
