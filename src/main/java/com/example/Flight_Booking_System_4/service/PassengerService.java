package com.example.Flight_Booking_System_4.service;

import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
import com.example.Flight_Booking_System_4.dataStructure.CustomLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public CustomLinkedList<Passenger> getTicketsByUsername(String username) {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByUsername(username));
        return passengers;
    }
}
