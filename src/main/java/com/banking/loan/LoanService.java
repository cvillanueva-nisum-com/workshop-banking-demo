package com.banking.loan;

import com.banking.loan.model.Loan;
import com.banking.loan.model.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanValidator loanValidator;

    public List<Loan> findByAccountId(Long accountId) {
        return loanRepository.findByAccountId(accountId);
    }

    public List<Loan> findDefaulted() {
        return loanRepository.findByStatus("DEFAULTED");
    }

    @Transactional
    public Loan createLoan(LoanRequest request, double debtToIncomeRatio, int creditScore) {
        LoanValidator.ValidationResult result = loanValidator.validate(request, debtToIncomeRatio, creditScore);
        if (!result.approved()) {
            throw new IllegalArgumentException("Solicitud rechazada: " + result.message());
        }

        BigDecimal rate = calculateRate(creditScore, request.termMonths());

        Loan loan = new Loan();
        loan.setAccountId(request.accountId());
        loan.setAmount(request.amount());
        loan.setInterestRate(rate);
        loan.setTermMonths(request.termMonths());
        loan.setStatus("ACTIVE");
        loan.setCreatedAt(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    private BigDecimal calculateRate(int creditScore, int termMonths) {
        double base = 0.08;
        if (creditScore >= 800) base = 0.038;
        else if (creditScore >= 750) base = 0.05;
        else if (creditScore >= 700) base = 0.065;
        if (termMonths > 120) base += 0.005;
        return BigDecimal.valueOf(base);
    }
}
