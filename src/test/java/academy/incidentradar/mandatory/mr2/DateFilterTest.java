package academy.incidentradar.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: фильтрация по датам. */
@DisplayName("MR2. Фильтр по датам")
class DateFilterTest {

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Без --from и --to обрабатываются все записи")
    void noFilterKeepsEverything() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Только --from: открытая правая граница")
    void onlyFromIsOpenEnded() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Только --to: открытая левая граница")
    void onlyToIsOpenEnded() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Обе границы: период включительно")
    void bothBoundsAreInclusive() {
        fail("Тест не реализован");
    }
}
