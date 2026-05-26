# GitHub Copilot Instructions - Banco Demo

## Stack & Versions
- Java 17 (use records, sealed classes, text blocks where appropriate)
- Spring Boot 3.2 (use constructor injection, not @Autowired)
- Spring Data JPA (H2 in demo / Oracle 19c in production)
- Lombok (@RequiredArgsConstructor, @Data, @Builder)
- JUnit 5 + Mockito + AssertJ for tests

## Code Conventions
- Use records for DTOs and immutable value objects
- Use @RequiredArgsConstructor instead of manual constructors
- Service layer: @Transactional on write operations only
- Controllers: thin — delegate all logic to services
- Repositories: no business logic, only query methods
- Package structure: feature-based (account/, loan/, transaction/, fraud/)

## Banking Domain Rules
- All monetary amounts use BigDecimal — NEVER float or double
- Use RoundingMode.HALF_UP for monetary calculations
- Validate DTI (debt-to-income) before any loan approval
- Minimum credit score for any loan: 650
- Transaction timestamps always stored as LocalDateTime (UTC)
- Log all state transitions (ACTIVE → DEFAULTED, etc.) at INFO level

## Security & Compliance (PCI-DSS / SBIF)
- NEVER log full account numbers — mask as BANK-XXX-****
- NEVER include RUT, card numbers, or tokens in log messages
- All endpoints require authentication in production (Spring Security)
- Fraud detection alerts must be persisted — never discard silently
- Use parameterized queries only — no string concatenation in queries

## Testing Standards
- Minimum 80% coverage on Service classes
- Use @DisplayName with business language (Spanish OK)
- Test names: shouldDoX_whenY pattern
- Mock repositories in unit tests, use @SpringBootTest for integration
- Always test: happy path, null inputs, boundary values, domain exceptions

## Naming
- Controllers: {Feature}Controller
- Services: {Feature}Service
- Repositories: {Feature}Repository
- DTOs (request): {Action}Request (record)
- DTOs (response): {Action}Response (record)
- Entities: {Feature} (no suffix)
