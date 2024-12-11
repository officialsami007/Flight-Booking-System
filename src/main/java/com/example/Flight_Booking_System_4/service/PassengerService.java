package com.example.Flight_Booking_System_4.service;

import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public List<Passenger> getTicketsByUsername(String username) {
        return passengerRepository.findByUsername(username);
    }
}
