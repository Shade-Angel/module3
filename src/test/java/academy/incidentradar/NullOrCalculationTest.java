package academy.incidentradar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import academy.incidentradar.model.LogRecord;
import academy.incidentradar.parser.LogParser;
import academy.incidentradar.statistics.Statistics;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR2. Граничные случаи")
class NullOrCalculationTest {

    @Test
    @DisplayName("Статистика для пустого списка не вызывает деления на ноль и возвращает корректные нули")
    void statisticsForEmptyListDoesNotCrash() {
        List<LogRecord> emptyRecords = Collections.emptyList();

        assertThatCode(() -> {
                    Statistics stats = new Statistics(emptyRecords);

                    assertThat(stats.getTotalRequests()).isEqualTo(0);
                    assertThat(stats.getResCode()).isEmpty();
                    assertThat(stats.getTopResources()).isEmpty();
                    assertThat(stats.getAvgSize()).isEqualTo(0.0);
                    assertThat(stats.getMaxSize()).isEqualTo(0L);
                    assertThat(stats.getP95Size()).isEqualTo(0.0);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Парсер корректно и безопасно обрабатывает пустые строки и строки из пробелов")
    void parserHandlesEmptyAndBlankLines() {
        LogParser parser = new LogParser();

        assertThat(parser.parse("")).isEmpty();
        assertThat(parser.parse("   ")).isEmpty();
        assertThat(parser.parse("\n\t")).isEmpty();
        assertThat(parser.parse(null)).isEmpty();
    }
}
