package com.example.Flight_Booking_System_4.service;

import com.example.Flight_Booking_System_4.model.Flight;
import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.repository.FlightRepository;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    private final Map<Integer, Queue<Passenger>> waitingLists = new HashMap<>();

    public LinkedList<Flight> searchFlights(LocalDate startDate, LocalDate endDate) {
        return new LinkedList<>(flightRepository.findByDepartureDateBetween(startDate, endDate));
    }

    public String bookTicket(Passenger passenger) {
        Optional<Flight> flightOpt = flightRepository.findById(passenger.getFlightNumber());
        if (flightOpt.isEmpty()) return "Flight not found!";

        LinkedList<Passenger> existingPassengers = new LinkedList<>(passengerRepository.findByFlightNumber(passenger.getFlightNumber()));
        boolean isDuplicate = existingPassengers.stream()
                .anyMatch(p -> p.getPassportNumber().equals(passenger.getPassportNumber()));

        if (isDuplicate) {
            return "This passport number is already in use for this flight. Please choose a different flight.";
        }

        Flight flight = flightOpt.get();
        if (flight.getBookedSeats() < flight.getTotalSeats()) {
            flight.setBookedSeats(flight.getBookedSeats() + 1);
            flightRepository.save(flight);
            passenger.setStatus("Confirmed");
            System.out.println("Saving passenger with username: " + passenger.getUsername()); // Debug
            passengerRepository.save(passenger);
            return "Ticket confirmed for " + passenger.getName();
        } else {
            waitingLists.computeIfAbsent(passenger.getFlightNumber(), k -> new LinkedList<>()).add(passenger);
            passenger.setStatus("Waiting");
            System.out.println("Adding to waiting list with username: " + passenger.getUsername()); // Debug
            passengerRepository.save(passenger);
            return "No available seats. Added to waiting list for flight " + passenger.getFlightNumber();
        }
    }



    public String cancelTicket(String passportNumber, int flightNumber) {
        // Find the passenger by both passport number and flight number
        Optional<Passenger> passengerOpt = passengerRepository.findByPassportNumberAndFlightNumber(passportNumber, flightNumber);
        if (passengerOpt.isEmpty()) return "Ticket not found for the provided flight number!";

        Passenger passenger = passengerOpt.get();
        passengerRepository.delete(passenger);

        // Update flight seat count
        Optional<Flight> flightOpt = flightRepository.findById(flightNumber);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            flight.setBookedSeats(flight.getBookedSeats() - 1);
            flightRepository.save(flight);
        }

        // Promote the first passenger in the waiting list
        Queue<Passenger> waitingList = waitingLists.get(flightNumber);
        if (waitingList != null && !waitingList.isEmpty()) {
            Passenger promotedPassenger = waitingList.poll();
            promotedPassenger.setStatus("Confirmed");
            passengerRepository.save(promotedPassenger);

            Optional<Flight> promotedFlightOpt = flightRepository.findById(promotedPassenger.getFlightNumber());
            promotedFlightOpt.ifPresent(promotedFlight -> {
                promotedFlight.setBookedSeats(promotedFlight.getBookedSeats() + 1);
                flightRepository.save(promotedFlight);
            });
        }

        return "Ticket cancelled for " + passenger.getName();
    }


    public Passenger searchTicket(String passportNumber) {
        return passengerRepository.findByPassportNumber(passportNumber).orElse(null);
    }

    public LinkedList<Passenger> getConfirmedTickets(int flightNumber) {
        return new LinkedList<>(passengerRepository.findByFlightNumber(flightNumber));
    }

    public String editPassenger(String passportNumber, Passenger updatedPassenger) {
        Optional<Passenger> passengerOpt = passengerRepository.findByPassportNumber(passportNumber);
        if (passengerOpt.isEmpty()) return "Passenger not found!";

        Passenger passenger = passengerOpt.get();
        passenger.setName(updatedPassenger.getName());
        passenger.setPassportNumber(updatedPassenger.getPassportNumber());
        passengerRepository.save(passenger);
        return "Passenger information updated successfully.";
    }

    public String viewTicketStatus(String passportNumber) {
        Optional<Passenger> passengerOpt = passengerRepository.findByPassportNumber(passportNumber);
        if (passengerOpt.isEmpty()) return "Ticket not found!";
        return "Ticket Status: " + passengerOpt.get().getStatus();
    }

    public LinkedList<Passenger> getMyTickets(String username) {
        LinkedList<Passenger> passengers = new LinkedList<>(passengerRepository.findByUsername(username));
        System.out.println("Fetched passengers for username: " + username + " -> " + passengers);
        return passengers;
    }

}
