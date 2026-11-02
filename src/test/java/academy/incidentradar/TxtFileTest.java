package academy.incidentradar;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.support.CliRunner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR1. Поддержка .txt файлов")
class TxtFileTest {

    @Test
    @DisplayName("Файл с расширением .txt с логами читается")
    void textIsAccepted() throws IOException {
        Path tempDir = Files.createTempDirectory("radar-txt");
        Path txtLofFile = tempDir.resolve("info.txt");
        Files.writeString(
                txtLofFile,
                "93.180.71.3 - - [17/May/2025:08:05:32 +000] "
                        + "\"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3\"%n".formatted());

        CliRunner.Result result = CliRunner.run("--path", txtLofFile.toString(), "--output-format", "markdown");

        assertThat(result.exitCode())
                .as("Программа должна прочитать txt файл.  - %s", result.stderr())
                .isEqualTo(0);
        assertThat(result.stdout()).isNotBlank();
    }

    @Test
    @DisplayName("Неподдериваемое расширение отвергается  кодом 2")
    void unSupport() throws IOException {
        Path tempDir = Files.createTempDirectory("radar-file");
        Path xmlLogFile = tempDir.resolve("data.xml");
        Files.writeString(xmlLogFile, "some \" xml \" data");

        CliRunner.Result result = CliRunner.run("--path", xmlLogFile.toString(), "--output-format", "markdown");

        assertThat(result.exitCode()).isEqualTo(2);
    }
}
