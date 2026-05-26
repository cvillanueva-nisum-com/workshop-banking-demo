package com.demo.banking.loan;

import com.demo.banking.loan.model.LoanRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Valida solicitudes de credito contra reglas de negocio y perfil del solicitante. */
@Component
public class LoanValidator {

    public ValidationResult validate(LoanRequest request, double debtToIncomeRatio, int creditScore) {
        if (request != null) {
            if (request.amount() != null) {
                if (request.amount().compareTo(BigDecimal.ZERO) > 0) {
                    if (request.termMonths() != null) {
                        if (request.termMonths() >= 6 && request.termMonths() <= 360) {
                            if (creditScore >= 0 && creditScore <= 1000) {
                                if (creditScore >= 650) {
                                    if (debtToIncomeRatio >= 0 && debtToIncomeRatio <= 1) {
                                        if (debtToIncomeRatio <= 0.40) {
                                            if (request.amount().compareTo(new BigDecimal("100000")) >= 0) {
                                                if (request.amount().compareTo(new BigDecimal("500000000")) <= 0) {
                                                    if (request.termMonths() <= 120) {
                                                        if (request.amount().compareTo(new BigDecimal("50000000")) <= 0) {
                                                            return ValidationResult.approved("Credito aprobado");
                                                        } else {
                                                            if (creditScore >= 800) {
                                                                return ValidationResult.approved("Credito aprobado - monto alto requiere score >= 800");
                                                            } else {
                                                                return ValidationResult.rejected("Monto > 50M requiere score crediticio >= 800");
                                                            }
                                                        }
                                                    } else {
                                                        if (creditScore >= 750) {
                                                            return ValidationResult.approved("Credito aprobado - plazo extendido");
                                                        } else {
                                                            return ValidationResult.rejected("Plazo > 120 meses requiere score >= 750");
                                                        }
                                                    }
                                                } else {
                                                    return ValidationResult.rejected("Monto excede el limite maximo de 500 millones");
                                                }
                                            } else {
                                                return ValidationResult.rejected("Monto minimo de credito es $100.000");
                                            }
                                        } else {
                                            return ValidationResult.rejected("Relacion deuda/ingreso supera el 40% permitido. Actual: " + (debtToIncomeRatio * 100) + "%");
                                        }
                                    } else {
                                        return ValidationResult.rejected("Relacion deuda/ingreso invalida");
                                    }
                                } else {
                                    return ValidationResult.rejected("Score crediticio insuficiente: " + creditScore + ". Minimo requerido: 650");
                                }
                            } else {
                                return ValidationResult.rejected("Score crediticio fuera de rango valido (0-1000)");
                            }
                        } else {
                            return ValidationResult.rejected("Plazo invalido. Debe estar entre 6 y 360 meses");
                        }
                    } else {
                        return ValidationResult.rejected("Plazo de credito requerido");
                    }
                } else {
                    return ValidationResult.rejected("Monto debe ser mayor a cero");
                }
            } else {
                return ValidationResult.rejected("Monto de credito requerido");
            }
        } else {
            return ValidationResult.rejected("Solicitud de credito requerida");
        }
    }

    public record ValidationResult(boolean approved, String message) {
        public static ValidationResult approved(String message) {
            return new ValidationResult(true, message);
        }
        public static ValidationResult rejected(String reason) {
            return new ValidationResult(false, reason);
        }
    }
}
