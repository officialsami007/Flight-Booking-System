package com.example.Flight_Booking_System_4.controller;

import com.example.Flight_Booking_System_4.model.Flight;
import com.example.Flight_Booking_System_4.model.Passenger;
import com.example.Flight_Booking_System_4.service.BookingService;
import com.example.Flight_Booking_System_4.repository.PassengerRepository;
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
        return bookingService.searchFlights(start, end).toList(); // Convert to List
    }

    // Book a ticket for the logged-in user
    @PostMapping("/book")
    public String bookTicket(@RequestBody Passenger passenger, Principal principal) {
        String username = principal.getName(); // Fetch the logged-in user's username
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
        return bookingService.getMyTickets(username).toList(); // Convert to List
    }

    // View ticket status by passport number
    @GetMapping("/status/{passportNumber}")
    public String viewTicketStatus(@PathVariable String passportNumber) {
        return bookingService.viewTicketStatus(passportNumber);
    }

    // Get confirmed passengers for a specific flight
    @GetMapping("/confirmed/{flightNumber}")
    public List<Passenger> getConfirmedTickets(@PathVariable int flightNumber) {
        return bookingService.getConfirmedTickets(flightNumber).toList(); // Convert to List
    }

    // Get all confirmed passengers
    @GetMapping("/status/confirmed")
    public List<Passenger> getConfirmedPassengers() {
        return bookingService.getConfirmedPassengers().toList(); // Convert to List
    }

    // Get all waiting passengers
    @GetMapping("/status/waiting")
    public List<Passenger> getWaitingPassengers() {
        return bookingService.getWaitingPassengers().toList(); // Convert to List
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
