package academy.incidentradar.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Обязательные тесты: валидация аргументов командной строки. */
@DisplayName("MR1. Валидация аргументов CLI")
class CliValidationTest {

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Не передан обязательный --path -> exit code 2")
    void missingPathExitsWithTwo() {
        fail("Тест не реализован");
    }

    @ParameterizedTest
    @ValueSource(strings = {"xml", "csv", "txt"})
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Неподдерживаемый --output-format \"{0}\" -> exit code 2")
    void unsupportedOutputFormatExitsWithTwo(String format) {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("--from больше или равен --to -> exit code 2")
    void fromNotBeforeToExitsWithTwo() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Несуществующий локальный файл -> exit code 2")
    void missingLocalFileExitsWithTwo() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Расширение файла отчёта не соответствует формату -> exit code 2")
    void outputExtensionMismatchExitsWithTwo() {
        fail("Тест не реализован");
    }
}
