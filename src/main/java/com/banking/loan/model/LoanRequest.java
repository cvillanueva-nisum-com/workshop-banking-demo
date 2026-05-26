package com.demo.banking.loan.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LoanRequest(
    @NotNull Long accountId,
    @NotNull @DecimalMin("100000.00") BigDecimal amount,
    @NotNull @Min(6) @Max(360) Integer termMonths,
    String purpose
) {}
