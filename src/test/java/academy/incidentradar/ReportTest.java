package academy.incidentradar;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.support.CliRunner;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("MR3. Личные тесты: граничные случаи")
class ReportTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Пустой файл логов генерирует валидный отчёт с нулями")
    void emptyLogFileGeneratesValidReport() {
        Path output = tempDir.resolve("empty_report.json");
        Path logFile = tempDir.resolve("empty.log");

        try {
            Files.writeString(logFile, "");
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
}
