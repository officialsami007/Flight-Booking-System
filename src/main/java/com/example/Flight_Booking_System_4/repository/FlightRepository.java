package com.example.Flight_Booking_System_4.repository;

import com.example.Flight_Booking_System_4.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.LinkedList;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    LinkedList<Flight> findByDepartureDateBetween(LocalDate startDate, LocalDate endDate);
}

