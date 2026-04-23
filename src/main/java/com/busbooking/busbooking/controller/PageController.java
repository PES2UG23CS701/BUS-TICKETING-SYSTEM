package com.busbooking.busbooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String homePage() {
        return "forward:/search.html";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "forward:/search.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "forward:/register.html";
    }

    @GetMapping("/booking")
    public String bookingPage() {
        return "forward:/booking.html";
    }

    @GetMapping("/payment")
    public String paymentPage() {
        return "forward:/payment.html";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "forward:/dashboard.html";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "forward:/admin-dashboard.html";
    }

    @GetMapping("/operator-dashboard")
    public String operatorDashboard() {
        return "forward:/operator-dashboard.html";
    }
}
