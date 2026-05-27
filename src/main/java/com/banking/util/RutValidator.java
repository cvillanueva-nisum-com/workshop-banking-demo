package com.banking.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/** Validacion de formato y digito verificador de RUT chileno. */
@Component
public class RutValidator {

    // Formato: 12345678-9 o 12345678-K (con o sin puntos: 12.345.678-9)
    private static final Pattern RUT_PATTERN = Pattern.compile(
        "^(\\d{1,2}\\.?(\\d{3}\\.?){2}-[\\dKk]|\\d{7,8}-[\\dKk])$"
    );

    public boolean isValid(String rut) {
        if (rut == null || rut.isBlank()) return false;

        String clean = rut.replace(".", "").replace("-", "").toUpperCase();

        if (clean.length() < 8 || clean.length() > 9) return false;

        String digits = clean.substring(0, clean.length() - 1);
        char dv = clean.charAt(clean.length() - 1);

        if (!digits.matches("\\d+")) return false;

        int sum = 0;
        int multiplier = 2;
        for (int i = digits.length() - 1; i >= 0; i--) {
            sum += (digits.charAt(i) - '0') * multiplier;
            multiplier = multiplier == 7 ? 2 : multiplier + 1;
        }

        int remainder = 11 - (sum % 11);
        char expected = switch (remainder) {
            case 11 -> '0';
            case 10 -> 'K';
            default -> (char) ('0' + remainder);
        };

        return dv == expected;
    }

    public boolean matchesFormat(String rut) {
        return rut != null && RUT_PATTERN.matcher(rut.trim()).matches();
    }

    public String normalize(String rut) {
        if (!isValid(rut)) throw new IllegalArgumentException("RUT invalido");
        String clean = rut.replace(".", "").replace("-", "").toUpperCase();
        String digits = clean.substring(0, clean.length() - 1);
        char dv = clean.charAt(clean.length() - 1);

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i > 0 && (digits.length() - i) % 3 == 0) formatted.append('.');
            formatted.append(digits.charAt(i));
        }
        return formatted + "-" + dv;
    }
}
