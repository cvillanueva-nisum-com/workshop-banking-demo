-- Demo seed data for GitHub Copilot Workshop

INSERT INTO account (id, account_number, owner_name, balance, account_type, active) VALUES
(1, 'BANK-001-2024', 'Empresa Constructora SA', 15000000.00, 'CORRIENTE', true),
(2, 'BANK-002-2024', 'Inversiones del Sur Ltda', 8500000.00, 'VISTA', true),
(3, 'BANK-003-2024', 'Comercial Norte SpA', 2300000.00, 'CORRIENTE', true),
(4, 'BANK-004-2024', 'Holding Pacific Group', 45000000.00, 'CORRIENTE', true),
(5, 'BANK-005-2024', 'Cuenta Bloqueada Test', 0.00, 'VISTA', false);

INSERT INTO loan (id, account_id, amount, interest_rate, term_months, status, created_at) VALUES
(1, 1, 50000000.00, 0.045, 60, 'ACTIVE', '2024-01-15 09:00:00'),
(2, 2, 12000000.00, 0.068, 36, 'ACTIVE', '2024-03-01 10:30:00'),
(3, 3, 5000000.00, 0.089, 24, 'DEFAULTED', '2023-06-20 14:00:00'),
(4, 4, 200000000.00, 0.038, 120, 'ACTIVE', '2024-02-10 08:00:00'),
(5, 1, 8000000.00, 0.072, 12, 'PAID', '2023-01-01 00:00:00');

INSERT INTO transaction (id, account_id, amount, transaction_type, description, created_at) VALUES
(1, 1, 500000.00, 'DEBIT', 'Pago proveedor', '2024-05-01 09:15:00'),
(2, 1, 1200000.00, 'DEBIT', 'Transferencia', '2024-05-01 09:20:00'),
(3, 1, 800000.00, 'DEBIT', 'Pago servicios', '2024-05-01 09:25:00'),
(4, 1, 950000.00, 'DEBIT', 'Compra materiales', '2024-05-01 09:28:00'),
(5, 1, 15000000.00, 'DEBIT', 'Retiro masivo sospechoso', '2024-05-01 09:30:00'),
(6, 2, 200000.00, 'CREDIT', 'Deposito nomina', '2024-05-02 08:00:00'),
(7, 3, 4500000.00, 'DEBIT', 'Transferencia inusual', '2024-05-02 03:14:00'),
(8, 4, 100000.00, 'DEBIT', 'Pago normal', '2024-05-03 11:00:00');
