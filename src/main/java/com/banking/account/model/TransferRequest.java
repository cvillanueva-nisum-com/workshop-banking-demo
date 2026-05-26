package com.demo.banking.account.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank String fromAccountNumber,
    @NotBlank String toAccountNumber,
    @NotNull @DecimalMin("1.00") BigDecimal amount,
    String description
) {}
