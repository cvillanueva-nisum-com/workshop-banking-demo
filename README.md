# Workshop Banking Demo

Proyecto de práctica para el workshop de GitHub Copilot.
Stack: Spring Boot 3.2 + Java 17 + H2 (in-memory).

## Setup

**Requisitos**
- Java 17
- Maven 3.8+ (o usar el wrapper incluido)

**Arrancar la app**
```bash
./mvnw spring-boot:run
```

La app levanta en `http://localhost:8080` con datos de prueba cargados automáticamente.

**Consola H2** (base de datos en memoria)
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:workshopdb
User: sa  /  Password: (vacío)
```

## Endpoints disponibles

```
# Cuentas
GET  /api/v1/accounts
GET  /api/v1/accounts/{accountNumber}/balance
POST /api/v1/accounts/transfer

# Créditos
GET  /api/v1/loans/account/{accountId}
GET  /api/v1/loans/defaulted
POST /api/v1/loans?dti=0.35&creditScore=720

# Transacciones
GET  /api/v1/transactions/account/{accountId}
GET  /api/v1/transactions/account/{accountId}/summary?date=2024-05-01
GET  /api/v1/transactions/account/{accountId}/monthly-volume?year=2024&month=5
```

## Correr los tests

```bash
./mvnw test
```

## Estructura del proyecto

```
src/main/java/com/banking/
├── account/       # Cuentas y transferencias
├── loan/          # Solicitudes y validación de créditos
├── transaction/   # Transacciones y reportes
├── fraud/         # (módulo a implementar durante el workshop)
└── util/          # Utilidades
```
