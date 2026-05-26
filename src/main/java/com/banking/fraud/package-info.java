/**
 * DEMO ESCENARIO 5 - AGENT MODE (Finale del Workshop)
 *
 * Este paquete esta INTENCIONALMENTE VACIO.
 *
 * El facilitador usara el siguiente prompt con GitHub Copilot Agent Mode
 * para que el asistente cree el modulo completo de forma autonoma:
 *
 * ---
 * Crear feature completo de deteccion de fraude para Banco Demo.
 * Requerimiento: detectar transacciones sospechosas en tiempo real.
 *
 * Crear en el paquete com.demo.banking.fraud:
 * - FraudCheckController (POST /api/v1/fraud/check)
 * - FraudCheckService con reglas:
 *     * monto > 3x promedio historico = SUSPICIOUS
 *     * 5 o mas transacciones en 10 minutos = BLOCKED
 *     * transaccion entre 00:00-05:00 hrs > $1.000.000 = REVIEW
 * - FraudRepository
 * - TransactionRequest DTO con validaciones
 * - FraudCheckResponse DTO (status: APPROVED/SUSPICIOUS/BLOCKED/REVIEW, reason, riskScore)
 * - FraudAlertEntity para persistir alertas
 * - JUnit 5 tests cubriendo los 8 casos de negocio
 * - Javadoc en todos los metodos publicos
 *
 * Stack: Java 17, Spring Boot 3.2, JPA con H2. Seguir el estilo
 * del resto del proyecto (records para DTOs, @RequiredArgsConstructor).
 * ---
 */
package com.demo.banking.fraud;
