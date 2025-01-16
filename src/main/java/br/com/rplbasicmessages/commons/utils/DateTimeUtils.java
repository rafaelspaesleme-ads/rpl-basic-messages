package br.com.rplbasicmessages.commons.utils;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    /**
     * Verifica se o período entre login e logoff atingiu ou ultrapassou o tempo especificado.
     *
     * @param loginTime       O momento em que o login foi feito.
     * @param logoffTime      O momento em que o logoff foi feito.
     * @param durationMinutes O tempo limite em minutos.
     * @return true se o período atingiu ou ultrapassou o tempo especificado, false caso contrário.
     */
    public static boolean isDurationExceeded(
            @NotNull(message = "Data inicio não pode ser nulo") LocalDateTime loginTime,
            @NotNull(message = "Data final não pode ser nulo") LocalDateTime logoffTime,
            @NotNull(message = "Duração não pode ser nulo") long durationMinutes) {
        // Calcula a duração entre loginTime e logoffTime em minutos
        long durationBetween = Duration.between(loginTime, logoffTime).toMinutes();

        // Verifica se o tempo entre os períodos é maior ou igual ao limite especificado
        return durationBetween >= durationMinutes;
    }
}
