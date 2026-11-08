package academy.incidentradar.model;

import java.time.LocalDate;

public record LogRecord(
        String ip,
        LocalDate date,
        String method,
        String resource,
        String protocol,
        int status,
        long bodyBytesSent
) {}
