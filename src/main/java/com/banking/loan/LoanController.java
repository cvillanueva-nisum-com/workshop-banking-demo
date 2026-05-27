package com.banking.loan;

import com.banking.loan.model.Loan;
import com.banking.loan.model.LoanRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/account/{accountId}")
    public List<Loan> getByAccount(@PathVariable Long accountId) {
        return loanService.findByAccountId(accountId);
    }

    @GetMapping("/defaulted")
    public List<Loan> getDefaulted() {
        return loanService.findDefaulted();
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(
            @Valid @RequestBody LoanRequest request,
            @RequestParam(defaultValue = "0.35") double dti,
            @RequestParam(defaultValue = "720") int creditScore) {
        Loan loan = loanService.createLoan(request, dti, creditScore);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateOnly(
            @Valid @RequestBody LoanRequest request,
            @RequestParam(defaultValue = "0.35") double dti,
            @RequestParam(defaultValue = "720") int creditScore) {
        LoanValidator.ValidationResult result = new LoanValidator().validate(request, dti, creditScore);
        return ResponseEntity.ok(Map.of(
            "approved", result.approved(),
            "message", result.message()
        ));
    }
}
