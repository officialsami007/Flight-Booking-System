package com.example.Flight_Booking_System_4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "booking";
    }

    @GetMapping("/booking.html")
    public String bookingPage() {
        return "booking";
    }

    @GetMapping("/editTicket.html")
    public String editTicketPage() {
        return "editTicket";
    }

    @GetMapping("/viewTicketStatus.html")
    public String viewTicketStatusPage() {
        return "viewTicketStatus";
    }

}
