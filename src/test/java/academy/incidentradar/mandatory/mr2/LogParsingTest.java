package academy.incidentradar.mandatory.mr2;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: разбор строки лога. */
@DisplayName("MR2. Парсер строки лога")
class LogParsingTest {

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Эталонная строка разбирается на корректные поля")
    void referenceLineIsParsedCorrectly() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Метод, ресурс и протокол разбираются в три отдельных поля")
    void requestIsSplitIntoThreeFields() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR2: реализуй тест и удали эту строку")
    @DisplayName("Битая строка пропускается с warning'ом, программа не падает")
    void malformedLineIsSkippedWithWarning() {
        fail("Тест не реализован");
    }
}
