package academy.incidentradar.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import academy.incidentradar.support.CliRunner;

/** Обязательные тесты: валидация аргументов командной строки. */
@DisplayName("MR1. Валидация аргументов CLI")
class CliValidationTest {

    @Test
    @DisplayName("Не передан обязательный --path -> exit code 2")
    void missingPathExitsWithTwo() {
        CliRunner.Result result = CliRunner.run("--output-format", "json");
        assertThat(result.exitCode()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"xml", "csv", "txt"})
    @DisplayName("Неподдерживаемый --output-format \"{0}\" -> exit code 2")
    void unsupportedOutputFormatExitsWithTwo(String format) {
        CliRunner.Result result = CliRunner.run("--path", "samples/logs/access-2025-03-01.log", "--output-format", format);
        assertThat(result.exitCode()).isEqualTo(2);
    }

    @Test
    @DisplayName("--from больше или равен --to -> exit code 2")
    void fromNotBeforeToExitsWithTwo() {
        CliRunner.Result result1 = CliRunner.run("--path", "samples/logs/access-2025-03-01.log", "--from", "2025-03-02", "--to", "2025-03-01");
        assertThat(result1.exitCode()).isEqualTo(2);
        CliRunner.Result result2 = CliRunner.run("--path", "samples/logs/access-2025-03-01.log", "--from", "2025-03-01", "--to", "2025-03-01");
        assertThat(result2.exitCode()).isEqualTo(2);
    }

    @Test
    @DisplayName("Несуществующий локальный файл -> exit code 2")
    void missingLocalFileExitsWithTwo() {
        CliRunner.Result result = CliRunner.run("--path", "samples/logs/does_not_exist.log", "--output-format", "json");
        assertThat(result.exitCode()).isEqualTo(2);
    }

    @Test
    @DisplayName("Расширение файла отчёта не соответствует формату -> exit code 2")
    void outputExtensionMismatchExitsWithTwo() throws IOException {
        Path tempDir = Files.createTempDirectory("radar");
        Path report = tempDir.resolve("report.md");
        CliRunner.Result result = CliRunner.run(
            "--path", "samples/logs/access-2025-03-01.log", 
            "--output-format", "json", 
            "--output-path", report.toString()
        );
        assertThat(result.exitCode()).isEqualTo(2);
    }
}
