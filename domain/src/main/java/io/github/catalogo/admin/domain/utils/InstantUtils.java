package io.github.catalogo.admin.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.MICROS;

public final class InstantUtils {
    private InstantUtils() {}
    public static Instant now() {
        return Instant
                .now()
                .truncatedTo(MICROS);
    }
}
