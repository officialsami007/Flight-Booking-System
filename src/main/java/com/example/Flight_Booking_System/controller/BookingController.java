package com.example.Flight_Booking_System.controller;

import com.example.Flight_Booking_System.dataStructure.CustomLinkedList;
import com.example.Flight_Booking_System.model.Flight;
import com.example.Flight_Booking_System.model.Passenger;
import com.example.Flight_Booking_System.service.BookingService;
import com.example.Flight_Booking_System.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PassengerRepository passengerRepository;

    // Search flights by date range
    @GetMapping("/searchFlights")
    public List<Flight> searchFlights(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return bookingService.searchFlights(start, end).toList();
    }

    // Book a ticket for the logged-in user
    @PostMapping("/book")
    public String bookTicket(@RequestBody Passenger passenger, Principal principal) {
        String username = principal.getName();
        passenger.setUsername(username);
        return bookingService.bookTicket(passenger);
    }

    // Cancel a ticket
    @DeleteMapping("/cancel")
    public String cancelTicket(@RequestParam String passportNumber, @RequestParam int flightNumber) {
        return bookingService.cancelTicket(passportNumber, flightNumber);
    }

    // Get the ticket of the logged-in user
    @GetMapping("/myTickets")
    public List<Passenger> getMyTickets(Principal principal) {
        String username = principal.getName();
        CustomLinkedList<Passenger> myTickets = bookingService.getMyTickets(username);
        bubbleSortPassengersByName(myTickets);
        return myTickets.toList();
    }

    // View ticket status by passport number
    @GetMapping("/status/{passportNumber}")
    public String viewTicketStatus(@PathVariable String passportNumber) {
        return bookingService.viewTicketStatus(passportNumber);
    }

    // Get confirmed passengers for a specific flight
    @GetMapping("/confirmed/{flightNumber}")
    public List<Passenger> getConfirmedTickets(@PathVariable int flightNumber) {
        return bookingService.getConfirmedTickets(flightNumber).toList();
    }

    // Get all confirmed passengers
    @GetMapping("/status/confirmed")
    public List<Passenger> getConfirmedPassengers() {
        CustomLinkedList<Passenger> confirmedPassengers = bookingService.getConfirmedPassengers();
        bubbleSortPassengersByName(confirmedPassengers);
        return confirmedPassengers.toList();
    }
    // Get all waiting passengers
    @GetMapping("/status/waiting")
    public List<Passenger> getWaitingPassengers() {
        CustomLinkedList<Passenger> waitingPassengers = bookingService.getWaitingPassengers();
        bubbleSortPassengersByName(waitingPassengers);
        return waitingPassengers.toList();
    }

    // Edit passenger information by passport number
    @PutMapping("/edit/{passportNumber}")
    public String editPassenger(@PathVariable String passportNumber, @RequestBody Passenger updatedPassenger) {
        return bookingService.editPassenger(passportNumber, updatedPassenger);
    }

    // Search for a specific ticket by passport number
    @GetMapping("/search/{passportNumber}")
    public Passenger searchTicket(@PathVariable String passportNumber) {
        return bookingService.searchTicket(passportNumber);
    }

    private void bubbleSortPassengersByName(CustomLinkedList<Passenger> passengers) {
        int n = passengers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Passenger passenger1 = passengers.get(j);
                Passenger passenger2 = passengers.get(j + 1);

                if (passenger1.getName().compareToIgnoreCase(passenger2.getName()) > 0) {
                    // Swap passengers
                    passengers.set(j, passenger2);
                    passengers.set(j + 1, passenger1);
                }
            }
        }
    }
}
