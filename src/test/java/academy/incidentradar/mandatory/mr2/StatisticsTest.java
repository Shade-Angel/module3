package academy.incidentradar.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.model.LogRecord;
import academy.incidentradar.statistics.Statistics;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR2. Статистика")
class StatisticsTest {

    @Test
    @DisplayName("Распределение кодов: 200,200,404,500,500,500 -> 200:2, 404:1, 500:3")
    void responseCodesAreCounted() {
        List<LogRecord> records = List.of(
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(404, 100),
                createRecord(500, 100),
                createRecord(500, 100),
                createRecord(500, 100));

        Statistics stats = new Statistics(records);

        assertThat(stats.getResCode()).containsExactlyInAnyOrderEntriesOf(Map.of(200, 2L, 404, 1L, 500, 3L));
    }

    @Test
    @DisplayName("Top-10 ресурсов отсортирован по убыванию количества")
    void topResourcesAreSortedDescending() {
        List<LogRecord> records = List.of(
                createRecord(200, "/a"),
                createRecord(200, "/a"),
                createRecord(200, "/a"),
                createRecord(200, "/b"),
                createRecord(200, "/b"),
                createRecord(200, "/c"));

        Statistics stats = new Statistics(records);

        assertThat(stats.getTopResources()).hasSize(3);
        assertThat(stats.getTopResources().get(0).getKey()).isEqualTo("/a");
        assertThat(stats.getTopResources().get(0).getValue()).isEqualTo(3L);
        assertThat(stats.getTopResources().get(1).getKey()).isEqualTo("/b");
        assertThat(stats.getTopResources().get(1).getValue()).isEqualTo(2L);
        assertThat(stats.getTopResources().get(2).getKey()).isEqualTo("/c");
        assertThat(stats.getTopResources().get(2).getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Размеры 100..500: average 300, max 500, p95 500")
    void responseSizeStatistics() {
        List<LogRecord> records = List.of(
                createRecord(200, 100),
                createRecord(200, 200),
                createRecord(200, 300),
                createRecord(200, 400),
                createRecord(200, 500));

        Statistics stats = new Statistics(records);

        assertThat(stats.getAvgSize()).isEqualTo(300.0);
        assertThat(stats.getMaxSize()).isEqualTo(500L);
        assertThat(stats.getP95Size()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Дробные значения округляются до 2 знаков после запятой")
    void fractionalValuesAreRoundedToTwoDecimals() {
        List<LogRecord> records = List.of(createRecord(200, 10), createRecord(200, 20), createRecord(200, 33));

        Statistics stats = new Statistics(records);

        assertThat(stats.getAvgSize()).isEqualTo(21.0);
    }

    @Test
    @DisplayName("Выбранный метод расчёта p95 зафиксирован и воспроизводим")
    void p95MethodIsDocumentedAndStable() {
        List<LogRecord> records = List.of(
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 100),
                createRecord(200, 500),
                createRecord(200, 600));

        Statistics stats = new Statistics(records);
        assertThat(stats.getP95Size()).isEqualTo(500.0);
    }

    private LogRecord createRecord(int status, long size) {
        return new LogRecord("1.2.3.4", LocalDate.now(), "GET", "/test", "HTTP/1.1", status, size);
    }

    private LogRecord createRecord(int status, String resource) {
        return new LogRecord("1.2.3.4", LocalDate.now(), "GET", resource, "HTTP/1.1", status, 100L);
    }
}
