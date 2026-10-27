package academy.incidentradar;

import static org.assertj.core.api.Assertions.assertThat;

import academy.incidentradar.support.CliRunner;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Проверка окружения. Если эти тесты упали — дело в настройке, а не в задании, см. README. */
@DisplayName("Проверка окружения")
class TemplateSmokeTest {

    private static final Path SAMPLES = Path.of("samples", "logs");
    private static final int EXPECTED_BULK_LINES = 3000;

    /** Формат строки NGINX-лога. Нужен только для проверки целостности готовых сэмплов. */
    private static final Pattern LOG_LINE = Pattern.compile(
            "^(\\S+) - (\\S+) \\[([^\\]]+)] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+) \"([^\"]*)\" \"([^\"]*)\"$");

    @Test
    @DisplayName("Приложение запускается и завершается с кодом 0")
    void applicationStartsAndExitsCleanly() {
        var result = CliRunner.run();

        assertThat(result.exitCode())
                .as("Программа завершилась с ненулевым кодом.%n%s", result)
                .isZero();
        assertThat(result.stdout()).as("Программа ничего не напечатала").isNotBlank();
    }

    @Test
    @DisplayName("Сэмплы логов на месте и содержат 3000 корректных строк")
    void bundledSampleLogsAreIntact() {
        var lines = Stream.of("access-2025-03-01.log", "access-2025-03-02.log")
                .map(SAMPLES::resolve)
                .flatMap(TemplateSmokeTest::readLines)
                .toList();

        assertThat(lines)
                .as("В samples/logs должно быть 3000 строк — на этом числе построен samples/logs/README.md")
                .hasSize(EXPECTED_BULK_LINES);

        assertThat(lines)
                .as("Все строки основных сэмплов должны соответствовать формату NGINX-лога")
                .allSatisfy(line -> assertThat(LOG_LINE.matcher(line).matches())
                        .as("строка не соответствует формату: %s", line)
                        .isTrue());
    }

    @Test
    @DisplayName("Файл с битыми строками содержит оба вида поломок")
    void malformedSampleHasBothKindsOfBreakage() {
        var lines = readLines(SAMPLES.resolve("malformed.log")).toList();

        assertThat(lines).as("В malformed.log должно быть 13 строк").hasSize(13);

        var structurallyValid =
                lines.stream().filter(line -> LOG_LINE.matcher(line).matches()).count();

        assertThat(lines.size() - structurallyValid)
                .as("Структурно битых строк должно быть 5")
                .isEqualTo(5);
        assertThat(structurallyValid)
                .as("Формату соответствуют 8 строк, но одна из них с непарсящейся датой — см. samples/logs/README.md")
                .isEqualTo(8);
    }

    private static Stream<String> readLines(Path path) {
        assertThat(path).as("Файл %s должен лежать в репозитории", path).exists();
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream();
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать " + path, e);
        }
    }
}
