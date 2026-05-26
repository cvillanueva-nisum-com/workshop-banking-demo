package com.demo.banking.loan;

import com.demo.banking.loan.model.LoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests existentes para LoanValidator.
 * Solo cubren el happy path — dejan muchos edge cases sin testear.
 * EJERCICIO: Usar Copilot /tests para completar la cobertura.
 */
class LoanValidatorTest {

    private LoanValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoanValidator();
    }

    @Test
    @DisplayName("Credito estandar deberia aprobarse")
    void shouldApproveStandardLoan() {
        LoanRequest request = new LoanRequest(1L, new BigDecimal("10000000"), 60, "Capital de trabajo");
        LoanValidator.ValidationResult result = validator.validate(request, 0.30, 720);
        assertThat(result.approved()).isTrue();
    }

    @Test
    @DisplayName("Score insuficiente deberia rechazarse")
    void shouldRejectLowCreditScore() {
        LoanRequest request = new LoanRequest(1L, new BigDecimal("5000000"), 36, "Test");
        LoanValidator.ValidationResult result = validator.validate(request, 0.30, 600);
        assertThat(result.approved()).isFalse();
        assertThat(result.message()).contains("650");
    }

    @Test
    @DisplayName("DTI alto deberia rechazarse")
    void shouldRejectHighDti() {
        LoanRequest request = new LoanRequest(1L, new BigDecimal("5000000"), 36, "Test");
        LoanValidator.ValidationResult result = validator.validate(request, 0.50, 750);
        assertThat(result.approved()).isFalse();
        assertThat(result.message()).contains("40%");
    }

    // --- AREA PARA COMPLETAR CON COPILOT /tests ---
    // Casos faltantes:
    // - request nulo
    // - monto nulo
    // - monto negativo
    // - plazo fuera de rango (5 meses, 361 meses)
    // - monto > 50M con score < 800
    // - monto > 50M con score >= 800
    // - plazo > 120 con score < 750
    // - monto minimo exacto (100000)
    // - monto maximo exacto (500000000)
}
