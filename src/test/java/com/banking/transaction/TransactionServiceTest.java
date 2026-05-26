package com.demo.banking.transaction;

import com.demo.banking.transaction.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * DEMO ESCENARIO 2 - Tests que exponen el BUG del porcentaje.
 *
 * El test "debitPercentageShouldBeAccurate" falla con la implementacion
 * actual porque usa division entera. Usar Copilot /fix para corregirlo.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Resumen diario deberia calcular totales correctamente")
    void shouldCalculateDailySummaryTotals() {
        Transaction debit1 = buildTx(1L, new BigDecimal("500000"), "DEBIT");
        Transaction debit2 = buildTx(1L, new BigDecimal("300000"), "DEBIT");
        Transaction credit1 = buildTx(1L, new BigDecimal("1000000"), "CREDIT");

        when(transactionRepository.findByAccountIdAndCreatedAtBetween(eq(1L), any(), any()))
            .thenReturn(List.of(debit1, debit2, credit1));

        Map<String, Object> summary = transactionService.getDailySummary(1L, LocalDate.now());

        assertThat(summary.get("totalTransactions")).isEqualTo(3L);
        assertThat(summary.get("debitTotal")).isEqualTo(new BigDecimal("800000"));
        assertThat(summary.get("creditTotal")).isEqualTo(new BigDecimal("1000000"));
    }

    @Test
    @DisplayName("Porcentaje de debitos deberia ser preciso con decimales")
    void debitPercentageShouldBeAccurate() {
        // 3 debitos de 7 total = 42.86%, NO 42 (division entera)
        List<Transaction> txs = List.of(
            buildTx(1L, new BigDecimal("100"), "DEBIT"),
            buildTx(1L, new BigDecimal("100"), "DEBIT"),
            buildTx(1L, new BigDecimal("100"), "DEBIT"),
            buildTx(1L, new BigDecimal("100"), "CREDIT"),
            buildTx(1L, new BigDecimal("100"), "CREDIT"),
            buildTx(1L, new BigDecimal("100"), "CREDIT"),
            buildTx(1L, new BigDecimal("100"), "CREDIT")
        );

        when(transactionRepository.findByAccountIdAndCreatedAtBetween(eq(1L), any(), any()))
            .thenReturn(txs);

        Map<String, Object> summary = transactionService.getDailySummary(1L, LocalDate.now());

        // Este test FALLA con la implementacion actual (retorna 42 en vez de 42.86)
        // EJERCICIO: Usar Copilot /fix para detectar y corregir el bug
        Object pct = summary.get("debitPercentage");
        assertThat(new BigDecimal(pct.toString()))
            .isGreaterThan(new BigDecimal("42.80"))
            .isLessThan(new BigDecimal("42.90"));
    }

    private Transaction buildTx(Long accountId, BigDecimal amount, String type) {
        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setAmount(amount);
        tx.setTransactionType(type);
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }
}
