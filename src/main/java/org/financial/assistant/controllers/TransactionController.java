package org.financial.assistant.controllers;

import org.financial.assistant.models.Category;
import org.financial.assistant.models.Transaction;
import org.financial.assistant.models.User;
import org.financial.assistant.security.UserDetails;
import org.financial.assistant.services.CategoryService;
import org.financial.assistant.services.TransactionPdfExporter;
import org.financial.assistant.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionPdfExporter transactionPdfExporter;
    private final CategoryService categoryService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 TransactionPdfExporter transactionPdfExporter,
                                 CategoryService categoryService) {
        this.transactionService = transactionService;
        this.transactionPdfExporter = transactionPdfExporter;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userDetails.getUser();

        List<Transaction> transactions = transactionService.getAllTransactionsForUser(user);
        List<Category> categories = categoryService.getAllCategoriesForUser(user);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Transactions - Financial assistant");
        model.addAttribute("bodyFragment", "transaction/index");
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);

        return "wrapper";
    }

    @PostMapping
    public String createTransaction(
            @ModelAttribute("transaction") Transaction transaction,
            @RequestParam("name") String transactionName,
            @RequestParam("amount") BigDecimal transactionAmount,
            @RequestParam("category") Category transactionCategory,
            @RequestParam("isAccounting") boolean transactionIsAccounting,
            @RequestParam("description") String transactionDescription,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userDetails.getUser();

        transaction.setName(transactionName);
        transaction.setAmount(transactionAmount);
        transaction.setCategory(transactionCategory);
        transaction.setIsAccounting(transactionIsAccounting);
        transaction.setDescription(transactionDescription);
        transaction.setUser(user);

        transactionService.createTransaction(transaction);

        return "redirect:/transaction";
    }

    @GetMapping("/{id}")
    public String getTransactionDetails(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        User user = userDetails.getUser();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Transaction " + id + " - Financial assistant");
        model.addAttribute("bodyFragment", "transaction/show");
        model.addAttribute("transaction", transaction);

        return "wrapper";
    }

    @GetMapping("/{id}/edit")
    public String editTransaction(@PathVariable("id") Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        User user = userDetails.getUser();
        List<Category> categories = categoryService.getAllCategoriesForUser(user);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Transaction " + id + " edit - Financial assistant");
        model.addAttribute("bodyFragment", "transaction/edit");
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);

        return "wrapper";
    }

    @PostMapping("/{id}/edit")
    public String updateTransaction(@PathVariable("id") Long id,
                                    @ModelAttribute Transaction transaction) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        existingTransaction.setName(transaction.getName());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setIsAccounting(transaction.getIsAccounting());
        existingTransaction.setDescription(transaction.getDescription());

        transactionService.updateTransaction(existingTransaction);

        return "redirect:/transaction";
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable("id") Long id) {
        transactionService.deleteTransaction(id);

        return "redirect:/transaction";
    }

    @GetMapping("/export/pdf")
    public void exportTransactionsToPDF(HttpServletResponse response) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        transactionPdfExporter.exportTransactionsToPdf(transactions, response);
    }

    @GetMapping("/export/pdf/{id}")
    public void getTransactionExportToPDF(@PathVariable("id") Long id,
                                          HttpServletResponse response) {
        Transaction transaction = transactionService.getTransactionById(id);
        transactionPdfExporter.exportTransactionToPdf(transaction, response);
    }
}
