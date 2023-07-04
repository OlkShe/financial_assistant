package org.financial.assistant.controllers;

import org.financial.assistant.models.Transaction;
import org.financial.assistant.models.User;
import org.financial.assistant.security.UserDetails;
import org.financial.assistant.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final TransactionService transactionService;

    @Autowired
    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Dashboard - Financial assistant");
        model.addAttribute("bodyFragment", "dashboard/index");

        // Get total expenses and income for the past 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Transaction> transactions = transactionService.getTransactionsByUserAndDate(
                user.getId(),
                thirtyDaysAgo,
                LocalDateTime.now(),
                true
        );

        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        int countExpenses = 0;
        int countIncome = 0;

        // Map to store daily expenses and income
        Map<LocalDate, BigDecimal> dailyExpenses = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> dailyIncome = new LinkedHashMap<>();

        // Initialize the maps with all 30 days
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate date = currentDate.minusDays(i);
            dailyExpenses.put(date, BigDecimal.ZERO);
            dailyIncome.put(date, BigDecimal.ZERO);
        }

        for (Transaction transaction : transactions) {
            if (transaction.getCategory().getType().equals("expenses")) {
                totalExpenses = totalExpenses.add(transaction.getAmount());
                countExpenses++;

                // Update daily expenses
                LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
                BigDecimal currentExpenses = dailyExpenses.get(transactionDate);
                dailyExpenses.put(transactionDate, currentExpenses.add(transaction.getAmount()));
            } else {
                totalIncome = totalIncome.add(transaction.getAmount());
                countIncome++;

                // Update daily income
                LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
                BigDecimal currentIncome = dailyIncome.get(transactionDate);
                dailyIncome.put(transactionDate, currentIncome.add(transaction.getAmount()));
            }
        }

        System.out.println("-------------------------");
        System.out.println(dailyExpenses);
        System.out.println(dailyIncome);
        System.out.println("+++++++++++++++++++++++++");


        // Format totalExpenses and totalIncome
        String formattedTotalExpenses = Transaction.formatAmount(totalExpenses);
        String formattedTotalIncome = Transaction.formatAmount(totalIncome);

        model.addAttribute("totalExpenses", formattedTotalExpenses);
        model.addAttribute("totalIncome", formattedTotalIncome);
        model.addAttribute("countExpenses", countExpenses);
        model.addAttribute("countIncome", countIncome);
        model.addAttribute("transactions", transactions);
        model.addAttribute("dailyExpenses", dailyExpenses);
        model.addAttribute("dailyIncome", dailyIncome);

        return "wrapper";
    }
}
