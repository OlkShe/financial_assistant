package org.financial.assistant.controllers;

import org.financial.assistant.models.User;
import org.financial.assistant.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
public class UserDetailsController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(Model model) {
        User currentUser = userDetailsService.getCurrentUser();

        model.addAttribute("user", currentUser);
        model.addAttribute("pageTitle", "Settings - Financial assistant");
        model.addAttribute("bodyFragment", "user/edit");

        return "wrapper";
    }

    @PatchMapping
    public String updateSettings(@RequestParam("login") String login,
                                 @RequestParam("email") String email,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        User currentUser = userDetailsService.getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Invalid old password");
            return "redirect:/settings";
        }

        currentUser.setUsername(login);
        currentUser.setEmail(email);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);

        if (!newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match");
                return "redirect:/settings";
            }

            String encodedPassword = passwordEncoder.encode(newPassword);
            currentUser.setPassword(encodedPassword);
        }

        userDetailsService.saveUser(currentUser);

        redirectAttributes.addFlashAttribute("success", "Settings updated successfully");
        return "redirect:/settings";
    }
}
