package com.example.Flight_Booking_System.service;

import com.example.Flight_Booking_System.model.Passenger;
import com.example.Flight_Booking_System.repository.PassengerRepository;
import com.example.Flight_Booking_System.dataStructure.CustomLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public CustomLinkedList<Passenger> getTicketsByUsername(String username) {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByUsername(username));
        bubbleSortPassengersByName(passengers); // Sort passengers by name
        return passengers;
    }

    private void bubbleSortPassengersByName(CustomLinkedList<Passenger> passengers) {
        int n = passengers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Passenger passenger1 = passengers.get(j);
                Passenger passenger2 = passengers.get(j + 1);

                if (passenger1.getName().compareToIgnoreCase(passenger2.getName()) > 0) {
                    // Swap the passengers
                    passengers.set(j, passenger2);
                    passengers.set(j + 1, passenger1);
                }
            }
        }
    }

}
