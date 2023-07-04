package org.financial.assistant.controllers;

import org.financial.assistant.security.UserDetails;
import org.financial.assistant.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final AdminService adminService;

    @Autowired
    public HomeController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String show(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("pageTitle", "Dashboard - Financial assistant");
        model.addAttribute("bodyFragment", "dashboard/dashboard");

        return "wrapper";
    }


    @GetMapping("/admin")
    public String adminPage() {
        adminService.doAdminStuff();
        return "admin";
    }
}
