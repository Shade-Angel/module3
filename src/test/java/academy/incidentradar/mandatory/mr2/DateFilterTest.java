package academy.incidentradar.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.filter.DateFilter;
import academy.incidentradar.model.LogRecord;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR2. Фильтр по датам")
class DateFilterTest {

    @Test
    @DisplayName("Без --from и --to обрабатываются все записи")
    void noFilterKeepsEverything() {
        DateFilter filter = new DateFilter(null, null);
        LogRecord record = createRecord(LocalDate.of(2025, 3, 15));

        assertThat(filter.test(record)).isTrue();
    }

    @Test
    @DisplayName("Только --from: открытая правая граница")
    void onlyFromIsOpenEnded() {
        DateFilter filter = new DateFilter(LocalDate.of(2025, 3, 10), null);

        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 9)))).isFalse();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 10)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 11)))).isTrue();
    }

    @Test
    @DisplayName("Только --to: открытая левая граница")
    void onlyToIsOpenEnded() {
        DateFilter filter = new DateFilter(null, LocalDate.of(2025, 3, 10));

        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 9)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 10)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 11)))).isFalse();
    }

    @Test
    @DisplayName("Обе границы: период включительно")
    void bothBoundsAreInclusive() {
        DateFilter filter = new DateFilter(LocalDate.of(2025, 3, 5), LocalDate.of(2025, 3, 15));

        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 4)))).isFalse();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 5)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 10)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 15)))).isTrue();
        assertThat(filter.test(createRecord(LocalDate.of(2025, 3, 16)))).isFalse();
    }

    private LogRecord createRecord(LocalDate date) {
        return new LogRecord("1.2.3.4", date, "GET", "/test", "HTTP/1.1", 200, 100L);
    }
}
