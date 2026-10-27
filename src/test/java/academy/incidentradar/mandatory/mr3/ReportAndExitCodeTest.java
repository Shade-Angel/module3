package academy.incidentradar.mandatory.mr3;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: отчёты и коды возврата — blackbox-проверки CLI. */
@DisplayName("MR3. Отчёты и коды возврата")
class ReportAndExitCodeTest {

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("JSON-отчёт валиден и содержит все обязательные поля")
    void jsonReportIsValidAndComplete() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Markdown-отчёт содержит заголовок и таблицы")
    void markdownReportHasHeaderAndTables() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Существующий файл отчёта не перезаписывается -> ошибка")
    void existingOutputFileIsNotOverwritten() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Успешный запуск -> exit code 0")
    void successfulRunExitsWithZero() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR3: реализуй тест и удали эту строку")
    @DisplayName("Некорректное использование -> exit code 2, а не 1")
    void misuseExitsWithTwoNotOne() {
        fail("Тест не реализован");
    }
}
