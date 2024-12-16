package com.example.Flight_Booking_System.controller;

import com.example.Flight_Booking_System.model.Passenger;
import com.example.Flight_Booking_System.service.PassengerService;
import com.example.Flight_Booking_System.dataStructure.CustomLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyTicketsController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping("/myTickets")
    public String viewMyTickets(Authentication authentication, Model model) {
        // Get the logged-in user's username
        String username = authentication.getName();

        // Fetch tickets for the user using CustomLinkedList
        CustomLinkedList<Passenger> tickets = passengerService.getTicketsByUsername(username);

        // Add tickets to the model
        model.addAttribute("tickets", tickets);

        return "myTickets";
    }
}
