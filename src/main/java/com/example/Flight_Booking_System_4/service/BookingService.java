package com.example.Flight_Booking_System_4.service;

import com.example.Flight_Booking_System_4.model.Flight;
import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.repository.FlightRepository;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
import com.example.Flight_Booking_System_4.dataStructure.CustomLinkedList;
import com.example.Flight_Booking_System_4.dataStructure.CustomHashMap;
import com.example.Flight_Booking_System_4.dataStructure.CustomQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    private final CustomHashMap<Integer, CustomQueue<Passenger>> waitingLists = new CustomHashMap<>();

    public CustomLinkedList<Flight> searchFlights(LocalDate startDate, LocalDate endDate) {
        CustomLinkedList<Flight> flights = new CustomLinkedList<>();
        flights.addAll(flightRepository.findByDepartureDateBetween(startDate, endDate));
        bubbleSortFlightsByDate(flights);
        return flights;
    }

    private void bubbleSortFlightsByDate(CustomLinkedList<Flight> flights) {
        int n = flights.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Flight flight1 = flights.get(j);
                Flight flight2 = flights.get(j + 1);

                if (flight1.getDepartureDate().isAfter(flight2.getDepartureDate())) {
                    // Swap the flights
                    flights.set(j, flight2);
                    flights.set(j + 1, flight1);
                }
            }
        }
    }

    public String bookTicket(Passenger passenger) {
        Optional<Flight> flightOpt = flightRepository.findById(passenger.getFlightNumber());
        if (flightOpt.isEmpty()) return "Flight not found!";

        Flight flight = flightOpt.get();
        CustomLinkedList<Passenger> existingPassengers = new CustomLinkedList<>();
        existingPassengers.addAll(passengerRepository.findByFlightNumber(passenger.getFlightNumber()));
        boolean isDuplicate = existingPassengers.toList().stream()
                .anyMatch(p -> p.getPassportNumber().equals(passenger.getPassportNumber()));

        if (isDuplicate) {
            return "This passport number is already in use for this flight. Please choose a different flight.";
        }

        if (flight.getBookedSeats() < flight.getTotalSeats()) {
            flight.setBookedSeats(flight.getBookedSeats() + 1);
            flightRepository.save(flight);
            passenger.setStatus("Confirmed");
            passengerRepository.save(passenger);
            return "Ticket confirmed for " + passenger.getName();
        } else {
            waitingLists.computeIfAbsent(passenger.getFlightNumber(), k -> new CustomQueue<>()).enqueue(passenger);
            passenger.setStatus("Waiting");
            passengerRepository.save(passenger);
            return "No available seats. Added to waiting list for flight " + passenger.getFlightNumber();
        }
    }

    public String cancelTicket(String passportNumber, int flightNumber) {
        Optional<Passenger> passengerOpt = passengerRepository.findByPassportNumberAndFlightNumber(passportNumber, flightNumber);
        if (passengerOpt.isEmpty()) return "Ticket not found for the provided flight number!";

        Passenger passenger = passengerOpt.get();
        passengerRepository.delete(passenger);

        Optional<Flight> flightOpt = flightRepository.findById(flightNumber);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            flight.setBookedSeats(flight.getBookedSeats() - 1);
            flightRepository.save(flight);
        }

        CustomQueue<Passenger> waitingList = waitingLists.get(flightNumber);
        if (waitingList != null && !waitingList.isEmpty()) {
            Passenger promotedPassenger = waitingList.dequeue();
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

    public CustomLinkedList<Passenger> getConfirmedTickets(int flightNumber) {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByFlightNumber(flightNumber));
        return passengers;
    }

    public CustomLinkedList<Passenger> getConfirmedPassengers() {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByStatus("Confirmed"));
        return passengers;
    }

    public CustomLinkedList<Passenger> getWaitingPassengers() {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByStatus("Waiting"));
        return passengers;
    }

    public CustomLinkedList<Passenger> getMyTickets(String username) {
        CustomLinkedList<Passenger> passengers = new CustomLinkedList<>();
        passengers.addAll(passengerRepository.findByUsername(username));
        return passengers;
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
}
