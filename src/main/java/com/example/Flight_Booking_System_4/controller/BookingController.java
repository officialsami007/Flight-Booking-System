package com.example.Flight_Booking_System_4.controller;

import com.example.Flight_Booking_System_4.model.Flight;
import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.service.BookingService;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedList;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PassengerRepository passengerRepository;

    // Search flights by date range
    @GetMapping("/searchFlights")
    public LinkedList<Flight> searchFlights(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LinkedList<Flight> flights = new LinkedList<>(bookingService.searchFlights(start, end));
        if (flights.isEmpty()) {
            System.out.println("No flights found for the given date range.");
        }
        return flights;
    }

    // Book a ticket for the logged-in user
    @PostMapping("/book")
    public String bookTicket(@RequestBody Passenger passenger, Principal principal) {
        // Fetch the logged-in user's username
        String username = principal.getName();
        // Set the username to the Passenger object
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
    public LinkedList<Passenger> getMyTickets(Principal principal) {
        String username = principal.getName();
        LinkedList<Passenger> tickets = new LinkedList<>(passengerRepository.findByUsername(username));
        tickets.forEach(ticket -> System.out.println("Ticket: " + ticket.getPassportNumber())); // Debugging line
        return tickets;
    }

    // View ticket status by passport number
    @GetMapping("/status/{passportNumber}")
    public String viewTicketStatus(@PathVariable String passportNumber) {
        return bookingService.viewTicketStatus(passportNumber);
    }

    // Get confirmed passengers for a specific flight
    @GetMapping("/confirmed/{flightNumber}")
    public LinkedList<Passenger> getConfirmedTickets(@PathVariable int flightNumber) {
        return new LinkedList<>(bookingService.getConfirmedTickets(flightNumber));
    }

    // Get all confirmed passengers
    @GetMapping("/status/confirmed")
    public LinkedList<Passenger> getConfirmedPassengers() {
        return new LinkedList<>(passengerRepository.findByStatus("Confirmed"));
    }

    // Get all waiting passengers
    @GetMapping("/status/waiting")
    public LinkedList<Passenger> getWaitingPassengers() {
        return new LinkedList<>(passengerRepository.findByStatus("Waiting"));
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
}
