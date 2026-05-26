package com.demo.banking.transaction;

import com.demo.banking.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/account/{accountId}")
    public List<Transaction> getByAccount(@PathVariable Long accountId) {
        return transactionService.findByAccountId(accountId);
    }

    @GetMapping("/account/{accountId}/summary")
    public Map<String, Object> getDailySummary(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return transactionService.getDailySummary(accountId, date);
    }

    @GetMapping("/account/{accountId}/monthly-volume")
    public Map<String, BigDecimal> getMonthlyVolume(
            @PathVariable Long accountId,
            @RequestParam int year,
            @RequestParam int month) {
        return Map.of("volume", transactionService.calculateMonthlyVolume(accountId, year, month));
    }
}
