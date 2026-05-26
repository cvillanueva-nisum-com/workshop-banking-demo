package com.demo.banking.account;

import com.demo.banking.account.model.Account;
import com.demo.banking.account.model.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * DEMO ESCENARIO 4: Este servicio tiene logica de negocio correcta
 * pero NO tiene tests unitarios. Usar Copilot /tests para generarlos.
 *
 * Funciones a testear:
 * - getBalance: cuenta activa vs inactiva vs no existe
 * - transfer: fondos suficientes, insuficientes, cuenta destino inactiva
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + accountNumber));
    }

    public BigDecimal getBalance(String accountNumber) {
        Account account = findByAccountNumber(accountNumber);
        if (!account.isActive()) {
            throw new IllegalStateException("La cuenta esta inactiva: " + accountNumber);
        }
        return account.getBalance();
    }

    @Transactional
    public void transfer(TransferRequest request) {
        Account origin = findByAccountNumber(request.fromAccountNumber());
        Account destination = findByAccountNumber(request.toAccountNumber());

        if (!origin.isActive()) {
            throw new IllegalStateException("Cuenta origen inactiva");
        }
        if (!destination.isActive()) {
            throw new IllegalStateException("Cuenta destino inactiva");
        }
        if (origin.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalStateException("Saldo insuficiente. Disponible: " + origin.getBalance());
        }

        origin.setBalance(origin.getBalance().subtract(request.amount()));
        destination.setBalance(destination.getBalance().add(request.amount()));

        accountRepository.save(origin);
        accountRepository.save(destination);
    }
}
