package com.banking.transaction;

import com.banking.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndCreatedAtBetween(
        Long accountId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT AVG(t.amount) FROM Transaction t WHERE t.accountId = :accountId AND t.transactionType = 'DEBIT'")
    BigDecimal findAverageDebitByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.accountId = :accountId AND t.createdAt >= :from")
    Long countRecentTransactions(@Param("accountId") Long accountId, @Param("from") LocalDateTime from);
}
