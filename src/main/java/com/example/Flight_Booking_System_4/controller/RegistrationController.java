package com.example.Flight_Booking_System_4.controller;

import com.example.Flight_Booking_System_4.model.User;
import com.example.Flight_Booking_System_4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "Username already exists!");
            return "register"; // Stay on the register page if an error occurs
        }
        userService.registerUser(user);
        return "redirect:/login?success"; // Redirect to login on successful registration
    }

    @GetMapping
    public String showRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Username already exists! Please choose a different username.");
        }
        return "register"; // Serve the register.html page
    }
}