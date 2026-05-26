# Demo Banking Demo — GitHub Copilot Workshop

Proyecto demo para el workshop de GitHub Copilot con Banco Demo.
Diseñado con escenarios pre-armados para demostrar las capacidades de Copilot progresivamente.

## Setup rapido

```bash
./mvnw spring-boot:run
# o
mvn spring-boot:run
```

La app arranca en `http://localhost:8080` con datos de prueba cargados automáticamente.
H2 console disponible en `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:workshopdb`)

---

## Escenarios de Demo

### Escenario 1 — Inline Autocomplete / Refactoring
**Archivo:** `loan/LoanValidator.java`

El metodo `validate()` tiene 10 niveles de anidamiento. Funciona, pero nadie lo quiere tocar.

**Demo:** Seleccionar el metodo → Copilot Chat → escribir:
```
Refactorizar usando early returns y extrayendo condiciones a metodos privados
con nombres descriptivos. Mantener identica la logica de negocio.
```

**WOW esperado:** Copilot convierte 70 lineas anidadas en ~30 lineas limpias en segundos.

---

### Escenario 2 — Bug Detection con /fix
**Archivo:** `transaction/TransactionService.java`

`getDailySummary()` tiene 2 bugs que pasaron code review:
1. Rango de fechas excluye transacciones en 23:59:59.XXX nanosegundos
2. Calculo de porcentaje usa division entera (pierde decimales)

**Demo:** Seleccionar el metodo → `/fix Revisar bugs sutiles, edge cases y precision numerica`

Alternativamente, correr `TransactionServiceTest` — el test `debitPercentageShouldBeAccurate` falla, lo que hace el bug evidente.

---

### Escenario 3 — /explain
**Archivo:** `util/RutValidator.java`

Validador de RUT chileno heredado del 2015. Regex + algoritmo módulo 11.

**Demo:** Seleccionar toda la clase → `/explain Explicar el regex y el algoritmo modulo 11 paso a paso`

**WOW esperado:** Copilot explica el modulo 11 mejor que la documentacion original del SII.

---

### Escenario 4 — Generacion de Tests con /tests
**Archivo:** `account/AccountService.java`

Logica correcta de transferencias pero sin tests. `LoanValidatorTest` tiene solo 3 casos de 12+ posibles.

**Demo:** Seleccionar `AccountService` → `/tests Generar tests unitarios cubriendo todos los casos de negocio`

**WOW esperado:** Copilot genera 8-10 tests con @DisplayName descriptivos cubriendo todos los edge cases.

---

### Escenario 5 — Agent Mode (FINALE)
**Paquete:** `fraud/` (intencionalmente vacio)

**Prompt Maestro para Agent Mode:**
```
Crear feature completo de deteccion de fraude para Banco Demo.
Requerimiento: detectar transacciones sospechosas en tiempo real.

Crear en el paquete com.demo.banking.fraud:
- FraudCheckController (POST /api/v1/fraud/check)
- FraudCheckService con reglas:
    * monto > 3x promedio historico = SUSPICIOUS
    * 5 o mas transacciones en 10 minutos = BLOCKED
    * transaccion entre 00:00-05:00 hrs > $1.000.000 = REVIEW
- FraudRepository
- TransactionRequest DTO con validaciones
- FraudCheckResponse DTO (status: APPROVED/SUSPICIOUS/BLOCKED/REVIEW, reason, riskScore)
- FraudAlertEntity para persistir alertas
- JUnit 5 tests cubriendo los 8 casos de negocio
- Javadoc en todos los metodos publicos

Stack: Java 17, Spring Boot 3.2, JPA. Seguir copilot-instructions.md.
```

**WOW esperado:** Copilot crea 6+ archivos, tests, Javadoc — todo en ~3 minutos.

---

## Endpoints disponibles para testing

```
GET  /api/v1/accounts
GET  /api/v1/accounts/{accountNumber}/balance
POST /api/v1/accounts/transfer

GET  /api/v1/loans/account/{accountId}
GET  /api/v1/loans/defaulted
POST /api/v1/loans?dti=0.35&creditScore=720

GET  /api/v1/transactions/account/{accountId}
GET  /api/v1/transactions/account/{accountId}/summary?date=2024-05-01
GET  /api/v1/transactions/account/{accountId}/monthly-volume?year=2024&month=5
```

---

## Datos de prueba cargados

| ID | Cuenta | Saldo | Tipo |
|---|---|---|---|
| 1 | BANK-001-2024 | $15.000.000 | CORRIENTE (activa) |
| 2 | BANK-002-2024 | $8.500.000 | VISTA (activa) |
| 3 | BANK-003-2024 | $2.300.000 | CORRIENTE (activa) |
| 4 | BANK-004-2024 | $45.000.000 | CORRIENTE (activa) |
| 5 | BANK-005-2024 | $0 | VISTA (INACTIVA) |

La cuenta 1 tiene transacciones del 2024-05-01 incluyendo un retiro sospechoso de $15.000.000.
La cuenta 3 tiene un préstamo en estado DEFAULTED.
