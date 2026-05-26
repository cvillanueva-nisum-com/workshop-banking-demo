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

/**
 * DEMO ESCENARIO 2 - DETECCION DE BUGS con Copilot /fix
 *
 * Este servicio tiene 2 bugs sutiles que en produccion generan
 * reportes financieros incorrectos. Han pasado por code review
 * sin ser detectados porque la logica "parece" correcta.
 *
 * EJERCICIO: Seleccionar el metodo getDailySummary() y/o
 * calculateMonthlyVolume() y usar Copilot Chat:
 * "/fix Revisar este metodo en busca de bugs sutiles.
 *  Analizar edge cases y precision numerica."
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    /**
     * BUG #1: El rango de fechas excluye las transacciones del ultimo dia del mes.
     * LocalDate.now() da la fecha actual, pero atEndOfDay() no existe en LocalDate.
     * El desarrollador uso atTime(23, 59, 59) pensando que era el fin del dia,
     * pero las transacciones con nanosegundos en 23:59:59.XXX quedan fuera.
     * Deberia usar atTime(LocalTime.MAX) o plusDays(1).atStartOfDay().
     *
     * Ademas, el calculo del porcentaje de debitos usa division entera implicita
     * cuando debitCount y total son int, perdiendo decimales.
     */
    public Map<String, Object> getDailySummary(Long accountId, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59); // BUG: pierde transacciones en 23:59:59.XXX

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

        // BUG #2: division entera — si debitCount=3 y total=7, resultado es 0 en vez de 42.85%
        // Deberia ser: BigDecimal.valueOf(debitCount).multiply(BigDecimal.valueOf(100))
        //              .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
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

    /**
     * Calcula el volumen mensual de transacciones.
     * Correcto en logica pero sin manejo de cuenta nula.
     */
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
