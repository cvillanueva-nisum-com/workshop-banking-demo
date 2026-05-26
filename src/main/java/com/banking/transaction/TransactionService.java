package com.demo.banking.transaction;

import com.demo.banking.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Logica de negocio para consulta y reporte de transacciones. */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Map<String, Object> getDailySummary(Long accountId, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59);

        List<Transaction> txs = transactionRepository.findByAccountIdAndCreatedAtBetween(accountId, from, to);

        long debitCount = txs.stream().filter(t -> "DEBIT".equals(t.getTransactionType())).count();
        long creditCount = txs.stream().filter(t -> "CREDIT".equals(t.getTransactionType())).count();
        long total = txs.size();

        BigDecimal debitTotal = txs.stream()
            .filter(t -> "DEBIT".equals(t.getTransactionType()))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal creditTotal = txs.stream()
            .filter(t -> "CREDIT".equals(t.getTransactionType()))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long debitPercentage = total > 0 ? (debitCount * 100) / total : 0;

        return Map.of(
            "date", date,
            "totalTransactions", total,
            "debitCount", debitCount,
            "creditCount", creditCount,
            "debitTotal", debitTotal,
            "creditTotal", creditTotal,
            "debitPercentage", debitPercentage,
            "netFlow", creditTotal.subtract(debitTotal)
        );
    }

    public BigDecimal calculateMonthlyVolume(Long accountId, int year, int month) {
        LocalDateTime from = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime to = from.plusMonths(1).minusNanos(1);

        return transactionRepository
            .findByAccountIdAndCreatedAtBetween(accountId, from, to)
            .stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<LocalDate, List<Transaction>> groupByDay(Long accountId) {
        return transactionRepository.findByAccountId(accountId)
            .stream()
            .collect(Collectors.groupingBy(t -> t.getCreatedAt().toLocalDate()));
    }
}
