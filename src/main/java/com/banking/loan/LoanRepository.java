package com.demo.banking.loan;

import com.demo.banking.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByAccountId(Long accountId);
    List<Loan> findByStatus(String status);
}
