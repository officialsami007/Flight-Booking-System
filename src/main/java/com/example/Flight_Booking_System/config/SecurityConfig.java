package com.example.Flight_Booking_System.config;

import com.example.Flight_Booking_System.model.User;
import com.example.Flight_Booking_System.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/h2-console/**").permitAll() // Public endpoints
                        .requestMatchers("/myTickets", "/booking.html", "/editTicket.html", "/viewTicketStatus.html").authenticated() // Restricted endpoints
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/myTickets", true) // Redirect to MyTickets after login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirect to login after logout
                        .permitAll()
                );

        // Allow H2 Console to display properly
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            User user = userService.findByUsername(username); // Fetch user from DB
            if (user == null || !user.getPassword().equals(password)) {
                throw new AuthenticationException("Invalid username or password") {};
            }

            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList()); // Return username as principal
        };
    }
}