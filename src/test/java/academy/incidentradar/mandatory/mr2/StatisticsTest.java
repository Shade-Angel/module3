package academy.incidentradar.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: подсчёт статистики. */
@DisplayName("MR2. Статистика")
class StatisticsTest {

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Распределение кодов: 200,200,404,500,500,500 -> 200:2, 404:1, 500:3")
    void responseCodesAreCounted() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Top-10 ресурсов отсортирован по убыванию количества")
    void topResourcesAreSortedDescending() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Размеры 100..500: average 300, max 500, p95 500")
    void responseSizeStatistics() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Дробные значения округляются до 2 знаков после запятой")
    void fractionalValuesAreRoundedToTwoDecimals() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Выбранный метод расчёта p95 зафиксирован и воспроизводим")
    void p95MethodIsDocumentedAndStable() {
        fail("Тест не реализован");
    }
}
