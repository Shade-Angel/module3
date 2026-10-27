package academy.incidentradar.mandatory.mr1;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Обязательные тесты: источники логов. */
@DisplayName("MR1. Источники логов")
class LogSourceTest {

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Локальный файл читается")
    void readsLocalFile() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Glob-маска: в отчёт попадают все подошедшие файлы")
    void readsAllFilesMatchingGlob() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("Лог читается по URL")
    void readsRemoteFile() {
        fail("Тест не реализован");
    }

    @Test
    @Disabled("MR1: реализуй тест и удали эту строку")
    @DisplayName("URL вернул 404 -> exit code 2")
    void remote404ExitsWithTwo() {
        fail("Тест не реализован");
    }
}
