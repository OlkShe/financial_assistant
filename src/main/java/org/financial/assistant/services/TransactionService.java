package org.financial.assistant.services;

import org.financial.assistant.models.Transaction;
import org.financial.assistant.models.User;
import org.financial.assistant.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getAllTransactionsForUser(User user) {
        return transactionRepository.findByUserId(user.getId());
    }

    public List<Transaction> getTransactionsByUserAndDate(Long userId,
                                                          LocalDateTime startTimestamp,
                                                          LocalDateTime endTimestamp,
                                                          boolean isAccounting) {
        return transactionRepository.findByUserIdAndCreatedAtBetweenAndIsAccounting(userId,
                startTimestamp,
                endTimestamp,
                isAccounting);
    }

    @Transactional
    public void createTransaction(Transaction transaction) {
        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
