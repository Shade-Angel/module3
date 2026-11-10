package academy.incidentradar.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import academy.incidentradar.model.LogRecord;
import academy.incidentradar.parser.LogParser;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: разбор строки лога. */
@DisplayName("MR2. Парсер строки лога")
class LogParsingTest {
    private final LogParser parser = new LogParser();

    @Test
    @DisplayName("Эталонная строка разбирается на корректные поля")
    void referenceLineIsParsedCorrectly() {
        String line = "93.180.71.3 - - [17/May/2015:08:05:32 +0000] "
                + "\"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3\"";
        Optional<LogRecord> record = parser.parse(line);

        assertThat(record).isPresent();
        LogRecord r = record.get();
        assertThat(r.ip()).isEqualTo("93.180.71.3");
        assertThat(r.method()).isEqualTo("GET");
        assertThat(r.resource()).isEqualTo("/downloads/product_1");
        assertThat(r.protocol()).isEqualTo("HTTP/1.1");
        assertThat(r.status()).isEqualTo(304);
        assertThat(r.bodyBytesSent()).isEqualTo(0);
    }

    @Test
    @DisplayName("Метод, ресурс и протокол разбираются в три отдельных поля")
    void requestIsSplitIntoThreeFields() {
        String line =
                "1.2.3.4 - - [01/Mar/2025:00:00:00 +0000] " + "\"POST /api/v1/test HTTP/2.0\" 200 100 \"-\" \"-\"";
        Optional<LogRecord> record = parser.parse(line);

        assertThat(record).isPresent();
        assertThat(record.get().method()).isEqualTo("POST");
        assertThat(record.get().resource()).isEqualTo("/api/v1/test");
        assertThat(record.get().protocol()).isEqualTo("HTTP/2.0");
    }

    @Test
    @DisplayName("Битая строка пропускается с warning'ом, программа не падает")
    void malformedLineIsSkippedWithWarning() {
        String malformedLine = "this is not a valid log line";

        assertThatCode(() -> {
                    Optional<LogRecord> record = parser.parse(malformedLine);
                    assertThat(record).isEmpty();
                })
                .doesNotThrowAnyException();
    }
}
