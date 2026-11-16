package academy.incidentradar.mandatory.mr3;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.support.CliRunner;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Обязательные тесты: отчёты и коды возврата — blackbox-проверки CLI. */
@DisplayName("MR3. Отчёты и коды возврата")
class ReportAndExitCodeTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("JSON-отчёт валиден и содержит все обязательные поля")
    void jsonReportIsValidAndComplete() {
        Path output = tempDir.resolve("report.json");
        Path logFile = tempDir.resolve("test.log");

        String logContent =
                "192.168.1.1 - - [17/May/2015:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Test\"\n";
        try {
            Files.writeString(logFile, logContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = CliRunner.run(
                "--path", logFile.toString(), "--output-format", "json", "--output-path", output.toString());

        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(output).exists();

        String json;
        try {
            json = Files.readString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(json).contains("\"totalRequestsCount\"");
        assertThat(json).contains("\"average\"");
        assertThat(json).contains("\"max\"");
        assertThat(json).contains("\"p95\"");
    }

    @Test
    @DisplayName("Markdown-отчёт содержит заголовок и таблицы")
    void markdownReportHasHeaderAndTables() {
        Path output = tempDir.resolve("report.md");
        Path logFile = tempDir.resolve("test.log");

        String logContent =
                "192.168.1.1 - - [17/May/2015:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Test\"\n";
        try {
            Files.writeString(logFile, logContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = CliRunner.run(
                "--path", logFile.toString(), "--output-format", "markdown", "--output-path", output.toString());

        assertThat(result.exitCode()).isEqualTo(0);
        assertThat(output).exists();

        String md;
        try {
            md = Files.readString(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(md).contains("# Incident Radar Report");
        assertThat(md).contains("| Code | Count |");
        assertThat(md).contains("| Resource | Count |");
    }

    @Test
    @DisplayName("Существующий файл отчёта не перезаписывается -> ошибка")
    void existingOutputFileIsNotOverwritten() {
        Path output = tempDir.resolve("existing.json");
        Path logFile = tempDir.resolve("test.log");

        try {
            Files.writeString(output, "old content");
            Files.writeString(
                    logFile,
                    "192.168.1.1 - - [17/May/2015:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Test\"\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = CliRunner.run(
                "--path", logFile.toString(), "--output-format", "json", "--output-path", output.toString());

        assertThat(result.exitCode()).isEqualTo(2);
        assertThat(result.stderr()).contains("уже существует");

        try {
            assertThat(Files.readString(output)).isEqualTo("old content");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Успешный запуск -> exit code 0")
    void successfulRunExitsWithZero() {
        Path output = tempDir.resolve("report.md");
        Path logFile = tempDir.resolve("test.log");

        try {
            Files.writeString(
                    logFile,
                    "192.168.1.1 - - [17/May/2015:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Test\"\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = CliRunner.run("--path", logFile.toString(), "--output-path", output.toString());
        assertThat(result.exitCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("Некорректное использование -> exit code 2, а не 1")
    void misuseExitsWithTwoNotOne() {
        var result1 = CliRunner.run("--output-format", "json");
        assertThat(result1.exitCode()).isEqualTo(2);

        var result2 = CliRunner.run("--path", "nonexistent_file_12345.log");
        assertThat(result2.exitCode()).isEqualTo(2);

        Path logFile = tempDir.resolve("test.log");
        try {
            Files.writeString(
                    logFile,
                    "192.168.1.1 - - [17/May/2015:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Test\"\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result3 = CliRunner.run("--path", logFile.toString(), "--output-format", "xml");
        assertThat(result3.exitCode()).isEqualTo(2);
    }
}
