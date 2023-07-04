package org.financial.assistant.repositories;

import org.financial.assistant.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndCreatedAtBetweenAndIsAccounting(Long userId, LocalDateTime startDate, LocalDateTime endDate, boolean isAccounting);
}
